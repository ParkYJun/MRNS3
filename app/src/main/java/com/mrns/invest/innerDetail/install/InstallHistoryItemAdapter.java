package com.mrns.invest.innerDetail.install;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eined on 2016. 1. 25..
 */
public class InstallHistoryItemAdapter extends BaseAdapter {
    private Context mContext;

    private List<InstallHistoryList_Item> mItems = new ArrayList<InstallHistoryList_Item>();

    public InstallHistoryItemAdapter(Context context) {
        mContext = context;
    }

    public void addItem(InstallHistoryList_Item it) {
        mItems.add(it);
    }

    public void setListItems(List<InstallHistoryList_Item> lit) {
        mItems = lit;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public boolean areAllItemsSelectable() {
        return false;
    }

    public boolean isSelectable(int position) {
        try {
            return mItems.get(position).isSelectable();
        } catch (IndexOutOfBoundsException ex){
            return false;
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        InstallHistoryList_View itemView;
        if(convertView == null) {
            itemView = new InstallHistoryList_View(mContext, mItems.get(position));
        } else {
            itemView = (InstallHistoryList_View) convertView;

            itemView.setText(0, mItems.get(position).getData(0));
            itemView.setText(1, mItems.get(position).getData(1));
            itemView.setText(2, mItems.get(position).getData(2));
            itemView.setText(3, mItems.get(position).getData(3));

        }

        return itemView;
    }
}
