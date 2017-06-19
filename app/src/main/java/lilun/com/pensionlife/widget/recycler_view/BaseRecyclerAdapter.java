package lilun.com.pensionlife.widget.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yk on 2017/1/10.
 * 添加头部header和底部footer的adapter
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    private final int TYPE_HEADER = 0;
    private final int TYPE_FOOTER = 1;
    private final int TYPE_NORMAL = 2;
    protected List<T> data;
    private View mHeaderView;
    private View mFooterView;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new Holder(mHeaderView);
        }

        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new Holder(mFooterView);
        }

        return new Holder(getItemLayout());
    }


    /**
     * ====================子类需要实现的方法===============
     */
    protected abstract View getItemLayout();


    protected abstract void bindItemView(View itemView, int position);

    /**
     * ===================================
     */


    /**
     * ====================公共方法===============
     */
    public void setHeader(View header) {
        this.mHeaderView = header;
    }


    public void setFooter(View footer) {
        this.mFooterView = footer;
    }

    /**
     * ===================================
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        if (getItemViewType(position) == TYPE_FOOTER) return;
        if (mHeaderView != null) {
            bindItemView(holder.itemView, position - 1);
        } else {
            bindItemView(holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView != null && mFooterView != null) {
            return data.size() + 2;
        } else if (mHeaderView != null) {
            return data.size() + 1;
        } else if (mFooterView != null) {
            return data.size() + 1;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == data.size() + 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        public View itemView;

        public Holder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) return;
            if (itemView == mFooterView) return;
            this.itemView = itemView;
        }
    }
}
