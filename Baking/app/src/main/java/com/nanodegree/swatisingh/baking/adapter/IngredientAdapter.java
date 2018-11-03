package com.nanodegree.swatisingh.baking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nanodegree.swatisingh.baking.R;

import java.util.ArrayList;

public class IngredientAdapter extends BaseExpandableListAdapter{
    private Context context;
    private String expandableListTitle;
    private ArrayList<String> expandableListDetail;


    public IngredientAdapter (Context context, String expandableListTitle, ArrayList<String> expandableListDetail){
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPos, int expandedListPos) {
        return this.expandableListDetail.get(expandedListPos);
    }

    @Override
    public long getChildId(int listPos, int expandedListPos) {
        return expandedListPos;
    }

    @Override
    public View getChildView(int listPos, final int expandedListpos, boolean isRecentChild, View chView, ViewGroup viewGroup) {
        final String expandedListText = (String) getChild(listPos, expandedListpos);
        if (chView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            chView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView expandedListTextView = (TextView) chView.findViewById(R.id.ingredient_item);
        expandedListTextView.setText(expandedListText);
        return chView;
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListDetail.size();
    }

    @Override
    public Object getGroup(int i) {
        return this.expandableListTitle;
    }


    @Override
    public int getGroupCount() {
        return 1;
    }
    @Override
    public long getGroupId(int i) {
        return i;
    }
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(i);

        if (view == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView expandedListTitleTextView = (TextView) view.findViewById(R.id.list_title);
        expandedListTitleTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        expandedListTitleTextView.setText(listTitle);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
