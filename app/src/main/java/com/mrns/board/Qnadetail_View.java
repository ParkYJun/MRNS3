package com.mrns.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;

/**
 * Created by geopeople on 2016-01-20.
 */
public class Qnadetail_View extends LinearLayout {

    /**
     * QnA 답변
     */
    private TextView qnaAnswer;

    /**
     * QnA 답변일시분
     */
    private TextView qnaAnsDate;

    /**
     * QnA 작성자
     */
    private TextView qnaAnsName;

    public Qnadetail_View(Context context, Qnadetail_Item qdItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_qnadetail_list, this, true);

        //Set Text
        qnaAnswer = (TextView) findViewById(R.id.qnadetail_a_content);
        qnaAnswer.setText(qdItem.getData(0));

        qnaAnsDate = (TextView) findViewById(R.id.qnadetail_a_date);
        qnaAnsDate.setText(qdItem.getData(1));

        qnaAnsName = (TextView) findViewById(R.id.qnadetail_a_name);
        qnaAnsName.setText(qdItem.getData(2));
    }


    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1) {
            qnaAnswer.setText(data);
        }else if(index == 2) {
            qnaAnsDate.setText(data);
        }else if(index == 3) {
            qnaAnsName.setText(data);
        }
    }

}
