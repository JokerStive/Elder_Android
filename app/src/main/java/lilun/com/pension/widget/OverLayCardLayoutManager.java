//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lilun.com.pension.widget;

import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.Recycler;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.View;

public class OverLayCardLayoutManager extends LayoutManager {
    private static final String TAG = "swipecard";
    private   float initScaleY = 10f;

    public OverLayCardLayoutManager() {
    }

    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        Log.e("swipecard", "onLayoutChildren() called with: recycler = [" + recycler + "], state = [" + state + "]");
        this.detachAndScrapAttachedViews(recycler);
        int itemCount = this.getItemCount();
        if(itemCount >= 1) {
            int bottomPosition;
            if(itemCount < CardConfig.MAX_SHOW_COUNT) {
                bottomPosition = 0;
            } else {
                bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
            }

            for(int position = bottomPosition; position < itemCount; ++position) {
                View view = recycler.getViewForPosition(position);
                this.addView(view);
                this.measureChildWithMargins(view, 0, 0);
                int widthSpace = this.getWidth() - this.getDecoratedMeasuredWidth(view);
                int heightSpace = this.getHeight() - this.getDecoratedMeasuredHeight(view);
                this.layoutDecoratedWithMargins(view, widthSpace / 2,0, widthSpace / 2 + this.getDecoratedMeasuredWidth(view), this.getDecoratedMeasuredHeight(view));
                int level = itemCount - position - 1;
                if(level>0) {
                    view.setScaleX(1.0F - CardConfig.SCALE_GAP * (float)level);
                    if(level < CardConfig.MAX_SHOW_COUNT - 1) {
                        view.setTranslationY((float)(CardConfig.TRANS_Y_GAP * level));
                        view.setScaleY(initScaleY - CardConfig.SCALE_GAP * (float)level);
                    } else {
                        view.setTranslationY((float)(CardConfig.TRANS_Y_GAP * (level - 1)));
                        view.setScaleY(initScaleY- CardConfig.SCALE_GAP * (float)(level - 1));
                    }
                }
            }

        }
    }
}
