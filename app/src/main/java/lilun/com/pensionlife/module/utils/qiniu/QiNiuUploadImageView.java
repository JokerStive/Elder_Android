package lilun.com.pensionlife.module.utils.qiniu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;


public class QiNiuUploadImageView extends ImageView {
    private Paint mPaint;
    private int mProgress;

    public QiNiuUploadImageView(Context context) {
        super(context);
        init();
    }

    public QiNiuUploadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        setProgress(100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#70000000"));// 半透明
        canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * mProgress
                , mPaint);// 绘制上半部分的半透明矩形区域（左上，右下，画笔）

        mPaint.setColor(Color.parseColor("#00000000"));// 全透明
        canvas.drawRect(0, getHeight() - getHeight() * mProgress,getWidth(), getHeight(), mPaint);// 绘制下半部分的全透明矩形区域
    }

    public void

    setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();//刷新界面，重新运行onDraw()方法
    }
}
