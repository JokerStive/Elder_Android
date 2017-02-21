package lilun.com.pension.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
*通用按下灰色的button
*@author yk
*create at 2017/2/16 19:22
*email : yk_developer@163.com
*/
public class CommonImageView extends ImageView{
    public CommonImageView(Context context) {
        super(context);
    }

    public CommonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Drawable mDrawable;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDrawable = getBackground();
                mDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDrawable.clearColorFilter();
                break;
        }
        return super.onTouchEvent(event);
    }
}
