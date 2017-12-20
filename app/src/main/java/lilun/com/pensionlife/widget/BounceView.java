package lilun.com.pensionlife.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 可弹动可移动的ImageView
 * Created by zp on 2017/12/15.
 */

public class BounceView extends android.support.v7.widget.AppCompatImageView {
    String TAG = "BounceView";
    private ValueAnimator animator;
    private int startX;
    private int startY;

    public BounceView(Context context) {
        super(context);
        init();
    }

    public BounceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BounceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progess = (float) animation.getAnimatedValue();

                //     Log.d("zp", getMeasuredWidth() + " " + getMeasuredHeight() + " " + getX() + " " + getY());
                if (progess < 0.2) {
                    float per = (float) Math.sin(Math.PI / 180 * progess * 5 * 720);
                    setScaleY(1 - per * 0.5f);
                }
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();

    }

    float tranx, trany;
    private boolean isClickState;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClickState = true;
                startX = (int) (event.getRawX());
                startY = (int) (event.getRawY());
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getRawX() - startX) > 40 || Math.abs(event.getRawY() - startY) > 40) {
                    isClickState = false;
                    moveView(event.getRawX() - startX + tranx, event.getRawY() - startY + trany);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                tranx = getTranslationX();
                trany = getTranslationY();
                if (isClickState)
                    performClick();
        }
        return true;
    }


    public void moveView(float dx, float dy) {
        float tx, ty;
        //设置可以移动的范围
        tx = dx;
        ty = dy;
        View parent = (View) getParent();
        if (getLeft() + dx <= 0) tx = -getLeft();
        else if (getRight() + dx >= parent.getWidth()) tx = parent.getWidth() - getRight();
        if (getTop() + dy <= 0) ty = -getTop();
        else if (getBottom() + dy >= parent.getHeight()) ty = parent.getHeight() - getBottom();

        setTranslationX(tx);
        setTranslationY(ty);
    }


}
