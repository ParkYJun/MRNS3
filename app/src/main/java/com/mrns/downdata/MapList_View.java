package com.mrns.downdata;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 현황자료 내려받기 - 지도자료 리스트
 * 아이템으로 보여줄 뷰 정의
 * Created by geopeople08 on 2016-01-08.
 */
public class MapList_View extends LinearLayout  {

    /**
     * 선택 아이콘
     */
    private ImageView rSelectIcon;

    /**
     * 지도자료 제목
     */
    private TextView mTitle;

    /**
     * 작성일
     */
    private TextView mDate;

    /**
     * 작성자
     */
    private TextView mName;

    public MapList_View(Context context, MapList_Item mItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(com.mrns.main.R.layout.activity_down_tab_map_list, this, true);

        //Set Icon
        rSelectIcon = (ImageView) findViewById(com.mrns.main.R.id.r_mapSelect);
        rSelectIcon.setImageDrawable(mItem.getIcon());

        mTitle = (TextView) findViewById(com.mrns.main.R.id.map_title);
        mTitle.setText(mItem.getData(0));

        mDate = (TextView) findViewById(com.mrns.main.R.id.map_date);
        mDate.setText(mItem.getData(1));

    }

    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1) {
            mTitle.setText(data);
        }else if(index == 2 ) {
            mDate.setText(data);
        }else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * set Icon
     */
    public void setIcon(Drawable icon)  {rSelectIcon.setImageDrawable(icon); }
}
