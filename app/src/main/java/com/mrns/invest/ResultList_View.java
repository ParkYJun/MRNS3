package com.mrns.invest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by geopeople08 on 2015-12-30.
 * 기준점 검색결과 리스트 아이템으로 보여줄 뷰 정의
 */
public class ResultList_View extends LinearLayout {

    /**
     * 기준점 종류
     */
    private ImageView pKindIcon;

    /**
     * 점의 명칭
     */
    private TextView pName;

    /**
     * 점의 상태
     */
    private TextView pStatus;

    /**
     * 점의위치
     */
    private ImageView pLocaIcon;

    /**
     * 조사일
     */
    private TextView iDate;

    private TextView iState;

    public ResultList_View(Context context, ResultList_Item aItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.activity_invest_result_list, this, true);

        //Set Icon
        pKindIcon = (ImageView) findViewById(R.id.pKind);
        pKindIcon.setImageDrawable(aItem.getIcon1());

        pName = (TextView) findViewById(R.id.pName);
        pName.setText(aItem.getData(0));

        pStatus = (TextView) findViewById(R.id.pStatus);
        pStatus.setText(aItem.getData(1));

        iDate = (TextView) findViewById(R.id.iDate);
        iDate.setText(aItem.getData(2));

        iState = (TextView) findViewById(R.id.iState);
        iState.setText("조사전");

        pLocaIcon = (ImageView) findViewById(R.id.pLocation);
        pLocaIcon.setImageDrawable(aItem.getIcon2());


    }

    /**
     * set Text
     *
     * @param index
     * @param data
     */
    public void setText(int index, String data) {
        if (index == 1) {
            pName.setText(data);
        } else if (index == 2) {
            pStatus.setText(data);
        } else if (index == 3) {
            iDate.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }


    /**
     * set Icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        pKindIcon.setImageDrawable(icon);
    }

    public void setIcon2(Drawable icon1) {
        pLocaIcon.setImageDrawable(icon1);
    }
}
