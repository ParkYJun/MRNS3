package com.mrns.common;

/**
 * Created by dongjunpark on 16. 8. 11..
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrns.main.R;


/**
 * Created by SangMin YI on 2015-11-27.
 * 버튼이 두개인 디이얼로그
 */
public class MsgDialog extends Dialog implements View.OnClickListener {

    public static final String Default_Positive_Button_Text = "확인";
    public static final String Default_Negative_Button_Text = "취소";

    private Context mContext = null;

    private OnClickListener mPositiveButtonListener = null;
    private OnClickListener mNegativeButtonListener = null;

    private TextView txtTitle;
    private TextView txtMessage;
    private RelativeLayout btnOk;
    private TextView txtOk;
    private RelativeLayout btnCancel;
    private TextView txtCancel;

    public void setTitle(CharSequence title) {
        if (title != null && title.length() > 0) {
            txtTitle.setText(title);
            txtTitle.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setVisibility(View.GONE);
        }
    }

    public void setMessage(int messageId) {
        setMessage(mContext.getText(messageId));
    }

    public void setMessage(CharSequence message) {
        txtMessage.setText(message);
    }

    public void setPositiveButtonText(int textId) {
        setPositiveButtonText(mContext.getText(textId));
    }

    public void setPositiveButtonText(CharSequence text) {
        txtOk.setText(text);
    }

    public void setNegativeButtonText(int textId) {
        setNegativeButtonText(mContext.getText(textId));
    }

    public void setNegativeButtonText(CharSequence text) {
        txtCancel.setText(text);
    }

    public void setPositiveButton(final OnClickListener listener) {
        setPositiveButton(Default_Positive_Button_Text, listener);
    }

    public void setPositiveButton(int textId, final OnClickListener listener) {
        setPositiveButton(mContext.getText(textId), listener);
    }

    public void setPositiveButton(CharSequence text, final OnClickListener listener) {
        setPositiveButtonText(text);
        mPositiveButtonListener = listener;
    }

    public void setNegativeButton(final OnClickListener listener) {
        setNegativeButton(Default_Negative_Button_Text, listener);
    }

    public void setNegativeButton(int textId, final OnClickListener listener) {
        setNegativeButton(mContext.getText(textId), listener);
    }

    public void setNegativeButton(CharSequence text, final OnClickListener listener) {
        setNegativeButtonText(text);
        mNegativeButtonListener = listener;
    }

    public MsgDialog(Context context) {
        super(context);

        mContext = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_alert2);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtMessage = (TextView) findViewById(R.id.txt_message);
        btnOk = (RelativeLayout) findViewById(R.id.btn_ok);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        btnCancel = (RelativeLayout) findViewById(R.id.btn_cancel);
        txtCancel = (TextView) findViewById(R.id.txt_cancel);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setPositiveButton(null);
        setNegativeButton(null);

        setMessage("");
    }

    @Override
    public void onClick(View v) {
        if (v == btnOk) {
            if (mPositiveButtonListener != null) {
                mPositiveButtonListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
            }

            dismiss();
        } else if (v == btnCancel) {
            if (mNegativeButtonListener != null) {
                mNegativeButtonListener.onClick(this, DialogInterface.BUTTON_NEGATIVE);
            }

            dismiss();
        }
    }
}
