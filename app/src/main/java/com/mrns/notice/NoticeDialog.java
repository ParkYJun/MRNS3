package com.mrns.notice;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mrns.main.R;

public class NoticeDialog extends Dialog {
    public NoticeDialog(@NonNull Context context, NoticeRecord noticeRecord) {
        super(context);
        setContentView(R.layout.layout_notice_dialog);
        init(noticeRecord);
    }

    private void init(final NoticeRecord noticeRecord) {
        ImageButton imagebtnClose = (ImageButton)findViewById(R.id.imagebtn_close);
        TextView textviewTitle = (TextView)findViewById(R.id.textview_title);
        TextView textviewRegistDate = (TextView)findViewById(R.id.textview_regist_date);
        TextView textviewRegistUserName = (TextView)findViewById(R.id.textview_regist_user_name);
        TextView textviewContents =  (TextView)findViewById(R.id.textview_contents);
        TextView textviewCloseForever = (TextView)findViewById(R.id.textview_close_forever);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.textview_close_forever :
                        addExcludeNotice(noticeRecord.getMobileNoticeSn());

                    case R.id.imagebtn_close :
                        dismiss();
                        break;
                }
            }
        };

        imagebtnClose.setOnClickListener(onClickListener);
        textviewCloseForever.setOnClickListener(onClickListener);

        textviewTitle.setText(noticeRecord.getNoticeTitle());
        textviewRegistDate.setText(noticeRecord.getRegistDate());
        textviewRegistUserName.setText(noticeRecord.getRegistUserName());
        textviewContents.setText(noticeRecord.getNoticeContents());
    }

    private void addExcludeNotice(int mobileNoticeSn) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.key_shared_preferences), Context.MODE_PRIVATE);
        String keyExcludeNotice = getContext().getString(R.string.key_exclude_notice);
        String strExcludeList = sharedPreferences.getString(keyExcludeNotice, getContext().getString(R.string.empty));

        if (!strExcludeList.equals(getContext().getString(R.string.empty))) {
            strExcludeList += getContext().getString(R.string.seperator);
        }

        strExcludeList += mobileNoticeSn;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyExcludeNotice, strExcludeList);
        editor.apply();
    }

}
