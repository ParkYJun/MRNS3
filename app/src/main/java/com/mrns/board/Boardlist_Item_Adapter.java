package com.mrns.board;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geopeople on 2016-01-19.
 */
public class Boardlist_Item_Adapter extends BaseAdapter {

    private Context bContext;

    private List<Boardlist_Item> bItems = new ArrayList<Boardlist_Item>();

    public Boardlist_Item_Adapter(Context context) {bContext = context; }

    public void addItem(Boardlist_Item Bit) { bItems.add(Bit); }

    public void setListItems(List<Boardlist_Item> Bit) { bItems = Bit; }

    public int getCount() { return bItems.size(); }

    public Object getItem(int position) { return bItems.get(position); }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try {
            return bItems.get(position).isbSelectable();
        }catch (IndexOutOfBoundsException ex) {
            return false;
        }
    }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        Boardlist_View itemView;
        if(convertView == null) {
            itemView = new Boardlist_View(bContext, bItems.get(position));
        } else {
            itemView = (Boardlist_View) convertView;

            itemView.setText(1, bItems.get(position).getData(0));
            itemView.setText(2, bItems.get(position).getData(1));
            itemView.setIcon(bItems.get(position).getIcon());


        }

        return itemView;
    }
}
