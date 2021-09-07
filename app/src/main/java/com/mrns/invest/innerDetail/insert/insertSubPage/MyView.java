package com.mrns.invest.innerDetail.insert.insertSubPage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by eined on 2016. 2. 1..
 */
public class MyView extends View {
    static final int RED_STATE=0;
    static final int BLUE_STATE=1;
    static final int YELLOW_STATE=2;

    int colorState = RED_STATE;

    ArrayList<Point> list;
    Paint[] paintList = new Paint[3];

    Paint paint = new Paint();
    Path path = new Path();    // 자취를 저장할 객체
    public MyView(Context context) {
//            super(context);
        super(context);
        init();
//            paint.setStyle(Paint.Style.STROKE); // 선이 그려지도록
//            paint.setStrokeWidth(10f); // 선의 굵기 지정
//            paint.setColor(Color.RED);
//            paint.setAntiAlias(true);
    }

    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    public void init(){

        list=new ArrayList<Point>();

        Paint  redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(15);
        redPaint.setAntiAlias(true);

        Paint  bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(5);
        bluePaint.setAntiAlias(true);

        Paint  yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setStrokeWidth(5);
        yellowPaint.setAntiAlias(true);

        paintList[0]=redPaint;
        paintList[1]=bluePaint;
        paintList[2]=yellowPaint;

    }

    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 메서드
//            canvas.drawPath(path, paint); // 저장된 path 를 그려라
//        canvas.drawColor(Color.WHITE);

        for(int i=0 ;i < list.size(); i++){

            Point p = list.get(i);
            if(!(p.isStart)){

                canvas.drawLine(list.get(i-1).x,
                        list.get(i-1).y,
                        list.get(i).x,
                        list.get(i).y,
                        paintList[list.get(i).colorState]);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventX=(int)event.getX();
        int eventY=(int)event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN :

                Point p = new Point(eventX, eventY, true, colorState);

                list.add(p);
                break;
            case MotionEvent.ACTION_MOVE :

                Point p2 = new Point(eventX, eventY, false, colorState);
                list.add(p2);

                invalidate();
                break;
        }


        return true;

    }

    public class Point implements Serializable {


        int x, y;
        boolean isStart=false;
        int colorState;

        public Point(int x, int y, boolean isStart){
            this.x = x;
            this.y = y;
            this.isStart = isStart;
        }
        public Point(int x, int y, boolean isStart, int colorState){
            this.x = x;
            this.y = y;
            this.isStart = isStart;
            this.colorState = colorState;
        }

    }
}
