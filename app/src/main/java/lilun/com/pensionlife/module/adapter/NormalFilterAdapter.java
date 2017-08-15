package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.ConditionOption;
import lilun.com.pensionlife.module.bean.Option;

/**
 * @author yk
 *         create at 2017/3/28 9:49
 *         email : yk_developer@163.com
 */
public class NormalFilterAdapter extends QuickAdapter<Option> {

    //    private  String title;
    private String whereKey;
    private OnItemClickListener listener;
    private int mSelectedPosition = 0;

    public NormalFilterAdapter(ConditionOption conditionOption) {
        super(R.layout.item_condition_option, conditionOption.getVal());
        this.whereKey = conditionOption.getType();
//        this.title = conditionOption.getCondition();
    }

    @Override
    protected void convert(BaseViewHolder helper, Option option) {
        helper.setVisible(R.id.iv_gou, helper.getAdapterPosition() == mSelectedPosition);
        helper.setTextColor(R.id.tv_option, helper.getAdapterPosition() == mSelectedPosition ? App.context.getResources().getColor((R.color.filter_checked_color)) : App.context.getResources().getColor((R.color.filter_unchecked_color)));
        helper.setText(R.id.tv_option, option.getOptionValue())
                .setOnClickListener(R.id.ll_item, v -> {
                    if (listener != null) {
                        listener.onItemClick(helper.getAdapterPosition(), option.getOptionValue(), whereKey, option.getOptionKey());
                        // if (helper.getAdapterPosition() != 0) {  修复显示位置与选择位置不匹配问题
                        mSelectedPosition = helper.getAdapterPosition();
                        notifyDataChanged();
                        //  }
                    }
                });
    }

    public interface OnItemClickListener {
        void onItemClick(int adapterPosition, String title, String whereKey, String whereValue);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
