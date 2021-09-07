package com.mrns.invest.innerDetail.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by eined on 2016. 1. 25..
 */
public class HistoryList_View extends LinearLayout {

    //순번
    private TextView sunbun;
    //조사일자
    private TextView joDate;
    //조사자
    private TextView joPerson;

    //조사기관
    private TextView joOrg;

    public HistoryList_View(Context context, HistoryList_Item aItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.activity_history_result_list, this, true);


        sunbun = (TextView) findViewById(R.id.pNum);
        sunbun.setText(aItem.getData(0));

        joDate = (TextView) findViewById(R.id.pDate);
        joDate.setText(aItem.getData(1));

        joPerson = (TextView) findViewById(R.id.pPerson);
        joPerson.setText(aItem.getData(2));

        joOrg = (TextView) findViewById(R.id.pOrg);
        joOrg.setText(aItem.getData(3));

    }

    public void setText(int index, String data) {
        if (index == 0) {
            sunbun.setText(data);
        } else if (index == 1) {
            joDate.setText(data);
        } else if (index == 2) {
            joPerson.setText(data);
        } else if (index == 3) {
            joOrg.setText(data);
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}

