package im.zico.wingtwitter.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import im.zico.wingtwitter.R;

/**
 * Created by tinyao on 12/1/14.
 */

public class DrawerListAdapter extends BaseExpandableListAdapter {
    private Context _context;

    private List<String> _listDataHeader; // header titles
    private TypedArray _listDataHeaderIcons;
    private HashMap<String, List<String>> _listDataChild;

    public DrawerListAdapter(Context context, List<String> listDataHeader,
                             TypedArray listDataHeaderIcons,
                             HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataHeaderIcons = listDataHeaderIcons;
        this._listDataChild = listChildData;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_drawer_child, null);
        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_drawer_child_item);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }



    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_drawer_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_drawer_group_label);
        ImageView lblListHeaderIcon = (ImageView) convertView.findViewById(R.id.img_drawer_group_tag);
        lblListHeader.setText(headerTitle);
        lblListHeaderIcon.setSelected(true);
        lblListHeaderIcon.setImageResource(_listDataHeaderIcons
                .getResourceId(groupPosition, R.drawable.ic_drawer_home));

        CheckBox groupIndicator = (CheckBox) convertView.findViewById(R.id.check_drawer_group);
        if (groupPosition == 3 || groupPosition == 4) {
            groupIndicator.setVisibility(View.VISIBLE);
        } else {
            groupIndicator.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}