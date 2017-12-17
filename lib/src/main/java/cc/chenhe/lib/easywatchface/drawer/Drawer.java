package cc.chenhe.lib.easywatchface.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import cc.chenhe.lib.easywatchface.BaseWatchFaceService;
import cc.chenhe.lib.easywatchface.animation.AlphaAnim;
import cc.chenhe.lib.easywatchface.animation.Animation;
import cc.chenhe.lib.easywatchface.animation.RotateAnim;
import cc.chenhe.lib.easywatchface.animation.TranslateAnim;

/**
 * Created by 晨鹤 on 2017/12/2.
 */

public abstract class Drawer {
    protected ArrayList<AnimationTask> animationTasks;
    protected Paint mPaint;
    protected float mDegree;
    protected float mRouteX, mRouteY;
    protected float mTranslateX, mTranslateY;


    public final void postAnimation(@NonNull Animation animation, @NonNull BaseWatchFaceService context, long delay) {
        if (animationTasks == null)
            animationTasks = new ArrayList<>();
        animationTasks.add(new AnimationTask(animation, context, delay));
    }

    public final void postAnimation(@NonNull Animation animation, @NonNull BaseWatchFaceService context) {
        postAnimation(animation, context, 0);
    }

    protected abstract void onDraw(Canvas canvas);

    public final void draw(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < animationTasks.size(); i++) {
            AnimationTask animationTask = animationTasks.get(i);

            if (animationTask.state != Animation.STATE_RUNNING) {
                if (animationTask.delay > SystemClock.elapsedRealtime() - animationTask.createTime)
                    continue;

                animationTask.start();
                animationTask.notifyAnimStateChanged(Animation.STATE_RUNNING);
            }

            double percent = animationTask.animation.getCancelFlag() ? 1 :
                    (SystemClock.elapsedRealtime() - animationTask.startTime) * 1d / animationTask.animation.getDuration();
            percent = percent > 1 ? 1 : percent;

            switch (animationTask.animation.getType()) {
                case Animation.TYPE_ALPHA:
                    executeAlphaAnim((AlphaAnim) animationTask.animation, percent, canvas);
                    break;
                case Animation.TYPE_ROTATE:
                    executeRotateAnim((RotateAnim) animationTask.animation, percent, canvas);
                    break;
                case Animation.TYPE_TRANSLATE:
                    executeTranslateAnim((TranslateAnim) animationTask.animation, percent, canvas);
                    break;
                default:
                    executeCustomAnim(animationTask.animation, percent, canvas);
                    break;
            }

            if (percent == 1) {
                if (animationTask.animation.getCancelFlag()) {
                    animationTask.notifyAnimStateChanged(Animation.STATE_CANCELED);
                    animationTasks.remove(i);
                    i--;
                    continue;
                }

                animationTask.notifyAnimStateChanged(Animation.STATE_COMPLETE);
                animationTasks.remove(i);
                i--;
            }
        }
        if (mTranslateX != 0 || mTranslateY != 0) canvas.translate(mTranslateX, mTranslateY);
        if (mDegree != 0) canvas.rotate(mDegree, mRouteX, mRouteY);
        onDraw(canvas);
        canvas.restore();
    }

    protected final void executeAlphaAnim(@NonNull AlphaAnim anim, double percent, @NonNull Canvas canvas) {
        mPaint.setAlpha((int) ((anim.getEndAlpha() - anim.getStartAlpha()) * percent + anim.getStartAlpha()));
    }

    protected final void executeRotateAnim(@NonNull RotateAnim anim, double percent, @NonNull Canvas canvas) {
        mDegree = (float) (anim.getDegree() * percent);
        mDegree = mDegree % 360f;
        mRouteX = anim.getPx();
        mRouteY = anim.getPy();
    }

    protected final void executeTranslateAnim(@NonNull TranslateAnim anim, double percent, @NonNull Canvas canvas) {
        mTranslateX = (float) (anim.getX() * percent);
        mTranslateY = (float) (anim.getY() * percent);
    }

    protected void executeCustomAnim(@NonNull Animation anim, double percent, @NonNull Canvas canvas) {
    }

    protected final class AnimationTask {
        BaseWatchFaceService context;
        Animation animation;
        long createTime, startTime;
        long delay;
        @Animation.AnimationState
        int state = Animation.STATE_DEFAULT;

        AnimationTask(@NonNull Animation animation, @NonNull BaseWatchFaceService context, long delay) {
            this.animation = animation;
            this.context = context;
            this.delay = delay;
            this.createTime = SystemClock.elapsedRealtime();
        }

        void notifyAnimStateChanged(@Animation.AnimationState int state) {
            context.onAnimationStateChanged(animation.getId(), state);
        }

        void start() {
            state = Animation.STATE_RUNNING;
            startTime = SystemClock.elapsedRealtime();
        }

    }
}
