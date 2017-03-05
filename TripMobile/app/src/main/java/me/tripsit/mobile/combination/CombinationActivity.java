package me.tripsit.mobile.combination;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.tripsit.mobile.R;
import me.tripsit.mobile.TripMobileActivity;
import me.tripsit.mobile.combinations.Combinations;

/**
 * Created by alex on 08/07/16.
 */
public class CombinationActivity extends TripMobileActivity implements View.OnClickListener, TextWatcher {

    private ViewGroup inputContainer;
    private EditText firstInput;
    private EditText secondInput;

    private ProgressBar progressBar;

    private ViewGroup combinationContainer;
    private TextView header;
    private TextView content;

    private List<View> viewsToAnimate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Look up all the views we will want to use while running
        progressBar = (ProgressBar)findViewById(R.id.progress_combinations);
        inputContainer = (ViewGroup)findViewById(R.id.input_container);
        combinationContainer = (ViewGroup)findViewById(R.id.combo_container);
        content = (TextView)findViewById(R.id.txt_combo_content);
        header = (TextView)findViewById(R.id.txt_combo_header);
        firstInput = (EditText)findViewById(R.id.input_first_drug);
        secondInput = (EditText)findViewById(R.id.input_second_drug);

        viewsToAnimate = new ArrayList<>();
        viewsToAnimate.add(combinationContainer);
        viewsToAnimate.add(inputContainer);

        // Set listeners
        findViewById(R.id.btn_search).setOnClickListener(this);

        // Listen to edit text changes and reset background color of top window
        // only to indicate their input is no longer valid
        firstInput.addTextChangedListener(this);
        secondInput.addTextChangedListener(this);

        //testCombinationActivity();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_combination;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // not used
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        clearCombination();
    }

    private void clearCombination() {
        header.setText("");
        content.setText("");
    }

    @Override
    public void afterTextChanged(Editable s) {
        // not used
    }

    private Pair<String, String> getInputs() {
        String firstDrug = firstInput.getText().toString().trim();
        String secondDrug = secondInput.getText().toString().trim();

        return new Pair<>(firstDrug, secondDrug);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ) {
            case R.id.btn_search:
                // Start the selection dialog.
                // Parse out the inputs
                Pair<String, String> inputs = getInputs();
                searchForCombination(inputs.first, inputs.second);
                break;
        }
    }

    // tried showing the inputs in a dialog but if you're shaking
    // while trying to check danger, you might hit
    // the dialog background and cancel your inputs
    // so i'm using it for errors
    private void displayError(final Combination code) {
        String error = getString(R.string.operation_failed);

        Pair<String, String> inputs = getInputs();
        String drug = null;
        switch ( code.error ) {
            case DRUG_A_NOT_FOUND:
                drug = inputs.first;
                if ( inputs.first.length() == 0 ) {
                    drug = getString(R.string.failed_empty_drug);
                }
                error = String.format(getString(R.string.failed_search_combo), drug);
                break;
            case DRUG_B_NOT_FOUND:
                drug = inputs.second;
                if ( inputs.second.length() == 0 ) {
                    drug = getString(R.string.failed_empty_drug);
                }
                error = String.format(getString(R.string.failed_search_combo), drug);
                break;
            case NETWORK_ERROR:
                error = getString(R.string.failed_download_combinations);
                break;
        }

        content.setText(error);
        content.setVisibility(View.VISIBLE);
    }

    public void searchForCombination(String firstDrug, String secondDrug) {
        progressBar.setVisibility(View.VISIBLE);

        new CombinationAsyncTask(){
            @Override
            protected void onPostExecute(Combination combination) {
                progressBar.setVisibility(View.GONE);
                if (combination.error != null) {
                    displayError(combination);
                } else {
                    displayCombination(combination);
                }
            }
        }.execute(firstDrug, secondDrug);
    }

    private void displayCombination(Combination combination) {
        Combination.CombinationSeverity severity = combination.getSeverity(this);

        if ( severity == null ) {
            combination.error = Combination.ErrorCode.GENERAL_FAILURE;
            combination.exception = new InvalidParameterException("No severity on valid combination?");
            displayError(combination);
            return;
        }

        String formattedContent = String.format(Locale.UK, severity.getContent(this), combination.drugA, combination.drugB);

        String finalContent = formattedContent;
        if ( combination.note != null ) {
            finalContent = String.format("%1$s\n%2$s", formattedContent, combination.note);
        }

        header.setText(severity.getHeader(this));
        content.setText(finalContent);

        int color = getResources().getColor(severity.backgroundColor);

        header.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
    }

    Button search = null;
    private void testCombinationActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Perform a few queries.
                search = (Button)findViewById(R.id.btn_search);
                testOne("mdma", "tramadol"); // deadly
                testOne("tramadol", "ssris"); // seratonin?
                testOne("lsd", "tramadol"); // decrease
                testOne("lsd", "dxm"); // increase
                testOne("amphetamines", "dxm"); // nothing ?
                testOne("fail", "tramadol"); // first not found
                testOne("tramadol", "fail"); // second not found
            }
        }).start();
    }

    private void testOne(final String first, final String second) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                firstInput.setText(first);
                secondInput.setText(second);
                onClick(search);
            }
        });

        try {
            Thread.sleep(3700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
