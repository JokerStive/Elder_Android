package lilun.com.pension.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
*侧滑冲突的viewpager
*@author yk
*create at 2017/2/8 9:28
*email : yk_developer@163.com
*/
public class SlidingMenuViewPager extends ViewPager {
    public SlidingMenuViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}
