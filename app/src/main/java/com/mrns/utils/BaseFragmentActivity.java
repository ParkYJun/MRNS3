package com.mrns.utils;

import android.graphics.Typeface;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by geopeople on 2016-01-26.
 */
public class BaseFragmentActivity extends FragmentActivity {

    private static Typeface mTypeface;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (BaseFragmentActivity.mTypeface == null)
            BaseFragmentActivity.mTypeface = Typeface.createFromAsset(getAssets(), "font/NotoSansCJKkr-Regular.otf");

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
