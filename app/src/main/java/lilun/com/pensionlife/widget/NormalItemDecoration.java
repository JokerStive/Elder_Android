package lilun.com.pensionlife.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.module.utils.UIUtils;

public class NormalItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 1;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL

    public NormalItemDecoration(int topButtomSpan) {
        this.space = UIUtils.dp2px(App.context, topButtomSpan);
    }
    public NormalItemDecoration(Context context , int topButtomSpan){
        this.space = UIUtils.dp2px(context, topButtomSpan);
    }

    /**
     * 自定义分割线
     *
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public NormalItemDecoration(int dividerHeight, int dividerColor) {
        mDividerHeight = dividerHeight;
        space = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mPaint != null)
            drawVertical(c, parent);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
      //  int position = parent.getChildAdapterPosition(view);
        if (parent.getAdapter().getItemCount() <= 1) {
            return;
        }

        outRect.bottom = space;
      //  outRect.set(0, 0, 0, mDividerHeight);
        super.getItemOffsets(outRect, view, parent, state);
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}