package com.mrns.updata;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 현황조사 업로드 - 올리기
 * 현황자료 업로드 리스트 클래스 정의
 * Created by geopeople08 on 2016-01-12.
 */
public class WorkList_Item_Adapter extends BaseAdapter {

    private Context mContext;

    private List<WorkList_Item> mItems = new ArrayList<>();

    public WorkList_Item_Adapter(Context context) { mContext = context; }

    public void addItem(WorkList_Item Wit) {
        mItems.add(Wit);
    }

    public void setListItems(List<WorkList_Item> Wit) { mItems = Wit; }

    public int getCount() { return mItems.size(); }

    public Object getItem(int position) { return mItems.get(position); }

    public boolean areAllItemsSelectable() { return false; }

    public boolean isSelectable(int position) {
        try{
            return mItems.get(position).ispSelectable();
        } catch (IndexOutOfBoundsException ex) {
            Log.e(mContext.getString(com.mrns.main.R.string.indexOutOfBoundsException), ex.getMessage());
            return false;
        } catch (Exception e){
            Log.e(mContext.getString(com.mrns.main.R.string.exception), e.getMessage());
            return false;
        }
    }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        WorkList_View itemView;
        if(convertView == null) {
            itemView = new WorkList_View(mContext, mItems.get(position));
        } else {
            itemView = (WorkList_View) convertView;

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
