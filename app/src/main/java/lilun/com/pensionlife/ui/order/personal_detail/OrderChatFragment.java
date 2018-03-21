package lilun.com.pensionlife.ui.order.personal_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseChatFragment;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.ToastHelper;

/**
 * 互动式订单详情聊天室
 * 需要向聊天室添加产品信息卡片 {@link OrderChatFragment#inflateProductView()}
 * <p>
 * <p>
 * Created by zp on 2017/5/3.
 */

public class OrderChatFragment extends BaseChatFragment {
    ProductOrder order;
    static String Arg_Order = "order";

    /**
     * 互动式聊天室
     *
     * @return
     */
    public static OrderChatFragment newInstance(ProductOrder order) {
        OrderChatFragment fragment = new OrderChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(Arg_Order, order);
        args.putSerializable(Arg_ChatType, ChatType_Interactive);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 获取产品id，订阅产品所在组织的聊天频道
     * 注意topic----eg ： 订单主题：/社会组织/营利/商家/赵丽颖/#order/orderId/.chat
     * 订单不挂组织，但是其关联的产品挂组织，因此可以取产品的组织
     *
     * @param arguments
     */
    @Override
    protected void getTransferData(Bundle arguments) {
        order = (ProductOrder) arguments.getSerializable(Arg_Order);
        chatType = (int) arguments.getSerializable(Arg_ChatType);

        if (order == null) try {
            throw new Throwable("OrganizationProduct must not be null");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
        objectId = order.getId();
        orgId = order.getProductBackup().getOrganizationId();
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("订单详情");
        super.initView(inflater);
    }

    @Override
    public void inflateProductView() {
        chatAdapter.setHeaderView(setProductView());
    }

    /**
     * 设置产品信息界面,
     *
     * @return
     */
    public View setProductView() {
        TextView tvOrderCreateTime, tvOrderStatus, tvOrderProductTitle, tvOrderPrice,
                tvOrderRegisterTime, tvProdcutNumber, tvOrderTotalPrice,
                tvContactName, tvContactMobile, tvContactAddress;
        ImageView ivOrderImg;

        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_product_info_layout, null, false);
        Button cancelOrder = (Button) inflate.findViewById(R.id.btn_cancel_order);
        LinearLayout llContactInfo = (LinearLayout) inflate.findViewById(R.id.ll_contact_info);
        RelativeLayout rlProductInfo = (RelativeLayout) inflate.findViewById(R.id.rl_product_info);
        tvOrderCreateTime = (TextView) inflate.findViewById(R.id.tv_order_create_time);
        tvOrderStatus = (TextView) inflate.findViewById(R.id.tv_order_status);
        tvOrderProductTitle = (TextView) inflate.findViewById(R.id.tv_order_product_title);
        tvOrderPrice = (TextView) inflate.findViewById(R.id.tv_order_price);
        tvOrderRegisterTime = (TextView) inflate.findViewById(R.id.tv_order_register_time);
        tvProdcutNumber = (TextView) inflate.findViewById(R.id.tv_prodcut_number);
        tvOrderTotalPrice = (TextView) inflate.findViewById(R.id.tv_order_total_price);
        tvContactName = (TextView) inflate.findViewById(R.id.tv_contact_name);
        tvContactMobile = (TextView) inflate.findViewById(R.id.tv_contact_mobile);
        tvContactAddress = (TextView) inflate.findViewById(R.id.tv_contact_address);
        ivOrderImg = (ImageView) inflate.findViewById(R.id.iv_order_img);
        tvOrderCreateTime.setText("预约时间:2018-01-24 20:14:27");
        tvOrderStatus.setText("待处理");
        tvOrderProductTitle.setText("英孚教育-四点半课堂1111111111111111");
        tvOrderPrice.setText("￥53.00");
        tvOrderRegisterTime.setText("服务时间:2018-01-24");
        tvProdcutNumber.setText("x 1");
        tvOrderTotalPrice.setText(Html.fromHtml("共 <font color=\"red\">1</font>件商品，合计: <font color=\"red\">￥53.00</font>"));
        tvContactName.setText("收货人：张三");
        tvContactMobile.setText("电话：185****8537");
        tvContactAddress.setText("地址：重庆 重庆市 南岸区长生桥镇茶园 新区城南家园六组团2栋三单元2-1");
        cancelOrder.setOnClickListener((v) -> {
            ToastHelper.get().showShort("canceled !");
        });
        llContactInfo.setOnClickListener((v) -> {
            ToastHelper.get().showShort("联系人!");
        });
        rlProductInfo.setOnClickListener((v) -> {
            ToastHelper.get().showShort("产品信息!");
        });
        return inflate;
    }

}
