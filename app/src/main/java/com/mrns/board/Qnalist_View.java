package com.mrns.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrns.main.R;
public class Qnalist_View extends LinearLayout {


    /**
     * QnA 제목
     */
    private TextView qnaTitle;

    /**
     * 댓글 카운트
     */
    private TextView qnaCount;

    /**
     * QnA 작성자
     */
    private TextView qnaName;

    /**
     * 날짜
     */
    private TextView qnaDate;

    public Qnalist_View(Context context, Qnalist_Item qItem) {
        super(context);

        //Layout Inflation
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_qnalist_list, this, true);

        //Set Text

        qnaTitle = (TextView) findViewById(R.id.qna_title);
        qnaTitle.setText(qItem.getData(0));

        qnaName = (TextView) findViewById(R.id.qna_name);
        qnaName.setText(qItem.getData(1));

        qnaDate = (TextView) findViewById(R.id.qna_date);
        qnaDate.setText(qItem.getData(2));

        qnaCount = (TextView) findViewById(R.id.qna_count);
        qnaCount.setText(String.valueOf(qItem.getAnsCount()));
    }



    /**
     * set Text
     */
    public void setText(int index, String data) {
        if(index == 1) {
            qnaTitle.setText(data);
        }else if(index == 2) {
            qnaName.setText(data);
        }else if(index == 3) {
            qnaDate.setText(data);
        }
    }

    public void setQnaCount(int cnt) {
        qnaCount.setText(String.valueOf(cnt));
    }

}
