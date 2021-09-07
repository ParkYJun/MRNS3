package com.mrns.invest.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;


public class FineDialog {
    protected static ProgressDialog mDialog = null;

    public static void showProgress(Context context, boolean cancelable, String message) {
        if(mDialog != null && mDialog.isShowing()) {
            hideProgress();
        }
        mDialog = new ProgressDialog(context);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(message);
        mDialog.setCancelable(cancelable);
        mDialog.show();
    }
    public static void hideProgress() {
        if(mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
    public static void AlertDialog(Context context, String title, String message) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setMessage(message)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
}
