package com.mrns.invest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geopeople08 on 2015-12-31.
 * 기준검 검색 결과 리스트 어댑터 클래스 정의
 */
public class ResultList_Item_Adapter extends BaseAdapter{
    
    private Context mContext;

    private List<ResultList_Item> mItems = new ArrayList<ResultList_Item>();

    public ResultList_Item_Adapter(Context context) {
        mContext = context;
    }

    public void addItem(ResultList_Item it) {
        mItems.add(it);
    }

    public void setListItems(List<ResultList_Item> lit) {
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
        ResultList_View itemView;
        if(convertView == null) {
            itemView = new ResultList_View(mContext, mItems.get(position));
        } else {
            itemView = (ResultList_View) convertView;

            itemView.setIcon(mItems.get(position).getIcon1());
            itemView.setText(1, mItems.get(position).getData(0));
            itemView.setText(2, mItems.get(position).getData(1));
            itemView.setText(3, mItems.get(position).getData(2));
//            itemView.setText(4, mItems.get(position).getData(3));
            itemView.setIcon2(mItems.get(position).getIcon2());

//            itemView.setIcon(mItems.get(position).getIcon1());
//            itemView.setIcon2(mItems.get(position).getIcon2());
//            itemView.setText(1, mItems.get(position).getData(0));
//            itemView.setText(2, mItems.get(position).getData(1));
//            itemView.setText(3, mItems.get(position).getData(2));

        }

        return itemView;
    }
}
