package lilun.com.pensionlife.module.adapter;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 个人订单adapter
 *
 * @author yk
 *         create at 2017/3/6 11:10
 *         email : yk_developer@163.com
 */
public class PersonalOrderAdapter extends QuickAdapter<ProductOrder> {

    private OnItemClickListener listener;

    public PersonalOrderAdapter(List<ProductOrder> data) {
        super(R.layout.item_personal_order, data);
    }


//    @Override
//    public int getItemCount() {
//        return Integer.MAX_VALUE;
//    }

    @Override
    protected void convert(BaseViewHolder helper, ProductOrder order) {
        OrganizationProduct product = order.getProduct();
        if (product != null) {

            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), null), R.drawable.icon_def, helper.getView(R.id.iv_product_icon));

            setOrderStatus(helper, order);
            setNextOperate(helper, order);
            String agencyName = StringUtils.getOrganizationNameFromId(StringUtils.removeSpecialSuffix(product.getOrganizationId()));
            helper.setText(R.id.tv_provider_name, agencyName)
                    .setText(R.id.tv_product_title, product.getTitle())
                    .setText(R.id.tv_reservation_time, "预约时间:" + StringUtils.IOS2ToUTC(order.getRegisterDate(), 0))
                    .setText(R.id.tv_product_price, Html.fromHtml("价格: <font color='#fe620f'>" + "￥" + new DecimalFormat("######0.00").format(product.getPrice()) + "</font>"))
                    .setOnClickListener(R.id.rl_item, v -> {
                        if (listener != null) {
                            listener.onItemClick(order);
                        }
                    })

                    .setOnClickListener(R.id.tv_next_operate, v -> {
                        if (listener != null) {
                            listener.nextOperate(order.getProductId());
                        }
                    })

            ;

        }
    }

    private void setNextOperate(BaseViewHolder helper, ProductOrder order) {
        TextView tvNextOperate = helper.getView(R.id.tv_next_operate);
        String status = order.getStatus();
        if (status.equals("reserved") || status.equals("delay")) {
            tvNextOperate.setVisibility(View.VISIBLE);
            tvNextOperate.setText("取消预约");
        }
    }

    private void setOrderStatus(BaseViewHolder helper, ProductOrder order) {
        TextView tvOrderStatus = helper.getView(R.id.tv_order_status);
        String status = order.getStatus();
        if (status.equals("reserved") || status.equals("delay")) {
            tvOrderStatus.setText("等待商家处理");
        } else if (status.equals("assigned")) {
            tvOrderStatus.setText("商家已经受理");
        } else if (status.equals("done")) {
            tvOrderStatus.setText("该订单已经完成");
        } else if (status.equals("cancel")) {
            tvOrderStatus.setText("该订单已经取消");
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductOrder order);

        void nextOperate(String productId);
    }
}
