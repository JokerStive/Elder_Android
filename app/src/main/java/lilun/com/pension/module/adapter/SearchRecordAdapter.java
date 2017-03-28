package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;

/**
 * @author yk
 *         create at 2017/3/28 9:49
 *         email : yk_developer@163.com
 */
public class SearchRecordAdapter extends QuickAdapter<String> {

    private OnItemClickListener listener;

    public SearchRecordAdapter(List<String> data) {
        super(R.layout.item_search_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String str) {
        helper.setText(R.id.tv_record,str)
                .setOnClickListener(R.id.ll_item, v -> {
                    if (listener != null) {
                        listener.onItemClick(str);
                    }
                });
    }

    public interface OnItemClickListener {
        void onItemClick(String str);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
