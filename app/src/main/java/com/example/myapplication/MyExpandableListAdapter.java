package com.example.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<CoinTO> coinTOList;

    public MyExpandableListAdapter(Context context, List<CoinTO> coinTOList) {
        this.context = context;
        this.coinTOList = coinTOList;


    }


    @Override
    public int getGroupCount() {
        return coinTOList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 3;
    }

    @Override
    public Object getGroup(int i) {
        return coinTOList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return coinTOList.get(i);
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
        CoinTO coin = coinTOList.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coin_item, null);
        }
        TextView item = view.findViewById(R.id.coin);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(coin.getName() + "  -  " + coin.getPrice());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        System.out.println(i1);
        CoinTO coin = coinTOList.get(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.coin_details, null);
        }

        TextView item = view.findViewById(R.id.details);

        if (i1 == 0)
            item.setText("Id : " + coin.getId());
        if (i1 == 1)
            item.setText("Last Updated : " + coin.getLast_updated());
        if (i1 == 2)
            item.setText("Symbol : " + coin.getSymbol());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {

        return true;
    }
}
