package ru.homecatering;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

class LeftMenuAdapter extends BaseExpandableListAdapter {
    private List<LeftMenuModel> lmHeader;
    private Map<LeftMenuModel, List<LeftMenuModel>> lmChildren;

    LeftMenuAdapter(Activity activity) {
        LeftMenuJSONParser leftMenuData = new LeftMenuJSONParser(activity);
        lmHeader = leftMenuData.getMenuHeader();
        lmChildren = leftMenuData.getMenuChildren();
    }

    @Override
    public int getGroupCount() {
        return this.lmHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<LeftMenuModel> lmc = this.lmChildren.get(getGroup(groupPosition));
        return lmc == null ? 0 : lmc.size();
    }

    @Override
    public LeftMenuModel getGroup(int groupPosition) {
        return lmHeader.get(groupPosition);
    }

    @Override
    public LeftMenuModel getChild(int groupPosition, int childPosition) {
        return lmChildren.get(getGroup(groupPosition)).get(childPosition);
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
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LeftMenuModel lmm = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.left_menu_item, null);
        }

        convertView.setTag(lmm.getId());
        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageDrawable(lmm.getIcon());
        icon.setContentDescription(lmm.getText());
        TextView text = convertView.findViewById(R.id.text);
        text.setText(lmm.getText());
        if (lmm.hasChildren) {
            Log.i("INFO",lmm.getText()+lmm.hasChildren+isExpanded);
            if (isExpanded) {
                convertView.findViewById(R.id.more).setVisibility(View.INVISIBLE);
                convertView.findViewById(R.id.less).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.more).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.less).setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LeftMenuModel lmm = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.left_menu_item_inner, null);
        }

        convertView.setTag(lmm.getId());
        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageDrawable(lmm.getIcon());
        icon.setContentDescription(lmm.getText());
        TextView text = convertView.findViewById(R.id.text);
        text.setText(lmm.getText());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
