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

import java.text.DecimalFormat;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseChatFragment;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.ui.contact.ContactDetailFragment;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 互动式订单详情聊天室
 * 需要向聊天室添加产品信息卡片 {@link OrderChatFragment#inflateProductView()}
 * <p>
 * <p>
 * Created by zp on 2017/5/3.
 */

public class OrderChatFragment extends BaseChatFragment<OrderDetailContract.Presenter> implements OrderDetailContract.View {
    ProductOrder order;
    static String Arg_Order = "order";
    TextView tvOrderCreateTime, tvOrderStatus, tvOrderProductTitle, tvOrderPrice,
            tvOrderRegisterTime, tvProdcutNumber, tvOrderTotalPrice,
            tvContactName, tvContactMobile, tvContactAddress;
    ImageView ivOrderImg;
    private Button btnCancelOrder;
    private LinearLayout llContactInfo;
    private RelativeLayout rlProductInfo;
    private View inflate;
    private String metaServiceContactId;

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
    protected void initPresenter() {
        mPresenter = new OrderDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setTitle("订单详情");
        super.initView(inflater);
        ((OrderDetailPresenter) mPresenter).getOrder(objectId);
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
        inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_product_info_layout, null, false);
        inflate.setVisibility(View.GONE);
        btnCancelOrder = (Button) inflate.findViewById(R.id.btn_cancel_order);
        llContactInfo = (LinearLayout) inflate.findViewById(R.id.ll_contact_info);
        rlProductInfo = (RelativeLayout) inflate.findViewById(R.id.rl_product_info);
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
        return inflate;
    }

    @Override
    public void showOrder(ProductOrder order) {
        this.order = order;
        inflate.setVisibility(View.VISIBLE);
        tvOrderCreateTime.setText("预约时间:" + StringUtils.IOS2ToUTC(order.getCreatedAt()));

        tvOrderStatus.setText(getStatusEn2Cn(order.getStatus()));

        btnCancelOrder.setVisibility("cancel".equals(order.getStatus()) ? View.GONE : View.VISIBLE);

        if (order.getProductBackup() != null) {
            tvOrderProductTitle.setText(order.getProductBackup().getTitle());
            tvOrderPrice.setText("￥" + order.getProductBackup().getPrice());
            tvOrderTotalPrice.setText(Html.fromHtml(getOrderTotalHtml(1, order.getProductBackup().getPrice().floatValue())));
            ivOrderImg.setBackgroundColor(getResources().getColor(R.color.gray));
            ImageLoaderUtil.instance().loadImage(order.getProductImage(), ivOrderImg);
            rlProductInfo.setOnClickListener((v) -> {
                start(ProductDetailFragment.newInstance(order.getProductId()));
            });
            metaServiceContactId = order.getProductBackup().getMetaServiceContactId();
        }
        tvOrderRegisterTime.setText("服务时间:2018-01-24" + StringUtils.IOS2ToUTC(order.getRegisterDate(), 0));
        tvProdcutNumber.setText("x 1");
        if (order.getContact() != null) {
            tvContactName.setText("收货人：" + order.getContact().getCreatorName());
            tvContactMobile.setText("电话：" + order.getContact().getMobile());
            tvContactAddress.setText("地址：" + order.getContact().getAddress());
            llContactInfo.setOnClickListener((v) -> {
                start(ContactDetailFragment.newInstance(metaServiceContactId, order.getContact()));
            });
        }
        btnCancelOrder.setOnClickListener((v) -> {
            new NormalDialog().createNormal(_mActivity, getAlartMsg(R.string.operate_cancel), () -> {
                ((OrderDetailContract.Presenter) mPresenter).cancelOrderStatus(objectId);
            });

        });


    }

    @Override
    public void changeOrderStatusSuccess(String status) {
        order.setStatus(status);
        tvOrderStatus.setText(getStatusEn2Cn(status));
    }

    /**
     * 转换为中文状态
     * reserved:已预约;assigned:已受理;done:已完成;cancel:已取消
     */
    public String getStatusEn2Cn(String status) {
        if (status == null) return null;
        String statusCn = "";
        switch (status) {
            case "reserved":
                statusCn = "已预约";
                break;
            case "assigned":
                statusCn = "已受理";
                break;
            case "done":
                statusCn = "已完成";
                break;
            case "cancel":
                statusCn = "已取消";
                break;
        }
        return statusCn;
    }

    /**
     * 获取订单总价格html文字
     *
     * @return
     */
    public String getOrderTotalHtml(int number, float price) {
        String format = new DecimalFormat("#.00").format(number * price);
        return "共 <font color=\"red\">" + number + "</font>件商品，合计: <font color=\"red\">￥" + format + "</font>";
    }

    /**
     * 返回 确定要 xxx 吗？"
     *
     * @param operate
     * @return
     */
    public String getAlartMsg(int operate) {
        return "确定要" + getString(operate) + "吗？";
    }
}
