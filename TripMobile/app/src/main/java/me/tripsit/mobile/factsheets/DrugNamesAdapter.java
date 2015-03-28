package me.tripsit.mobile.factsheets;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddie on 28/03/15.
 */
public class DrugNamesAdapter extends ArrayAdapter<String> implements Filterable {

    private final DrugNameFilter filter;
    private final List<String> originalList;

    public DrugNamesAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        originalList = new ArrayList<String>(objects);
        filter = new DrugNameFilter();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class DrugNameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            clear();
            for (String drugName : originalList) {
                if (drugName.toLowerCase().contains(constraint.toString().toLowerCase())) {
                    add(drugName);
                }
            }
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }
}
