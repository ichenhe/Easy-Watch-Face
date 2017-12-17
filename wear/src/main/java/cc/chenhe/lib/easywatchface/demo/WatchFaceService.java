package cc.chenhe.lib.easywatchface.demo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.WindowInsets;

import java.util.Calendar;

import cc.chenhe.lib.easywatchface.BaseWatchFaceService;
import cc.chenhe.lib.easywatchface.animation.AlphaAnim;
import cc.chenhe.lib.easywatchface.animation.Animation;
import cc.chenhe.lib.easywatchface.animation.RotateAnim;
import cc.chenhe.lib.easywatchface.animation.TranslateAnim;
import cc.chenhe.lib.easywatchface.common.WatchFaceTime;
import cc.chenhe.lib.easywatchface.common.WatchMode;
import cc.chenhe.lib.easywatchface.common.WatchShape;
import cc.chenhe.lib.easywatchface.common.util.Rate;
import cc.chenhe.lib.easywatchface.drawer.LineDrawer;

/**
 * Created by 晨鹤 on 2017/11/30.
 */

public class WatchFaceService extends BaseWatchFaceService {

    private Paint mPaint;
    private String s;

    @Override
    protected long getInteractiveModeUpdateRate() {
        return Rate.INTERVAL_30FPS;
    }

    @Override
    protected void onWatchModeChanged(WatchMode watchMode) {
        super.onWatchModeChanged(watchMode);
    }


    LineDrawer lineDrawer;

    AlphaAnim alphaAnim;
    RotateAnim rotateAnim;
    TranslateAnim translateAnim;

    @Override
    protected void onLayout(WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        super.onLayout(shape, screenBounds, screenInsets);
        mPaint = new Paint();
        mPaint.setTextSize(50);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);

        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
        lineDrawer = new LineDrawer(0, screenBounds.height() / 2f, 30, screenBounds.height() / 2f, paint);

        alphaAnim = new AlphaAnim();
        alphaAnim.setStartAlpha(0);
        alphaAnim.setEndAlpha(255);
        alphaAnim.setDuration(5000);
        lineDrawer.postAnimation(alphaAnim, this);

        rotateAnim = new RotateAnim(90, screenBounds.width() / 2f,
                screenBounds.height() / 2f, 3000);
        lineDrawer.postAnimation(rotateAnim, this, 2000);

        translateAnim = new TranslateAnim(0, 100, 3000);
        lineDrawer.postAnimation(translateAnim, this, 2000);
    }

    private String getAnimName(long id) {
        if (id == alphaAnim.getId())
            return "Alpha";
        else if (id == rotateAnim.getId())
            return "Rotate";
        else if (id == translateAnim.getId())
            return "Translate";
        return "null";
    }

    @Override
    public void onAnimationStateChanged(long animId, @Animation.AnimationState int state) {
        super.onAnimationStateChanged(animId, state);

        switch (state) {
            case Animation.STATE_CANCELED:
                Log.e("Anim", getAnimName(animId) + "---CANCELED");
                break;
            case Animation.STATE_COMPLETE:
                Log.e("Anim", getAnimName(animId) + "---COMPLETE");
                break;
            case Animation.STATE_DEFAULT:
                Log.e("Anim", getAnimName(animId) + "---DEFAULT");
                break;
            case Animation.STATE_RUNNING:
                Log.e("Anim", getAnimName(animId) + "---RUNNING");
                break;
        }
    }

    @Override
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
        super.onTimeChanged(oldTime, newTime);
        s = newTime.get(Calendar.HOUR_OF_DAY) + ":" + newTime.get(Calendar.MINUTE) + ":" + newTime.get(Calendar.SECOND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawText(s, canvas.getWidth() / 2f, canvas.getHeight() / 2f, mPaint);

        lineDrawer.draw(canvas);
    }
}
