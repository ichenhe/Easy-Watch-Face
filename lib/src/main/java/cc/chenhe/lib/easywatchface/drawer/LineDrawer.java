package cc.chenhe.lib.easywatchface.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cc.chenhe.lib.easywatchface.animation.Animation;

/**
 * Created by 晨鹤 on 2017/12/2.
 */

public class LineDrawer extends Drawer {
    private float mStartX, mStartY, mStopX, mStopY;

    public LineDrawer(float startX, float startY, float stopX, float stopY, @Nullable Paint paint) {
        mStartX = startX;
        mStartY = startY;
        mStopX = stopX;
        mStopY = stopY;
        mPaint = paint == null ? new Paint() : paint;
    }

    @Override
    protected void executeCustomAnim(@NonNull Animation anim, double percent, @NonNull Canvas canvas) {
        super.executeCustomAnim(anim, percent, canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
    }

}
