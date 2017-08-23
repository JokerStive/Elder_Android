package lilun.com.pensionlife.base;

import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;

/**
 * 添加header、footer快速集成的adapter
 * mScrollIdle标志 滑动过程中不加载图片，避免出现大量的异步任务，从而线程池拥堵
 * @author yk
 *         create at 2017/2/13 15:48
 *         email : yk_developer@163.com
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T> {
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
        if (getData().size() > 0) {
            getData().clear();
        }
        getData().addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elements) {
        getData().addAll(elements);
        notifyDataSetChanged();
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
