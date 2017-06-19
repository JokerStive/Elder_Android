package lilun.com.pensionlife.base;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
*添加header、footer快速集成的adapter
*@author yk
*create at 2017/2/13 15:48
*email : yk_developer@163.com
*/
public abstract class QuickMultiItemAdapter<T extends MultiItemEntity> extends BaseMultiItemQuickAdapter<T> {
    public QuickMultiItemAdapter(List<T> data) {
        super(data);
    }

//    public QuickMultiItemAdapter(int layoutResId, List<T> data) {
//
//        super(layoutResId, data);
//    }

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
