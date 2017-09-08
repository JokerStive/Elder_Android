package lilun.com.pensionlife.base;

import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;

/**
 * 添加header、footer快速集成的adapter
 * mScrollIdle标志 滑动过程中不加载图片，避免出现大量的异步任务，从而线程池拥堵
 *
 * @author yk
 *         create at 2017/2/13 15:48
 *         email : yk_developer@163.com
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public boolean mScrollIdle = true;

    public void setmScrollIdle(boolean mScrollIdle) {
        this.mScrollIdle = mScrollIdle;
    }

    public QuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }


    public void setEmptyView() {
        setEmptyView(getEmptyView());
    }

    public View getEmptyView() {
        return LayoutInflater.from(App.context).inflate(R.layout.empty_data, null);
    }

    public void clear() {
        getData().clear();
        notifyDataChanged();
    }

    public void replaceAll(List<T> elements) {
        setNewData(elements);
        if (isLoadMoreEnable() && elements.size()<Config.defLoadDatCount){
            loadMoreEnd();
        }
    }


    public void addAll(List<T> elements) {
        addData(elements);
    }

    public void addAll(List<T> elements, int defDataCount) {
        addData(elements);
        if (isLoadMoreEnable()) {
            loadMoreComplete();
        }
        setIsLoadMoreEnd(elements, defDataCount);
    }

    /**
     * @param isDefDataCount 每次加载更多是否是默认的数据量
     */
    public void addAll(List<T> elements, boolean isDefDataCount) {
        addData(elements);
        if (isLoadMoreEnable()) {
            loadMoreComplete();
        }
        if (isDefDataCount) {
            setIsLoadMoreEnd(elements, Config.defLoadDatCount);
        }
    }

    private void setIsLoadMoreEnd(List<T> elements, int defCount) {
        if (elements.size() < defCount) {
            loadMoreEnd();
        }
    }


    public void add(T element) {
        getData().add(element);
        notifyDataSetChanged();
    }

    public void remove(T element) {
        getData().remove(element);
        notifyDataSetChanged();
    }

    public void addAllReverse(List<T> elements) {
        getData().addAll(0, elements);
        notifyDataSetChanged();
    }


    public void notifyDataChanged() {
        super.notifyDataSetChanged();
        if (getData().size() == 0) {
            onEmptyData();
        } else {
            onHasData();
        }
    }

    private void onHasData() {

    }

    private void onEmptyData() {

    }


}
