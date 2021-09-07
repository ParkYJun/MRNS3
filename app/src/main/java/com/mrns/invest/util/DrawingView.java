package com.mrns.invest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.almeros.android.multitouch.MoveGestureDetector;
import com.almeros.android.multitouch.RotateGestureDetector;
import com.mrns.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import team.uptech.motionviews.viewmodel.Layer;
import team.uptech.motionviews.widget.entity.MotionEntity;
import team.uptech.motionviews.widget.entity.PathEntity;

public class DrawingView extends View {

    private static final float TOUCH_TOLERANCE = 4;

    private Canvas mCanvas;
    private Path mPath = new Path();
    private Paint mPaint;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private Canvas mImageCanvas;
    private Paint mImageBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Bitmap mImageBitmap;

    private final List<MotionEntity> mEntities = new ArrayList<>();
    private final List<MotionEntity> mUnEntities = new ArrayList<>();

    private Bitmap mBitmap;

    private static final int LINE_TYPE_FREE = 0;
    private static final int LINE_TYPE_STRAIGHT = 1;
    private static final int LINE_TYPE_ARROW = 2;

    private final static int MODE_USE_PEN = 0;
    private final static int MODE_ADD_TEXT = 1;
    private final static int MODE_ADD_ICON = 2;
    private final static int MODE_USE_ERASE = 3;

    private int mPenColor = Color.RED;
    private int mPenWidth = 12;
    private int mPenType = LINE_TYPE_FREE;
    private int mDrawingMode = MODE_USE_PEN;
    private DashPathEffect mDashPathEffect = null;
    private MotionEntity mSelectedEntity;
    private Paint mSelectedLayerPaint;
    private int mWidth;
    private int mHeight;
    private float mX, mY;
    private boolean mTouched;


    //! 기타 MotionView 관련 선언부

    public interface Constants {
        float SELECTED_LAYER_ALPHA = 0.15F;
    }
    public interface DrawingViewCallback {
        void onEntitySelected(@Nullable MotionEntity entity);
        void onEntityDoubleTap(@NonNull MotionEntity entity);
    }
    private DrawingViewCallback mDrawingViewCallback;
    private ScaleGestureDetector mScaleGestureDetector;
    private RotateGestureDetector mRotateGestureDetector;
    private MoveGestureDetector mMoveGestureDetector;
    private GestureDetectorCompat mGestureDetectorCompat;

    //! @brief 생성자
    public DrawingView(Context c, HashMap<String, Integer> sizeValues) {
        super(c);
        mWidth = sizeValues.get(c.getString(R.string.key_width));
        mHeight = sizeValues.get(c.getString(R.string.key_height));

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mImageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mImageCanvas = new Canvas(mImageBitmap);

        init();
        init(c);
    }

    public void deleteEntity() {
        int index = -1;

        if (null != mSelectedEntity) {
            for (int i = 0; i < mEntities.size(); i++) {
                if (null != mEntities.get(i) && mEntities.get(i).isSelected()) {
                    index = i;
                    break;
                }
            }
        }

        if (mEntities.size() > 0 && -1 != index) {
            mUnEntities.add(mEntities.remove(index));
        }

        selectEntity(null, true);
    }

    //! getter & setter
    public int getmPenColor() {
        return mPenColor;
    }

    public MotionEntity getmSelectedEntity() {
        return mSelectedEntity;
    }

    public int getmDrawingMode() {
        return mDrawingMode;
    }

    public void setmDrawingViewCallback(DrawingViewCallback mDrawingViewCallback) {
        this.mDrawingViewCallback = mDrawingViewCallback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);

        canvas.drawBitmap(mImageBitmap, 0, 0, mImageBitmapPaint);

        reDrawEntities();
    }

    //! 초기화

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        if (MODE_USE_ERASE == mDrawingMode) {
            mPaint.setStrokeWidth(40);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        } else {
            mPaint.setDither(true);
            mPaint.setColor(mPenColor);
            mPaint.setStrokeWidth(mPenWidth);
            mPaint.setXfermode(null);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        mPaint.setPathEffect(mDashPathEffect);
    }

    private void init(@NonNull Context context) {
        setWillNotDraw(false);

        mSelectedLayerPaint = new Paint();
        mSelectedLayerPaint.setAlpha((int) (255 * Constants.SELECTED_LAYER_ALPHA));
        mSelectedLayerPaint.setAntiAlias(true);

        this.mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.mRotateGestureDetector = new RotateGestureDetector(context, new RotateListener());
        this.mMoveGestureDetector = new MoveGestureDetector(context, new MoveListener());
        this.mGestureDetectorCompat = new GestureDetectorCompat(context, new TapsListener());

        setOnTouchListener(onTouchListener);

        invalidate();
    }

    private void initEntityBorder(@NonNull MotionEntity entity ) {
        int strokeSize = getResources().getDimensionPixelSize(R.dimen.stroke_size);
        Paint borderPaint = new Paint();
        borderPaint.setStrokeWidth(strokeSize);
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.stroke_color));

        entity.setBorderPaint(borderPaint);
    }

    //! 오버라이딩

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mSelectedEntity != null) {
            mSelectedEntity.draw(canvas, mSelectedLayerPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    //! 텍스트, 아이콘 이벤트 리스너

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mScaleGestureDetector != null) {
                mScaleGestureDetector.onTouchEvent(event);
                mRotateGestureDetector.onTouchEvent(event);
                mMoveGestureDetector.onTouchEvent(event);
                mGestureDetectorCompat.onTouchEvent(event);
            }

            //if (mSelectedEntity == null) {
            if (MODE_USE_PEN == mDrawingMode || MODE_USE_ERASE == mDrawingMode) {
                onTouchEvent(event);
            } else {
                mTouched = false;
            }

            return true;
        }
    };

    private class TapsListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mDrawingViewCallback != null && mSelectedEntity != null) {
                mDrawingViewCallback.onEntityDoubleTap(mSelectedEntity);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            updateOnLongPress(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            updateSelectionOnTap(e);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mSelectedEntity != null) {
                float scaleFactorDiff = detector.getScaleFactor();
                mSelectedEntity.getLayer().postScale(scaleFactorDiff - 1.0F);
                invalidate();
            }
            return true;
        }
    }

    private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
        @Override
        public boolean onRotate(RotateGestureDetector detector) {
            if (mSelectedEntity != null) {
                mSelectedEntity.getLayer().postRotate(-detector.getRotationDegreesDelta());
                invalidate();
            }
            return true;
        }
    }

    private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
        @Override
        public boolean onMove(MoveGestureDetector detector) {
            handleTranslate(detector.getFocusDelta());
            return true;
        }
    }


    //! 텍스트, 아이콘 이벤트 처리

    //! @brief 제일 위의 아이콘이 선택되도록 처리
    private void updateSelectionOnTap(MotionEvent e) {
        MotionEntity entity = findEntityAtPoint(e.getX(), e.getY());
        selectEntity(entity, true);
    }

    //! @brief 오래 클릭할 경우 최상단으로 오도록 처리
    private void updateOnLongPress(MotionEvent e) {
        // if layer is currently selected and point inside layer - move it to front
        if (mSelectedEntity != null) {
            PointF p = new PointF(e.getX(), e.getY());
            if (mSelectedEntity.pointInLayerRect(p)) {
                bringLayerToFront(mSelectedEntity);
            }
        }
    }

    //! 선그리기 이벤트 처리

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DrawPoint eventPoint = new DrawPoint(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(eventPoint);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(eventPoint);
                break;
            case MotionEvent.ACTION_UP:
                touchUp(eventPoint);
                break;
        }

        invalidate();
        return true;
    }

    private void touchStart(DrawPoint eventPoint) {
        mTouched = true;
        mUnEntities.clear();
        mX = eventPoint.getfX();
        mY = eventPoint.getfY();
        mPath.moveTo(mX, mY);
    }

    private void touchMove(DrawPoint eventPoint) {
        if (Math.abs(eventPoint.getfX() - mX) >= TOUCH_TOLERANCE || Math.abs(eventPoint.getfY() - mY) >= TOUCH_TOLERANCE) {
            if (LINE_TYPE_FREE == mPenType) {
                mPath.quadTo(mX, mY, (eventPoint.getfX() + mX) / 2, (eventPoint.getfY() + mY) / 2);
                mX = eventPoint.getfX();
                mY = eventPoint.getfY();
            } else {
                reDrawEntities();
                reDrawEntities();
                mPath = new Path();
                mPath.moveTo(mX, mY);

                finishPath(eventPoint);
            }
        }
    }

    private void touchUp(DrawPoint eventPoint) {
        if (mTouched) {
            if (LINE_TYPE_FREE == mPenType) {
                finishPath(eventPoint);
            }

            addPathEntity(mPath, mPaint);
        }

        mTouched = false;
        mPath = new Path();
        init();
    }

    //! 버튼 이벤트 관련 처리
    public void onChangeDrawingMode(int selectedIndex) {
        mDrawingMode = selectedIndex;

        if (MODE_USE_ERASE == selectedIndex) {
            mPenType = LINE_TYPE_FREE;
            mDashPathEffect = null;
        }

        init();
    }

    public void onChangePenType(int selectedIndex) {
        mPenType = selectedIndex;
        init();
    }

    public void onChangeDashType(int selectedIndex) {
        switch (selectedIndex) {
            case 0:
                mDashPathEffect = null;
                break;
            case 1:
                mDashPathEffect = new DashPathEffect(new float[] {10, 20}, 0);
                break;
            case 2:
                mDashPathEffect = new DashPathEffect(new float[] {80, 30}, 0);
                break;
        }

        init();
    }

    public void onChangePenColor(int color) {
        mPenColor = color;
        init();
    }

    public void onChangePenWidth(int selectedIndex) {
        mPenWidth = getResources().getIntArray(R.array.array_pen_width)[selectedIndex];
        init();
    }

    public void onUndo() {
        selectEntity(null, true);

        if (mEntities.size() > 0) {
            mUnEntities.add(mEntities.remove(mEntities.size() - 1));
            reDrawEntities();
        }
    }

    public void onRedo() {
        selectEntity(null, true);

        if (mUnEntities.size() > 0) {
            mEntities.add(mUnEntities.remove(mUnEntities.size() - 1));
            reDrawEntities();
        }
    }

    public void reDrawEntities() {
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mImageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mImageCanvas = new Canvas(mImageBitmap);

        for (MotionEntity entity : mEntities) {
            if (null == entity) {
                mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
                mCanvas = new Canvas(mBitmap);

                mImageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
                mImageCanvas = new Canvas(mImageBitmap);
            } else if (entity instanceof PathEntity) {
                mCanvas.drawPath(((PathEntity) entity).getPath(), ((PathEntity) entity).getPaint());
            } else {
                entity.draw(mImageCanvas, null);
            }
        }

        invalidate();
    }


    public void onClear() {
        selectEntity(null, true);
        mEntities.add(null);
        invalidate();
    }

    //! 내부 처리 함수들

    private void addPathEntity(Path path, Paint paint) {
        Layer layer = new Layer();
        PathEntity entity = new PathEntity(layer, getWidth(), getHeight(), path, paint);

        addEntityAndPosition(entity);
    }

    private void bringLayerToFront(@NonNull MotionEntity entity) {
        // removing and adding brings layer to front
        if (mEntities.remove(entity)) {
            mEntities.add(entity);
            invalidate();
        }
    }

    private void finishPath(DrawPoint eventPoint) {
        mPath.lineTo(eventPoint.getfX(), eventPoint.getfY());

        if (LINE_TYPE_ARROW == mPenType) {
            DrawPoint startPoint = new DrawPoint(mX, mY);
            addArrowHead(mPath, eventPoint, startPoint);
        }

        mCanvas.drawPath(mPath, mPaint);
    }

    private void onRedraw() {
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mImageBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mImageCanvas = new Canvas(mImageBitmap);
        invalidate();
    }

    public double calcAzimuth(DrawPoint point1, DrawPoint point2, boolean bIsDegree) {
        double dResult = Math.atan2(point2.getfX() -point1.getfX(), point2.getfY() - point1.getfY());

        if (bIsDegree) {
            dResult = (dResult * 180 / Math.PI) * -1 + 180;
        }

        return dResult;
    }

    //! @brief 직선 Path에 화살표 모양 머리 추가
    //! @param path 해당 path
    //! @param lastPoint 직선 마지막 점
    //! @param otherPoint 직선 마지막 전 점
    public Path addArrowHead(Path path, DrawPoint lastPoint, DrawPoint otherPoint) {
        double dArrowLength = 40;
        double dArrowAngle = 30;
        double dDegree = calcAzimuth(otherPoint, lastPoint, true);
        double adjustRadian = (dDegree + dArrowAngle - 180) * -1 * Math.PI / 180;
        path.lineTo((float)(lastPoint.getfX() - dArrowLength * Math.sin(adjustRadian)), (float)(lastPoint.getfY() - dArrowLength * Math.cos(adjustRadian)));
        path.moveTo(lastPoint.getfX(), lastPoint.getfY());
        adjustRadian = (dDegree - dArrowAngle - 180) * -1 * Math.PI / 180;
        path.lineTo((float)(lastPoint.getfX() - dArrowLength * Math.sin(adjustRadian)), (float)(lastPoint.getfY() - dArrowLength * Math.cos(adjustRadian)));

        return path;
    }

    public void addEntityAndPosition(@Nullable MotionEntity entity) {
        if (entity != null) {
            initEntityBorder(entity);
            initialTranslateAndScale(entity);
            mEntities.add(entity);
            selectEntity(entity, true);
        }
    }

    public void unselectEntity() {
        if (mSelectedEntity != null) {
            mSelectedEntity.setIsSelected(false);
        }

        invalidate();

        reDrawEntities();
    }

    private void handleTranslate(PointF delta) {
        if (mSelectedEntity != null) {
            float newCenterX = mSelectedEntity.absoluteCenterX() + delta.x;
            float newCenterY = mSelectedEntity.absoluteCenterY() + delta.y;
            // limit entity center to screen bounds
            boolean needUpdateUI = false;
            if (newCenterX >= 0 && newCenterX <= getWidth()) {
                mSelectedEntity.getLayer().postTranslate(delta.x / getWidth(), 0.0F);
                needUpdateUI = true;
            }
            if (newCenterY >= 0 && newCenterY <= getHeight()) {
                mSelectedEntity.getLayer().postTranslate(0.0F, delta.y / getHeight());
                needUpdateUI = true;
            }
            if (needUpdateUI) {
                invalidate();
            }
        }
    }

    private void initialTranslateAndScale(@NonNull MotionEntity entity) {
        entity.moveToCanvasCenter();
        entity.getLayer().setScale(entity.getLayer().initialScale());
    }

    public void selectEntity(@Nullable MotionEntity entity, boolean updateCallback) {
        if (mSelectedEntity != null) {
            mSelectedEntity.setIsSelected(false);
        }

        if (entity != null) {
            entity.setIsSelected(true);
        }

        mSelectedEntity = entity;

        invalidate();

        if (updateCallback && mDrawingViewCallback != null) {
            mDrawingViewCallback.onEntitySelected(entity);
        }
    }

    private MotionEntity findEntityAtPoint(float x, float y) {
        MotionEntity selected = null;
        PointF p = new PointF(x, y);
        for (int i = mEntities.size() - 1; i >= 0; i--) {
            if (null == mEntities.get(i)) {
                break;
            } else if (mEntities.get(i) instanceof PathEntity) {
                continue;
            } else if (mEntities.get(i).pointInLayerRect(p)) {
                selected = mEntities.get(i);
                break;
            }
        }
        return selected;
    }

}
