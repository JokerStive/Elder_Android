package lilun.com.pensionlife.ui.order.personal_detail;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.base.BaseChatFragment;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.pay.Cashier;
import lilun.com.pensionlife.pay.Order;
import lilun.com.pensionlife.pay.PayCallBack;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.ui.contact.ContactDetailFragment;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

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
            tvContactName, tvContactMobile, tvContactAddress, tvReFoundDesc, tvCountDown;
    ImageView ivOrderImg;
    private Button btnCancelOrder;
    private LinearLayout llContactInfo;
    private RelativeLayout rlProductInfo;
    private View inflate;
    private String metaServiceContactId;
    private long invalidTime;
    private String paymentMethod = Order.paymentMethods.alipay;

    private CompositeSubscription countDownSubscription = new CompositeSubscription();


    /**
     * 根据订单状态的不同，可以进行不同的操作
     * -1 无，0 付款 1退款 2取消订单
     */
    private int doSome = -1;
    private Cashier cashier;

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
        refresh();
    }

    @Override
    public void inflateProductView() {
        chatAdapter.setHeaderView(setProductView());
        chatAdapter.setChatType(1);
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
        tvCountDown = (TextView) inflate.findViewById(R.id.tv_count_down);
        tvReFoundDesc = (TextView) inflate.findViewById(R.id.tv_reFound_desc);
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
        tvOrderCreateTime.setText("下单时间:" + StringUtils.IOS2ToUTC(order.getCreatedAt(), 8));

        tvOrderStatus.setText(getStatusEn2Cn(order.getStatus()));

        differentShowAndOperateFromOrderStatus(order);
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
        String registerDate = order.getRegisterDate();
        tvOrderRegisterTime.setVisibility(TextUtils.isEmpty(registerDate) ? View.INVISIBLE : View.VISIBLE);
        tvOrderRegisterTime.setText("预约时间:" + StringUtils.IOS2ToUTC(order.getRegisterDate(), 0));
        tvProdcutNumber.setText("x 1");
        if (order.getContact() != null) {
            tvContactName.setText("收货人：" + order.getContact().getCreatorName());
            tvContactMobile.setText("电话：" + order.getContact().getMobile());
            tvContactAddress.setText("地址：" + order.getContact().getAddress());
            llContactInfo.setOnClickListener((v) -> {
                start(ContactDetailFragment.newInstance(order.getMetaServiceContact(), order.getContact()));
            });
        }
        btnCancelOrder.setOnClickListener((v) -> {
            doSome();
        });
    }


    //根据不同的订单状态，不同的操作
    private void doSome() {
        switch (doSome) {
            //付款
            case 0:
                pay();
                break;
            //退款
            case 1:
                reFound();
                break;
            //取消订单
            case 2:
                cancelOrder();
                break;
        }


    }

    private void cancelOrder() {
        new NormalDialog().createNormal(_mActivity, "确定要取消订单吗？", () -> {
            mPresenter.changeOrderStatus(order.getId(), Order.Status.canceled);
        });
    }

    private void reFound() {
        new NormalDialog().createNormal(_mActivity, "确定要申请退款吗？", () -> {
            ToastHelper.get().showWareShort("申请退款");
        });
    }

    private void pay() {
        if (cashier == null) {
            ArrayList<String> paymentMethods = new ArrayList<>();
            paymentMethods.add(Order.paymentMethods.alipay);
            paymentMethods.add(Order.paymentMethods.weixin);
            cashier = Cashier.newInstance(order.getId(), order.getPrice().toString(), paymentMethods);
        }
        cashier.setPayCallBack(new PayCallBack() {
            @Override
            public void paySuccess() {
                refresh();
            }
        });
        cashier.show(_mActivity.getFragmentManager(), null);
    }

    //刷新界面
    private void refresh() {
        mPresenter.getOrder(objectId);
    }


    private void differentShowAndOperateFromOrderStatus(ProductOrder order) {
        int status = order.getStatus();
        Integer paid = order.getPaid();

        //支付式订单
        if (paid != null) {

            //待付款状态
            if (paid == Order.Paid.unpaid) {
                doSome = 0;
                setCountDown(order);
                tvReFoundDesc.setVisibility(View.GONE);
                btnCancelOrder.setText("付款");
            }

            //已经付款状态下，如果是线下付款和预约式订单一致
            if (paid == Order.Paid.paid && (status != Order.Status.completed)) {
                setPayMemo();
                if (TextUtils.equals(paymentMethod, Order.paymentMethods.offline)) {
                    doSome = 2;
                    btnCancelOrder.setText("取消订单");
                } else {
                    doSome = 1;
                    btnCancelOrder.setText("申请退款");
                }
            }


            //已经退款
            if (paid == Order.Paid.refunded) {
                btnCancelOrder.setVisibility(View.GONE);
                tvReFoundDesc.setText("商家已退款");
            }

            //正在退款中
            if (paid == Order.Paid.refunding) {
                //商家拒绝退款
                if (status == Order.Status.refused) {
                    tvReFoundDesc.setText("商家拒绝退款，请联系商家");
                    doSome = 1;
                    btnCancelOrder.setText("申请退款");
                } else {
                    btnCancelOrder.setVisibility(View.GONE);
                    tvReFoundDesc.setText("退款已申请，等待商家处理");
                }
            }

        }

        //预约式订单
        else {
            if ((status == Order.Status.reserved || status == Order.Status.accepted)) {
                doSome = 2;
                btnCancelOrder.setText("取消订单");
            }
        }
    }

    /**
     * 显示用什么方式支付的
     */
    private void setPayMemo() {
        if (TextUtils.equals(paymentMethod, Order.paymentMethods.alipay)) {
            tvReFoundDesc.setCompoundDrawablePadding(UIUtils.dp2px(App.context, 10));
            tvReFoundDesc.setCompoundDrawables(App.context.getResources().getDrawable(R.drawable.icon_ali_pay), null, null, null);
            tvReFoundDesc.setText(String.format("已经通过支付宝付款%1s元", order.getPrice()));
        } else if (TextUtils.equals(paymentMethod, Order.paymentMethods.weixin)) {
            tvReFoundDesc.setCompoundDrawablePadding(UIUtils.dp2px(App.context, 10));
            tvReFoundDesc.setCompoundDrawables(App.context.getResources().getDrawable(R.drawable.icon_wx_pay), null, null, null);
            tvReFoundDesc.setText(String.format("已经通过微信付款%1s元", order.getPrice()));
        } else if (TextUtils.equals(paymentMethod, Order.paymentMethods.offline)) {
            tvReFoundDesc.setText("采用线下支付方式付款");
        }
    }

    private void setCountDown(ProductOrder order) {
        Date date = StringUtils.IOS2ToUTCDate(order.getCreatedAt());
        assert date != null;
        invalidTime = date.getTime() + Order.Paid.invalid_time;
        long currentTime = new Date().getTime();
        long leftTimeSec = (invalidTime - currentTime) / 1000;
        if (leftTimeSec <= 0) return;
        final long finalLeftTime = leftTimeSec;
        tvCountDown.setVisibility(View.VISIBLE);
        countDownSubscription.add(Observable.interval(0, 1, TimeUnit.SECONDS)
                .take((int) leftTimeSec + 1)
                .map(aLong -> finalLeftTime - aLong)
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Long>() {
                    @Override
                    public void _next(Long leftTimeSecond) {
                        long day = leftTimeSecond / (24 * 60 * 60);
                        long hour = (leftTimeSecond / (60 * 60) - day * 24);
                        long min = ((leftTimeSecond / 60) - day * 24 * 60 - hour * 60);
                        long second = (leftTimeSecond - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        tvCountDown.setText("剩余支付时间" + String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", second));
                    }
                }));

    }

    @Override
    public void changeOrderStatusSuccess(int status) {
        refresh();
    }

    /**
     * 转换为中文状态
     */
    public String getStatusEn2Cn(int status) {
        String statusCn = "";
        switch (status) {
            case Order.Status.reserved:
            case Order.Status.payed:
                statusCn = "待受理";
                break;
            case Order.Status.accepted:
            case Order.Status.refused:
                statusCn = "已受理";
                break;
            case Order.Status.completed:
            case Order.Status.assessed:
                statusCn = "已完成";
                break;
            case Order.Status.canceled:
                statusCn = "已取消";
                break;
        }
        return statusCn;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownSubscription != null && !countDownSubscription.isUnsubscribed()) {
            countDownSubscription.unsubscribe();
            countDownSubscription.clear();
            countDownSubscription = null;
        }
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


}
