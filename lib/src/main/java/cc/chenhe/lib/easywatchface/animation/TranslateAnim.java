package cc.chenhe.lib.easywatchface.animation;

/**
 * Created by 晨鹤 on 2017/12/3.
 */

public class TranslateAnim extends Animation {

    private float mX, mY;

    public TranslateAnim() {
    }

    public TranslateAnim(float x, float y, long duration) {
        setDuration(duration);
        this.mX = x;
        this.mY = y;
    }

    @Override
    protected String getAnimationType() {
        return TYPE_TRANSLATE;
    }


    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }
}
