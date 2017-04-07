package lilun.com.pension.module.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 用户预约登记信息adapter
 *
 * @author yk
 *         create at 2017/4/5 13:26
 *         email : yk_developer@163.com
 */
public class MerchantOrderAdapter extends QuickAdapter<ProductOrder> {
    private OnItemClickListener listener;

    public MerchantOrderAdapter(List<ProductOrder> data) {
        super(R.layout.item_merchant_order, data);
    }

    public MerchantOrderAdapter(int layoutResId, List<ProductOrder> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductOrder order) {
        helper.setText(R.id.tv_provider_name, order.getName())
                .setText(R.id.tv_health_status, order.getStatus())
                .setText(R.id.tv_creator, "下单人:"+order.getCreatorName())
                .setText(R.id.tv_time, "下单时间:"+StringUtils.IOS2ToUTC(order.getCreatedAt(), 0))
                .setOnClickListener(R.id.tv_memo, v -> {
                    if (listener != null) {
                        listener.onMemo();
                    }
                })
                .setOnClickListener(R.id.tv_call, v -> {
                    if (listener != null && !TextUtils.isEmpty(order.getMobile())) {
                        String mobile = order.getMobile();
                        listener.onCall(mobile);
                    }
                });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onCall(String  phone);

        void onMemo();
    }
}
