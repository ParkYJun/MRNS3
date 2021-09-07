package com.mrns.invest.innerDetail.install;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by eined on 2016. 1. 25..
 */
public class InstallHistoryList_View extends LinearLayout {

    //순번
    private TextView sunbun;
    //설치구분
    private TextView instFlag;
    //설치일자
    private TextView instDate;
    //설치자
    private TextView instPerson;

    public InstallHistoryList_View(Context context, InstallHistoryList_Item aItem) {
        super(context);

        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.activity_install_history_result_list,this,true);

        sunbun = (TextView) findViewById(R.id.pNum);
        sunbun.setText(aItem.getData(0));

        instFlag = (TextView) findViewById(R.id.pInstFlag);
        instFlag.setText(aItem.getData(1));

        instDate = (TextView) findViewById(R.id.pInstDate);
        instDate.setText(aItem.getData(2));

        instPerson = (TextView) findViewById(R.id.pInstPerson);
        instPerson.setText(aItem.getData(3));
    }

    public void setText(int index, String data) {
        if (index == 0) {
            sunbun.setText(data);
        } else if (index == 1) {
            instFlag.setText(data);
        } else if (index == 2) {
            instDate.setText(data);
        } else if (index == 3) {
            instPerson.setText(data);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
