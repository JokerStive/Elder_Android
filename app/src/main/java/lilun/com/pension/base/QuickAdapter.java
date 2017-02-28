package lilun.com.pension.base;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
*添加header、footer快速集成的adapter
*@author yk
*create at 2017/2/13 15:48
*email : yk_developer@163.com
*/
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T> {

    public QuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public void clear(){
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

    public void addAllReverse(List<T> elements) {
        getData().addAll(0,elements);
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

    @Override
    public void setEmptyView(View emptyView) {
        super.setEmptyView(emptyView);
    }
}
