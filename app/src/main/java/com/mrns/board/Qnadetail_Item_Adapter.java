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
public class Qnadetail_Item_Adapter extends BaseAdapter {

    private Context qContext;

    private List<Qnadetail_Item> qItems = new ArrayList<Qnadetail_Item>();

    public Qnadetail_Item_Adapter(Context context) { qContext = context; }

    public void addItem(Qnadetail_Item Qit) { qItems.add(Qit); }

    public void setListItems(List<Qnadetail_Item> Qit) {qItems = Qit; }

    public int getCount() { return qItems.size(); }

    public Object getItem(int position) { return qItems.get(position); }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try {
            return qItems.get(position).ispSelectable();
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        Qnadetail_View itemView;
        if(convertView == null) {
            itemView = new Qnadetail_View(qContext, qItems.get(position));
        } else {
            itemView = (Qnadetail_View) convertView;

            itemView.setText(1, qItems.get(position).getData(0));
            itemView.setText(2, qItems.get(position).getData(1));
            itemView.setText(3, qItems.get(position).getData(2));
        }

        return itemView;
    }

}
