package com.mrns.board;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by geopeople on 2016-01-19.
 */
public class Boardlist_View extends LinearLayout {

    private ImageView bIcon;

    /**
     * 공지사항 제목
     */
    private TextView bTitle;

    /**
     * 공지일자
     */
    private TextView bDate;

    public Boardlist_View(Context context, Boardlist_Item bItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_boardlist_list, this, true);

        //set Icon
        bIcon = (ImageView) findViewById(R.id.board_icon);
        bIcon.setImageDrawable(bItem.getIcon());

        //Set text
        bTitle = (TextView) findViewById(R.id.board_title);
        bTitle.setText(bItem.getData(0));

        bDate = (TextView) findViewById(R.id.board_date);
        bDate.setText(bItem.getData(1));
    }

    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1){
            bTitle.setText(data);
        }else if(index == 2) {
            bDate.setText(data);
        }
    }

    public void setIcon(Drawable icon)  {bIcon.setImageDrawable(icon); }


}
