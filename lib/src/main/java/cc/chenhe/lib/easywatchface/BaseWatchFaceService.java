package cc.chenhe.lib.easywatchface;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.wearable.complications.ComplicationData;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cc.chenhe.lib.easywatchface.animation.Animation;
import cc.chenhe.lib.easywatchface.common.WatchFaceTime;
import cc.chenhe.lib.easywatchface.common.WatchMode;
import cc.chenhe.lib.easywatchface.common.WatchShape;
import cc.chenhe.lib.easywatchface.common.util.Logr;

/**
 * Created by Chenhe on 2017/11/21.
 */

public abstract class BaseWatchFaceService extends WatchFaceService {
    private static final String TAG = BaseWatchFaceService.class.getSimpleName();

    private final BaseEngine mWatchFaceEngine = new BaseEngine();
    private boolean mLayoutComplete;
    private final Rect mFaceRect = new Rect();
    private WatchShape mWatchShape = WatchShape.UNKNOWN;
    private WindowInsets mFaceInsets;
    private final WatchFaceTime mPreviousTime = new WatchFaceTime();
    private final WatchFaceTime mLatestTime = new WatchFaceTime();
    private boolean mIs24HourFormat = false;
    private boolean mIsAmbient = false;
    private boolean mLowBitAmbient = false;
    private boolean mBurnInProtection = false;
    private ContentObserver mFormatChangeObserver;

    @Override
    public Engine onCreateEngine() {
        Logr.v("WatchFace.onCreateEngine");

        return mWatchFaceEngine;
    }

    /**
     * Returns true if user preference is set to 24-hour format.
     *
     * @return true if 24 hour time format is selected, false otherwise.
     */
    public final boolean is24HourFormat() {
        return mIs24HourFormat;
    }

    /**
     * Returns the width of the watch face.
     *
     * @return value of the watch face {@link android.graphics.Rect}s width
     */
    public final int getWidth() {
        return mFaceRect.width();
    }

    /**
     * Returns the height of the watch face.
     *
     * @return value of the watch face {@link android.graphics.Rect}s height
     */
    public final int getHeight() {
        return mFaceRect.height();
    }

    /**
     * Returns the shape of the watch face (e.g. round, square)
     *
     * @return watch face {@link WatchShape}.
     */
    public final WatchShape getWatchShape() {
        return mWatchShape;
    }

    /**
     * Called when the size and shape of the watch face are first realized, and then every time they
     * are changed.
     *
     * @param shape        the watch screen shape.
     * @param screenBounds the raw screen size.
     * @param screenInsets the screen's window insets.
     */
    protected void onLayout(WatchShape shape, Rect screenBounds, WindowInsets screenInsets) {
        Logr.v(String.format("WatchFace.onLayout: Shape=%s; Bounds=%s; Insets=%s", shape.name(),
                screenBounds, screenInsets));
    }

    /**
     * Lifecycle event guaranteed to be called once after {@link #onLayout(WatchShape, Rect, WindowInsets)}
     * has been called for the first time.
     */
    protected void onLayoutCompleted() {
    }

    /**
     * Override to provide a custom {@link android.support.wearable.watchface.WatchFaceStyle} for the
     * watch face.
     *
     * @return {@link android.support.wearable.watchface.WatchFaceStyle} for watch face.
     */
    protected WatchFaceStyle getWatchFaceStyle() {
        return null;
    }

    /**
     * Called when the system tells us the current watch mode has changed (e.g.
     * {@link BaseWatchFaceService.BaseEngine#onAmbientModeChanged}).
     *
     * @param watchMode the current {@link WatchMode}
     */
    protected void onWatchModeChanged(WatchMode watchMode) {
        Logr.v(String.format("WatchFace.onWatchModeChanged: watchMode=%s", watchMode.name()));
    }

    /**
     * Called when the "Use 24-hour format" user setting is changed.
     * The first detection will not trigger.
     *
     * @param is24HourFormat is 24h format used or not.
     */
    protected void on24HourFormatChanged(boolean is24HourFormat) {
    }

    public void onAnimationStateChanged(long animId, @Animation.AnimationState int state) {
    }

    /**
     * Returns the {@link WatchMode#INTERACTIVE} mode update rate in millis.
     * This will tell the {@link BaseWatchFaceService} base class the period to call
     * {@link #onTimeChanged(WatchFaceTime, WatchFaceTime)} and
     * {@link #onDraw(android.graphics.Canvas)}.
     * <br>Attention: in {@link WatchMode#INTERACTIVE} mode, {@link BaseEngine#onTimeTick()}
     * will do nothing.
     * <br><br>DEFAULT={@link android.text.format.DateUtils#MINUTE_IN_MILLIS}
     *
     * @return number of millis to wait before calling onTimeChanged and onDraw.
     */
    protected long getInteractiveModeUpdateRate() {
        return DateUtils.MINUTE_IN_MILLIS;
    }

    /**
     * Override to perform view and logic updates. This will be called once per minute
     * ({@link BaseWatchFaceService.BaseEngine#onTimeTick()}) in
     * {@link WatchMode#AMBIENT} modes and once per {@link #getInteractiveModeUpdateRate()} in
     * {@link WatchMode#INTERACTIVE} mode.
     * This is also called when the date, time, and/or time zone (ACTION_DATE_CHANGED,
     * ACTION_TIME_CHANGED, and ACTION_TIMEZONE_CHANGED intents, respectively) is changed on the watch.
     *
     * @param oldTime {@link WatchFaceTime} last time this method was called.
     * @param newTime updated {@link WatchFaceTime}
     */
    @SuppressLint("DefaultLocale")
    protected void onTimeChanged(WatchFaceTime oldTime, WatchFaceTime newTime) {
//        Logr.v(String.format("WatchFace.onTimeChanged: oldTime=%d; newTime=%d",
//                oldTime.getTimeInMillis(), newTime.getTimeInMillis()));
    }

    /**
     * Override to render watch face on Canvas. This will be called once per minute by
     * ({@link BaseWatchFaceService.BaseEngine#onTimeTick()}) in
     * {@link WatchMode#AMBIENT} modes and once per {@link #getInteractiveModeUpdateRate()} in
     * {@link WatchMode#INTERACTIVE} mode.
     *
     * @param canvas canvas on which to draw watch face.
     */
    protected abstract void onDraw(Canvas canvas);

    /**
     * Override to be informed of touch event.
     *
     * @param tapType   event type which were declared in {@link WatchFaceService}.
     * @param x         X coordinate of the event.
     * @param y         Y coordinate of the event.
     * @param eventTime The time the tap occurred, in the {@link SystemClock#uptimeMillis()} time base.
     */
    protected void onTapCommand(@TapType int tapType, int x, int y, long eventTime) {
        Logr.v("WatchFace.onTapCommand: " + tapType);
    }

    /**
     * Change the {@link WatchMode#INTERACTIVE} mode update rate for the
     * duration of the current interactive mode . The mode will change back to one specified by
     * {@link #getInteractiveModeUpdateRate()} once the watch returns to interactive mode.
     * May be useful for creating animations when a higher-than-normal update rate is desired for a
     * short period of time.
     * This will tell the {@link BaseWatchFaceService} base class the period to call
     * {@link #onTimeChanged(WatchFaceTime, WatchFaceTime)} and
     * {@link #onDraw(android.graphics.Canvas)}.
     *
     * @param updateRateMillis      The new update rate, expressed in milliseconds between updates
     * @param delayUntilWholeSecond Whether the first update should start on a whole second (i.e. when milliseconds are 0)
     */
    public void startPresentingWithUpdateRate(long updateRateMillis, boolean delayUntilWholeSecond) {
        mWatchFaceEngine.checkTimeUpdater(updateRateMillis, delayUntilWholeSecond);
    }

    public void setActiveComplications(int[] ids) {
        mWatchFaceEngine.setActiveComplications(ids);
    }

    /**
     * Override to be informed of complication data update.
     * This will be called by
     * {@link WatchFaceService.Engine#onComplicationDataUpdate(int, ComplicationData)}
     *
     * @param id   The id of the complication that the data relates to.
     * @param data The data that should be displayed in the complication.
     */
    protected void onComplicationDataUpdate(int id, ComplicationData data) {
    }

    /**
     * Returns the current {@link WatchMode}.
     *
     * @return the current {@link WatchMode}.
     */
    protected final WatchMode getCurrentWatchMode() {
        WatchMode watchMode;
        if (mIsAmbient) {
            if (mBurnInProtection) {
                if (mLowBitAmbient) {
                    watchMode = WatchMode.LOW_BIT_BURN_IN;
                } else {
                    watchMode = WatchMode.BURN_IN;
                }
            } else if (mLowBitAmbient) {
                watchMode = WatchMode.LOW_BIT;
            } else {
                watchMode = WatchMode.AMBIENT;
            }
        } else {
            watchMode = WatchMode.INTERACTIVE;
        }
        return watchMode;
    }

    /**
     * Invalidates the entire canvas and forces {@link #onDraw(android.graphics.Canvas)} to be called.
     */
    public final void invalidate() {
        Canvas canvas = mWatchFaceEngine.getSurfaceHolder().lockCanvas();
        if (canvas == null) {
            Logr.d("Cannot execute invalidate - WatchFaceService Engine Canvas is null.");
            return;
        }

        try {
            onDraw(canvas);
        } catch (Exception e) {
            Log.e(TAG, "Exception in WatchFace onDraw", e);
        } finally {
            mWatchFaceEngine.getSurfaceHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Reload the time format config and call {@link #on24HourFormatChanged(boolean)} only if
     * it is different from the previous config.
     */
    private void updateTimeFormat() {
        boolean is24Hour = DateFormat.is24HourFormat(BaseWatchFaceService.this);
        if (is24Hour != mIs24HourFormat) {
            mIs24HourFormat = is24Hour;
            on24HourFormatChanged(mIs24HourFormat);
        }
    }


    private void updateTimeAndInvalidate() {
        mPreviousTime.set(mLatestTime);
        mLatestTime.setToNow();

        onTimeChanged(mPreviousTime, mLatestTime);

        invalidate();
    }

    public class BaseEngine extends Engine {
        private final ScheduledExecutorService mScheduledTimeUpdaterPool =
                Executors.newScheduledThreadPool(2);
        private ScheduledFuture<?> mScheduledTimeUpdater;

        private final BroadcastReceiver mDateTimeChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))
                    mLatestTime.setTimeZone(TimeZone.getDefault());
                updateTimeAndInvalidate();
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_DATE_CHANGED);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            registerReceiver(mDateTimeChangedReceiver, filter);

            mIs24HourFormat = DateFormat.is24HourFormat(BaseWatchFaceService.this);

            if (mFormatChangeObserver == null) {
                mFormatChangeObserver = new TimeFormatObserver(new Handler());
                getContentResolver().registerContentObserver(
                        Settings.System.getUriFor(Settings.System.TIME_12_24), true, mFormatChangeObserver);
            }
        }

        @Override
        public void onDestroy() {
            unregisterReceiver(mDateTimeChangedReceiver);

            if (mFormatChangeObserver != null) {
                getContentResolver().unregisterContentObserver(
                        mFormatChangeObserver);
                mFormatChangeObserver = null;
            }

            cancelTimeUpdater();
            mScheduledTimeUpdaterPool.shutdown();

            super.onDestroy();
        }

        private final Runnable mTimeUpdater = new Runnable() {
            @Override
            public void run() {
                updateTimeAndInvalidate();
            }
        };

        private void cancelTimeUpdater() {
            if (mScheduledTimeUpdater != null) {
                mScheduledTimeUpdater.cancel(true);
            }
        }

        private boolean isTimeUpdaterRunning() {
            return (mScheduledTimeUpdater != null && !mScheduledTimeUpdater.isCancelled());
        }

        private void checkTimeUpdater() {
            checkTimeUpdater(getInteractiveModeUpdateRate(), true);
        }

        /**
         * First cancel the Updater and restart it according to the situation.
         *
         * @param updateRate The interval of call {@link #onDraw(Canvas)} in {@link WatchMode#INTERACTIVE} mode.
         * @param delayStart Whether the first update should start on a whole second.
         */
        private void checkTimeUpdater(long updateRate, boolean delayStart) {
            cancelTimeUpdater();
            // If we're ambient or invisible, we rely on timeTick to update instead of a scheduled future.
            if (!mIsAmbient && isVisible()) {
                // start updater on next second (millis = 0) when delayed start is requested
                long initialDelay = (delayStart ? DateUtils.SECOND_IN_MILLIS - (System.currentTimeMillis() % 1000) : 0);
                // We use scheduleAtFixedRate(), so that the time it takes for the function to execute
                // will not effect the redraw's rate.
                mScheduledTimeUpdater = mScheduledTimeUpdaterPool.scheduleAtFixedRate(mTimeUpdater,
                        initialDelay, updateRate, TimeUnit.MILLISECONDS);
            }
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            Logr.d(String.format("WatchFace.onSurfaceChanged: format=%d; width=%d; height=%d",
                    format, width, height));

            if (mFaceRect.width() != width || mFaceRect.height() != height) {
                mFaceRect.set(0, 0, width, height);

                if (mLayoutComplete) {
                    // A size change has occurred after the first layout. The subclass must re-layout.
                    onLayout(mWatchShape, mFaceRect, mFaceInsets);
                } // else, we wait for onApplyWindowInsets to perform the first layout.
            }
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);
            Logr.d("WatchFace.onApplyWindowInsets: " + "isRound=" + Boolean.toString(insets.isRound()));

            mFaceInsets = insets;
            mWatchShape = mFaceInsets.isRound() ? WatchShape.CIRCLE : WatchShape.SQUARE;

            WatchFaceStyle watchFaceStyle = getWatchFaceStyle();
            if (watchFaceStyle != null) {
                setWatchFaceStyle(watchFaceStyle);
            }

            onLayout(mWatchShape, mFaceRect, mFaceInsets);

            // Start the time updater after the first layout is complete.
            if (!mLayoutComplete) {
                mLayoutComplete = true;
                onLayoutCompleted();
                updateTimeAndInvalidate();
                checkTimeUpdater();
            }
        }

        @Override
        public void onTapCommand(@TapType int tapType, int x, int y, long eventTime) {
            super.onTapCommand(tapType, x, y, eventTime);
            BaseWatchFaceService.this.onTapCommand(tapType, x, y, eventTime);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false);

            Logr.d("WatchFace.onPropertiesChanged: " + "LowBit=" + Boolean.toString(mLowBitAmbient) +
                    "; BurnIn=" + Boolean.toString(mBurnInProtection));
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            Logr.v("WatchFace.onAmbientModeChanged: " + Boolean.toString(inAmbientMode));

            if (mIsAmbient != inAmbientMode) {
                mIsAmbient = inAmbientMode;

                onWatchModeChanged(getCurrentWatchMode());
                updateTimeAndInvalidate();
                checkTimeUpdater();
            }
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            Logr.v("WatchFace.onTimeTick");

            // only update if layout has completed and time updater not running
            if (mLayoutComplete && !isTimeUpdaterRunning()) {
                updateTimeAndInvalidate();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            // need to call super, otherwise onPropertiesChanged will not be called
            super.onVisibilityChanged(visible);
            Logr.v("WatchFace.onVisibilityChanged: " + visible);

            if (visible) {
                updateTimeAndInvalidate();
            }
            checkTimeUpdater();
        }

        @Override
        public void onComplicationDataUpdate(int id, ComplicationData data) {
            BaseWatchFaceService.this.onComplicationDataUpdate(id, data);
        }

        private class TimeFormatObserver extends ContentObserver {
            TimeFormatObserver(Handler handler) {
                super(handler);
            }

            @Override
            public void onChange(boolean selfChange) {
                updateTimeFormat();
                updateTimeAndInvalidate();
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                updateTimeFormat();
                updateTimeAndInvalidate();
            }
        }
    }
}