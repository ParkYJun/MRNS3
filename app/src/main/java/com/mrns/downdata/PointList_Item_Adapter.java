package com.mrns.downdata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 현황조사 다운로드 - 현황조사자료
 * 현황자료 리스트 어댑터 클래스 정의
 * Created by geopeople08 on 2016-01-07.
 */
public class PointList_Item_Adapter extends BaseAdapter {

    private Context mContext;

    private List<PointList_Item> mItems = new ArrayList<PointList_Item>();

    public PointList_Item_Adapter(Context context) {
        mContext = context;
    }

    public void addItem(PointList_Item it) {
        mItems.add(it);
    }

    public void setListItems(List<PointList_Item> lit) { mItems = lit; }

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
        PointList_View itemView;
        if(convertView == null) {
            itemView = new PointList_View(mContext, mItems.get(position));
        } else {
            itemView = (PointList_View) convertView;

            itemView.setIcon(mItems.get(position).getIcon());
            itemView.setText(1, mItems.get(position).getData(0));
            itemView.setText(2, mItems.get(position).getData(1));
        }

        return itemView;
    }

}
