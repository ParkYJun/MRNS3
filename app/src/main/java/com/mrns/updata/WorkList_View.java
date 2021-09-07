package com.mrns.updata;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by geopeople08 on 2016-01-11.
 */
public class WorkList_View extends LinearLayout{

    /**
     * 점의종류 아이콘
     */
    private ImageView pKindIcon;

    /**
     * 점의명칭
     */
    private TextView pTitle;

    /**
     * 점의상태
     */
    private TextView pStatus;

    /**
     * 조사일
     */
    private TextView pDate;

    /**
     * 조사상태
     */
    private TextView invest_status;

    /**
     * 점의위치 아이콘
     */
    private ImageView pLocaIcon;

    public WorkList_View(Context context, WorkList_Item wItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_upload_tab_work_list, this, true);

        //Set Icon
        pKindIcon = (ImageView) findViewById(R.id.point_kind);
        pKindIcon.setImageDrawable(wItem.getIcon1());

//        pLocaIcon = (ImageView) findViewById(R.id.point_location);
//        pLocaIcon.setImageDrawable(wItem.getIcon2());

        //Set Text
        pTitle = (TextView) findViewById(R.id.point_title);
        pTitle.setText(wItem.getData(0));

        pStatus = (TextView) findViewById(R.id.point_status);
        pStatus.setText(wItem.getData(1));

        pDate = (TextView) findViewById(R.id.invest_date);
        pDate.setText(wItem.getData(2));

        invest_status = (TextView) findViewById(R.id.invest_status);
        invest_status.setText(wItem.getData(3));

    }

    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1){
            pTitle.setText(data);
        }else if(index == 2) {
            pStatus.setText(data);
        }else if(index == 3) {
            pDate.setText(data);
        }else if(index == 4) {
            invest_status.setText(data);
        }
    }

    /**
     * set Icon
     */
    public void setIcon1(Drawable icon1) {
        pKindIcon.setImageDrawable(icon1);
    }

//    public void setIcon2(Drawable icon2) {
//        pLocaIcon.setImageDrawable(icon2);
//    }

}
