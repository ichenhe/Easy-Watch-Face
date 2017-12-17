package cc.chenhe.lib.easywatchface.animation;

/**
 * Created by 晨鹤 on 2017/12/3.
 */

public final class RotateAnim extends Animation {
    private float degree, px, py;

    public RotateAnim() {
    }

    public RotateAnim(float degree, float px, float py, long duration) {
        setDuration(duration);
        setDegree(degree);
        setPx(px);
        setPy(py);
    }

    @Override
    protected String getAnimationType() {
        return TYPE_ROTATE;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public float getPy() {
        return py;
    }

    public void setPy(float py) {
        this.py = py;
    }
}
