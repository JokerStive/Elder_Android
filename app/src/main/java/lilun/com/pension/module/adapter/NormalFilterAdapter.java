package lilun.com.pension.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ConditionOption;

/**
 * @author yk
 *         create at 2017/3/28 9:49
 *         email : yk_developer@163.com
 */
public class NormalFilterAdapter extends QuickAdapter<ConditionOption> {

    private OnItemClickListener listener;

    public NormalFilterAdapter(List<ConditionOption> data) {
        super(R.layout.item_condition_option, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConditionOption conditionOption) {
        helper.setText(R.id.tv_option, conditionOption.getConditionValue())
                .setOnClickListener(R.id.ll_item, v -> {
                    if (listener != null) {
                        listener.onItemClick(helper.getAdapterPosition(),conditionOption);
                    }
                });
    }

    public interface OnItemClickListener {
        void onItemClick(int adapterPosition, ConditionOption option);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
