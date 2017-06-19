package lilun.com.pensionlife.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.module.utils.UIUtils;

/**
 * 求助倒计时圆环
 *
 * @author yk
 *         create at 2017/1/23 13:58
 *         email : yk_developer@163.com
 */
public class CountDown extends View {

    private final Context mContext;
    private Paint mWithinCirclePaint;
    private int mCenterX;
    private int mCenterY;
    private Paint mOutCirclePaint;
    private int mRadio = 94;
    private int time = 7;
    private Path outCirclePath;
    private PathMeasure pathMeasure;
    private ValueAnimator mValueAnimator;
    private float mAnimatedValue;


    private State mCurrentState = State.NONE;

    public CountDown(Context context) {
        super(context);
        mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels / dm.density > 820) mRadio = 282;
        init();
    }


    public CountDown(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if (dm.widthPixels / dm.density > 820) mRadio = 282;
        init();
    }

    private void init() {
        initPaint();
        initPath();
    }

    private void initAnimator() {
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(time * 1000);
        mValueAnimator.addUpdateListener(animation -> {
            mAnimatedValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        mValueAnimator.start();
    }

    private void initPath() {
        outCirclePath = new Path();


        pathMeasure = new PathMeasure();

    }

    private void initPaint() {
        mWithinCirclePaint = new Paint();
        mWithinCirclePaint.setStrokeWidth(20);
        mWithinCirclePaint.setColor(mContext.getResources().getColor(R.color.red));
        mWithinCirclePaint.setStyle(Paint.Style.STROKE);
        mWithinCirclePaint.setAntiAlias(true);

        mOutCirclePaint = new Paint();
        mOutCirclePaint.setStrokeWidth(48);
        mOutCirclePaint.setColor(mContext.getResources().getColor(R.color.red_deep));
        mOutCirclePaint.setStyle(Paint.Style.STROKE);
        mOutCirclePaint.setAntiAlias(true);
    }

    private void setTime(int time) {
        this.time = time;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

    }

    // 这个视图拥有的状态
    public static enum State {
        NONE,
        STARTING
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);

    }

    private void drawCircle(Canvas canvas) {
        canvas.translate(mCenterX, mCenterY);

        canvas.rotate(-90);
        switch (mCurrentState) {
            case NONE:
                outCirclePath.reset();
                outCirclePath.addCircle(0, 0, UIUtils.dp2px(mContext, mRadio), Path.Direction.CW);
                canvas.drawPath(outCirclePath, mOutCirclePaint);
                pathMeasure.setPath(outCirclePath, false);
                mCurrentState = State.STARTING;

                //画好之后开启动画
                mValueAnimator.start();
                break;
            case STARTING:
                canvas.drawCircle(0, 0, UIUtils.dp2px(mContext, mRadio), mWithinCirclePaint);
                Path dst = new Path();
                pathMeasure.getSegment(0, pathMeasure.getLength() * mAnimatedValue, dst, true);
                canvas.drawPath(dst, mOutCirclePaint);

                if (mAnimatedValue == 1.0) {

                    stopAnimator();
                }
        }
    }

    public void stopAnimator() {
        Logger.d("animator dismiss");
        mValueAnimator.cancel();
        mValueAnimator.removeAllUpdateListeners();
    }

    public void startAnimator(int time) {
        setTime(time);
        initAnimator();
    }
}
