package lilun.com.pension.module.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pension.R;
import lilun.com.pension.base.QuickAdapter;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;

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
        Contact userProfile = order.getUserProfile();
        helper.setText(R.id.tv_creator, String.format("预约人:%1$s", userProfile == null ? "" : userProfile.getName()));
        helper.setText(R.id.tv_sophisticated, order.getName())
                .setText(R.id.tv_health_status, StringUtils.getOrderStatusValue(order.getStatus()))
                .setText(R.id.tv_time, "预约时间：" + StringUtils.IOS2ToUTC(order.getRegisterDate(), 0))
                .setOnClickListener(R.id.tv_memo, v -> {
                    if (listener != null) {
                        listener.onMemo(order);
                    }
                })
                .setOnClickListener(R.id.tv_call, v -> {
                    if (listener != null && !TextUtils.isEmpty(order.getMobile())) {
                        listener.onCall(order);
                    } else {
                        ToastHelper.get().showWareShort("顾客电话为空");
                    }
                });
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onCall(ProductOrder order);

        void onMemo(ProductOrder order);
    }
}
