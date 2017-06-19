package lilun.com.pensionlife.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.ScreenUtils;

/**
 * 首页侧滑menu
 *
 * @author yk
 *         create at 2017/2/6 10:29
 *         email : yk_developer@163.com
 */
public class SlidingMenu extends HorizontalScrollView {

    private int mScreenWidth;
    private boolean once;
    private int mMenuRightPadding = 50;
    private int mMenuWidth;
    private int mHalfMenuWidth;

    public SlidingMenu(Context context) {
        super(context);
        mScreenWidth = ScreenUtils.getScreenWith(App.context);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = ScreenUtils.getScreenHeight(context);

//        Logger.d("屏幕宽度" + mScreenWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 显示的设置一个宽度
         */
        if (!once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
            ViewGroup content = (ViewGroup) wrapper.getChildAt(1);
            // dp to px
            mMenuRightPadding = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, content
                            .getResources().getDisplayMetrics());

            mMenuWidth = mScreenWidth/2;
            mHalfMenuWidth = mMenuWidth / 2;
            menu.getLayoutParams().width = mMenuWidth;
//            content.getLayoutParams().width = mScreenWidth;
//            Logger.d("屏幕宽度"+mScreenWidth+"----"+"侧滑宽度"+menu.getLayoutParams().width+"---"+"内容宽度"+content.getLayoutParams().width);
//            mMenuWidth = menu.getWidth();
//            mHalfMenuWidth = mMenuWidth / 2;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            once = true;
        }
        if (mMenuWidth!=0){
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth)
                    this.smoothScrollTo(mMenuWidth, 0);
                else
                    this.smoothScrollTo(0, 0);
                return true;
        }
        return super.onTouchEvent(ev);
    }

}
