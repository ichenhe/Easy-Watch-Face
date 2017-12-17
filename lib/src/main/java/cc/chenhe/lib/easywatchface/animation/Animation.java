package cc.chenhe.lib.easywatchface.animation;

import android.os.SystemClock;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 晨鹤 on 2017/12/2.
 */

public abstract class Animation {
    public static final String TYPE_ALPHA = "cc.chenhe.TYPE_ALPHA";
    public static final String TYPE_ROTATE = "cc.chenhe.TYPE_ROTATE";
    public static final String TYPE_TRANSLATE = "cc.chenhe.TYPE_TRANSLATE";

    public static final int STATE_DEFAULT = 10;
    public static final int STATE_RUNNING = 11;
    public static final int STATE_CANCELED = 12;
    public static final int STATE_COMPLETE = 13;

    @IntDef({STATE_RUNNING, STATE_CANCELED, STATE_COMPLETE, STATE_DEFAULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationState {
    }

    private final long mId = SystemClock.elapsedRealtimeNanos();
    private boolean mCancelFlag = false;
    private long mDuration;
    private String mType;

    public Animation() {
        this.mType = getAnimationType();
    }

    protected abstract String getAnimationType();

    public String getType() {
        return mType;
    }

    public boolean getCancelFlag() {
        return mCancelFlag;
    }

    public void setCancelFlag(boolean cancelFlag) {
        this.mCancelFlag = cancelFlag;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public long getId() {
        return mId;
    }
}
