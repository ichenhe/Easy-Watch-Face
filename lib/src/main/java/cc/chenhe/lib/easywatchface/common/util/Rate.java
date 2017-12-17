package cc.chenhe.lib.easywatchface.common.util;

import android.text.format.DateUtils;


/**
 * Created by Chenhe on 2017/11/22.
 * <p>
 * A class about canvas refresh rate.
 */

public class Rate {
    public static final long INTERVAL_30FPS = DateUtils.SECOND_IN_MILLIS / 30;

    public static int interval2fps(long interval) {
        return (int) (DateUtils.SECOND_IN_MILLIS / interval);
    }

    public static long fps2interval(int fps) {
        return DateUtils.SECOND_IN_MILLIS / fps;
    }
}
