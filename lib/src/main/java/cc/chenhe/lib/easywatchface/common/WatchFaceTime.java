/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ustwo studio inc (www.ustwo.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cc.chenhe.lib.easywatchface.common;

import android.support.annotation.NonNull;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A point in time.
 */
public class WatchFaceTime extends GregorianCalendar {
    /**
     * The number of milliseconds passed within the current second. From 0 to 999.
     */
//    public int millis;

    /**
     * Hour of the morning or afternoon. [0,11]
     */
//    public int hour12;
    public WatchFaceTime() {
        super();
        reset();
    }

    public void setToNow() {
        setTimeInMillis(System.currentTimeMillis());
    }

    public void set(@NonNull WatchFaceTime that) {
        setTimeZone(that.getTimeZone());
        setTimeInMillis(that.getTimeInMillis());
    }

//    private void setHour12() {
//        hour12 = (hour % 12);
//    }

    /**
     * Set to now and set the time zone to the default.
     */
    protected void reset() {
        clear();
        setTimeZone(TimeZone.getDefault());
        setToNow();
    }

    /**
     * Determine if the hour value has changed.
     *
     * @param otherTime a WatchFaceTime to compare to.
     * @return true if it changed, otherwise false.
     */
    public boolean hasHourChanged(@NonNull WatchFaceTime otherTime) {
        return get(HOUR_OF_DAY) != otherTime.get(HOUR_OF_DAY);
    }

    /**
     * Determine if the minute value has changed.
     *
     * @param otherTime a WatchFaceTime to compare to.
     * @return true if it changed, otherwise false.
     */
    public boolean hasMinuteChanged(@NonNull WatchFaceTime otherTime) {
        return get(MINUTE) != otherTime.get(MINUTE);
    }

    /**
     * Determine if the second value has changed.
     *
     * @param otherTime a WatchFaceTime to compare to.
     * @return true if it changed, otherwise false.
     */
    public boolean hasSecondChanged(@NonNull WatchFaceTime otherTime) {
        return get(SECOND) != otherTime.get(SECOND);
    }

    /**
     * Determine if the day of the year or the year changed.
     *
     * @param otherTime a WatchFaceTime to compare to.
     * @return true if it changed, otherwise false.
     */
    public boolean hasDateChanged(@NonNull WatchFaceTime otherTime) {
        return (get(DAY_OF_YEAR) != otherTime.get(DAY_OF_YEAR)) ||
                (get(YEAR) != otherTime.get(YEAR));
    }

    /**
     * Determine if the time zone changed.
     *
     * @param otherTime a WatchFaceTime to compare to.
     * @return true if it changed, otherwise false.
     */
    public boolean hasTimeZoneChanged(@NonNull WatchFaceTime otherTime) {
        return (getTimeZone() == null ? otherTime.getTime() != null :
                !getTimeZone().equals(otherTime.getTimeZone()));
    }

}
