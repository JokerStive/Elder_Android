package lilun.com.pension.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * 圆形imageView
 */

public class CircleImageView extends ImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int defWith = 100;
        int defHeight = 100;
        int defSize;
        //获取宽高的测量模式
        int withSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int withSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //当宽/高为wrap_content即对用AT_MOST的时候，设置一个默认值给系统
        if (withSpecSize!=0 && heightSpecSize!=0){
            defSize = withSpecSize>heightSpecSize?heightSpecSize:withSpecSize;
            setMeasuredDimension(defSize, defSize);
        }else {
            setMeasuredDimension(defWith, defHeight);
        }
//        if (withSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(defWith, defHeight);
//        } else if (withSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(defWith, heightSpecSize);
//        } else {
//            setMeasuredDimension(withSpecSize, defHeight);
//        }

    }
}