package com.mrns.utils;

import android.graphics.Typeface;
//import android.support.v7.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by geopeople08 on 2016-01-07.
 */
public class BaseTabActivity extends AppCompatActivity {

    private static Typeface mTypeface;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (BaseTabActivity.mTypeface == null)
            BaseTabActivity.mTypeface = Typeface.createFromAsset(getAssets(), "font/NotoSansCJKkr-Regular.otf");

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);
    }


    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView)child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }


}
