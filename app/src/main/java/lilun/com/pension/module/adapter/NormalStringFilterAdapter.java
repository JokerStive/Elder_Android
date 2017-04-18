package lilun.com.pension.module.adapter;

import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Area;

/**
 * @author yk
 *         create at 2017/3/28 9:49
 *         email : yk_developer@163.com
 */
public class NormalStringFilterAdapter extends QuickAdapter<Area> {

    private OnItemClickListener listener;
    private int mSelectedPosition = -1;


    public NormalStringFilterAdapter(List<Area> data) {
        super(R.layout.item_condition_option, data);
    }

    public void resetSelectedPosition(){
        mSelectedPosition=-1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Area area) {
        helper.setTextColor(R.id.tv_option, helper.getAdapterPosition() == mSelectedPosition ? App.context.getResources().getColor((R.color.red)) : Color.BLACK);
        helper.setText(R.id.tv_option, area.getName())
                .setOnClickListener(R.id.ll_item, v -> {
                    if (listener != null && mSelectedPosition != helper.getAdapterPosition()) {
                        String replace=null;
                        String id = area.getId();
                        if (!TextUtils.isEmpty(id)){
                            replace = id.replace(App.context.getResources().getString(R.string.common_address), "");
                            Logger.d(replace);
                        }
                        listener.onItemClick(helper.getAdapterPosition(), replace);
                        mSelectedPosition = helper.getAdapterPosition();
                        notifyDataChanged();
                    }
                });
    }

    public interface OnItemClickListener {
        void onItemClick(int adapterPosition, String option);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
