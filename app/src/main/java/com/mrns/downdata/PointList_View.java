package com.mrns.downdata;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * 현황자료 내려받기 - 현황 조사자료 리스트
 * 아이템으로 보여줄 뷰 정의
 * Created by geopeople08 on 2016-01-07.
 */
public class PointList_View extends LinearLayout {

    /**
     * 선택 아이콘
     */
    private ImageView rSelectIcon;

    /**
     * 현황조사 제목
     */
    private TextView pTitle;

    /**
     * 작성일
     */
    private TextView pDate;

    /**
     * 작성자
     */
    private TextView pName;

    public PointList_View(Context context, PointList_Item pItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_down_tab_point_list, this, true);

        //Set Icon
        rSelectIcon = (ImageView) findViewById(R.id.r_pointSelect);
        rSelectIcon.setImageDrawable(pItem.getIcon());

        pTitle = (TextView) findViewById(R.id.point_title);
        pTitle.setText(pItem.getData(0));

        pDate = (TextView) findViewById(R.id.point_date);
        pDate.setText(pItem.getData(1));

    }

    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1) {
            pTitle.setText(data);
        }else if(index == 2 ) {
            pDate.setText(data);
        }else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * set Icon
     */
    public void setIcon(Drawable icon)  {rSelectIcon.setImageDrawable(icon); }
}
