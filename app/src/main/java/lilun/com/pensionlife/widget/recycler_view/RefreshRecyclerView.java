package lilun.com.pensionlife.widget.recycler_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yk on 2017/1/10.
 * 下拉刷新，上拉加载更多的recyclerView
 */
public class RefreshRecyclerView extends RecyclerView {

    private BaseRecyclerAdapter mAdapter;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!(getAdapter() instanceof BaseRecyclerAdapter)) {
            throw new RuntimeException("the adapter must extends RefreshRecyclerAdapter");
        } else {
            mAdapter = (BaseRecyclerAdapter) getAdapter();
            //do some thing

        }

    }



}
