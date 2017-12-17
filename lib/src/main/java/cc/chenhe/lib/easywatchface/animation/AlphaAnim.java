package cc.chenhe.lib.easywatchface.animation;

/**
 * Created by 晨鹤 on 2017/12/2.
 */

public final class AlphaAnim extends Animation {
    private int startAlpha, endAlpha;

    public AlphaAnim() {
    }

    public AlphaAnim(int startAlpha, int endAlpha, long duration) {
        setDuration(duration);
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
    }

    @Override
    protected String getAnimationType() {
        return TYPE_ALPHA;
    }

    public int getStartAlpha() {
        return startAlpha;
    }

    public void setStartAlpha(int startAlpha) {
        this.startAlpha = startAlpha;
    }

    public int getEndAlpha() {
        return endAlpha;
    }

    public void setEndAlpha(int endAlpha) {
        this.endAlpha = endAlpha;
    }
}
