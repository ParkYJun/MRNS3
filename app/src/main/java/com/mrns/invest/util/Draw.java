package com.mrns.invest.util;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by luclipse on 2016-03-01.
 */
public class Draw {
    private Path mPath;
    private Paint mPaint;

    public Draw(Path mPath, Paint mPaint) {
        this.mPath = mPath;
        this.mPaint = mPaint;
    }

    public Path getmPath() {
        return mPath;
    }

    public void setmPath(Path mPath) {
        this.mPath = mPath;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }
}
