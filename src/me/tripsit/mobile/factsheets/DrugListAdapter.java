package me.tripsit.mobile.factsheets;

import java.util.List;
import java.util.TreeMap;

import me.tripsit.mobile.R;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DrugListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private TreeMap<String, List<String>> content;
	
	public DrugListAdapter(Context context, TreeMap<String, List<String>> content) {
		this.context = context;
		this.content = content;
	}
	
	@Override
	public int getGroupCount() {
		return content.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return content.get(getGroup(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return content.keySet().toArray()[groupPosition];
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return content.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_ListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final String childText = (String) getChild(groupPosition, childPosition);
		 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.txt_listItem);
 
        txtListChild.setText(childText);
        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
