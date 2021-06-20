package com.example.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<String> coinList;
    Map<String, List<String>> collectionList;

    public MyExpandableListAdapter(Context context, List<String> coinList, Map<String, List<String>> collectionList) {
        this.context = context;
        this.coinList = coinList;
        this.collectionList = collectionList;


    }


    @Override
    public int getGroupCount() {
        return collectionList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return collectionList.get(coinList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return coinList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return collectionList.get(coinList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {

        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String coinName = getGroup(i).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coin_item, null);
        }
        TextView item = view.findViewById(R.id.coin);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(coinName);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String info = getChild(i, i1).toString();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coin_details, null);
        }

        TextView item = view.findViewById(R.id.details);
        item.setText(info);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return true;
    }
}
