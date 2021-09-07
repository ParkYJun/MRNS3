package team.uptech.motionviews.widget.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import team.uptech.motionviews.viewmodel.Layer;

/**
 * Created by WAVUS_KKH on 2017-08-31.
 */

public class PathEntity extends MotionEntity{
    private Path mPath;
    private Paint mPaint;

    public PathEntity(@NonNull Layer layer, @IntRange(from = 1) int canvasWidth, @IntRange(from = 1) int canvasHeight, Path path, Paint paint) {
        super(layer, canvasWidth, canvasHeight);

        mPath = path;
        mPaint = paint;

        // initial position of the entity
        srcPoints[0] = 0; srcPoints[1] = 0;
        srcPoints[2] = 0; srcPoints[3] = 0;
        srcPoints[4] = 0; srcPoints[5] = 0;
        srcPoints[6] = 0; srcPoints[7] = 0;
        srcPoints[8] = 0; srcPoints[8] = 0;
    }

    @Override
    protected void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    public Path getPath() {
        return mPath;
    }

    public Paint getPaint() {
        return mPaint;
    }
}
