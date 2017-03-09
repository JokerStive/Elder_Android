//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package lilun.com.pension.module.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import lilun.com.pension.widget.CardConfig;

public class MyCallBack extends SimpleCallback {
    protected RecyclerView mRv;
    protected BaseQuickAdapter mAdapter;

    public MyCallBack(RecyclerView rv, BaseQuickAdapter adapter) {
        this(0, 15, rv, adapter);
    }

    public MyCallBack(int dragDirs, int swipeDirs, RecyclerView rv,BaseQuickAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.mRv = rv;
        this.mAdapter = adapter;
    }


    public float getThreshold(ViewHolder viewHolder) {
        return (float)this.mRv.getWidth() * this.getSwipeThreshold(viewHolder);
    }

    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSwiped(ViewHolder viewHolder, int direction) {
        if (mAdapter.getItemCount()!=0){
            mAdapter.remove(mAdapter.getItemCount()-1);
            if (mAdapter.getItemCount()==0){
                Logger.d("推送栏设置gone");
                mRv.setVisibility(View.GONE);
            }
        }

    }




    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        double swipValue = Math.sqrt((double)(dX * dX + dY * dY));
        double fraction = swipValue / (double)this.getThreshold(viewHolder);
        if(fraction > 1.0D) {
            fraction = 1.0D;
        }

        int childCount = recyclerView.getChildCount();

        if (childCount==0){
            recyclerView.setVisibility(View.GONE);
            return;
        }
        for(int i = 0; i < childCount; ++i) {
            View child = recyclerView.getChildAt(i);
            int level = childCount - i - 1;
            if(level > 0) {
                child.setScaleX((float)((double)(1.0F - CardConfig.SCALE_GAP * (float)level) + fraction * (double)CardConfig.SCALE_GAP));
                if(level < CardConfig.MAX_SHOW_COUNT - 1) {
                    child.setScaleY((float)((double)(1.0F - CardConfig.SCALE_GAP * (float)level) + fraction * (double)CardConfig.SCALE_GAP));
                    child.setTranslationY((float)((double)(CardConfig.TRANS_Y_GAP * level) - fraction * (double)CardConfig.TRANS_Y_GAP));
                }
            }
        }

    }
}
