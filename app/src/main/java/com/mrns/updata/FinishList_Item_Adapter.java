package com.mrns.updata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geopeople08 on 2016-01-12.
 */
public class FinishList_Item_Adapter extends BaseAdapter {

    private Context mContext;

    private List<FinishList_Item> mItems = new ArrayList<FinishList_Item>();

    public FinishList_Item_Adapter(Context context) {mContext = context; }

    public void addItem(FinishList_Item Fit) { mItems.add(Fit); }

    public void setListItems(List<FinishList_Item> Fit) { mItems = Fit; }

    public int getCount() { return mItems.size(); }

    public Object getItem(int position) { return mItems.get(position); }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try{
            return mItems.get(position).ispSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) { return position; }
    public View getView(int position, View convertView, ViewGroup parent) {
        FinishList_View itemView;
        if(convertView == null) {
            itemView = new FinishList_View(mContext, mItems.get(position));
        } else {
            itemView = (FinishList_View) convertView;

            itemView.setIcon1(mItems.get(position).getIcon1());
//            itemView.setIcon2(mItems.get(position).getIcon2());
            itemView.setText(1, mItems.get(position).getData(0));
            itemView.setText(2, mItems.get(position).getData(1));
            itemView.setText(3, mItems.get(position).getData(2));
            itemView.setText(4, mItems.get(position).getData(3));
        }

        return itemView;
    }

}
