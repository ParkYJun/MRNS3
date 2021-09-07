package com.mrns.board;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geopeople on 2016-01-20.
 */
public class Qnalist_Item_Adapter extends BaseAdapter {

    private Context qContext;

    private List<Qnalist_Item> qItems = new ArrayList<Qnalist_Item>();

    public Qnalist_Item_Adapter(Context context) { qContext = context; }

    public void addItem(Qnalist_Item Qit) { qItems.add(Qit); }

    public void setListItems(List<Qnalist_Item> Qit) {qItems = Qit; }

    public int getCount() { return qItems.size(); }

    public Object getItem(int position) { return qItems.get(position); }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try{
            return qItems.get(position).ispSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        Qnalist_View itemView;
        if(convertView == null) {
            itemView = new Qnalist_View(qContext, qItems.get(position));
        } else {
            itemView = (Qnalist_View) convertView;

            itemView.setText(1, qItems.get(position).getData(0));
            itemView.setText(2, qItems.get(position).getData(1));
            itemView.setText(3, qItems.get(position).getData(2));
            itemView.setQnaCount(qItems.get(position).getAnsCount());
        }

        return itemView;
    }


}
