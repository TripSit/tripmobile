package me.tripsit.mobile.factsheets;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

import me.tripsit.mobile.R;

public class DrugListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private LinkedHashMap<String, List<String>> content;
	
	public DrugListAdapter(Context context, LinkedHashMap<String, List<String>> content) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_header, parent, false);
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

		LinearLayout layout = (LinearLayout)convertView;
        if (layout == null || layout.getTag() == null ) {
            layout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
			layout.setTag(new ChildViewHolder(layout));
        }
		ChildViewHolder holder = (ChildViewHolder)layout.getTag();

		holder.text.setText(childText);

		if ( isLastChild ) {
			holder.divider.setVisibility(View.VISIBLE);
		} else {
			holder.divider.setVisibility(View.GONE);
		}

        return layout;
	}

	private class ChildViewHolder {
		public TextView text;
		public ImageView divider;

		public ChildViewHolder(LinearLayout layout) {
			text = (TextView)layout.findViewById(R.id.txt_listItem);
			divider = (ImageView)layout.findViewById(R.id.img_listItemDivider);
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
