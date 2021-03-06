package lilun.com.pensionlife.ui.order.personal_detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.pay.Order;
import lilun.com.pensionlife.ui.agency.detail.ProductDetailFragment;
import lilun.com.pensionlife.ui.agency.detail.ProviderDetailFragment;
import lilun.com.pensionlife.ui.education.colleage_details.CollegeDetailFragment;
import lilun.com.pensionlife.ui.education.course_details.CourseDetailFragment;
import lilun.com.pensionlife.widget.CustomTextView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 订单详情V
 *
 * @author yk
 *         create at 2017/3/6 8:55
 *         email : yk_developer@163.com
 */

public class OrderDetailFragment extends BaseFragment<OrderDetailContract.Presenter> implements OrderDetailContract.View {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.tv_order_status)
    TextView tvOrderStatus;
    @Bind(R.id.tv_order_create_time)
    TextView tvOrderCreateTime;
    @Bind(R.id.tv_order_reservation_time)
    TextView tvOrderReservationTime;
    @Bind(R.id.tv_contact_mobile)
    TextView tvContactMobile;
    @Bind(R.id.tv_contact_name)
    TextView tvContactName;
    @Bind(R.id.tv_contact_address)
    TextView tvContactAddress;
    @Bind(R.id.tv_provider_name)
    TextView tvProviderName;
    @Bind(R.id.iv_product_icon)
    ImageView ivProductIcon;
    @Bind(R.id.tv_product_title)
    TextView tvProductTitle;
    @Bind(R.id.tv_product_area)
    TextView tvProductArea;
    @Bind(R.id.tv_order_count)
    TextView tvOrderCount;
    @Bind(R.id.tv_order_total_price)
    TextView tvOrderTotalPrice;
    @Bind(R.id.tv_order_price)
    TextView tvOrderPrice;
    @Bind(R.id.tv_cancel_order)
    CustomTextView tvCancelOrder;

    private String mOrderId;
    private String status_reserved = "reserved";
    private String status_assigned = "assigned";
    private String status_done = "done";
    private String status_cancel = "cancel";
    private String status_assessed = "assessed";
    private String status_delay = "delay";
    private ProductOrder mOrder;
    private String agencyId;
//    private String mNextStatus;

    public static OrderDetailFragment newInstance(String orderId) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putString("mOrderId", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mOrderId = arguments.getString("mOrderId");
        Preconditions.checkNull(mOrderId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new OrderDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(() -> getActivity().finish());
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getOrder(mOrderId);
    }


    @Override
    public void showOrder(ProductOrder order) {
        //show order
        this.mOrder = order;

        //订单信息
        int status = getStatus(order.getStatus());
        if (status != 0) {
            tvOrderStatus.setText(Html.fromHtml("订单状态: <font color='#fe620f'>" + App.context.getString(status) + "</font>"));
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        tvOrderCreateTime.setText("预约时间:" + StringUtils.IOS2ToUTC(order.getCreatedAt(), 7));
        tvOrderReservationTime.setText("服务时间:" + StringUtils.IOS2ToUTC(order.getRegisterDate(), format));

        //联系人信息
        Contact contact = order.getContact();
        if (contact != null) {
            tvContactMobile.setText(Html.fromHtml("手机号:<font color='#fe620f'>" + contact.getMobile() + "</font>"));

            tvContactName.setText("联系人:" + contact.getName());

            tvContactAddress.setText(contact.getAddress());
        }

        //根据订单状态下一步的操作
        int statusStringRes = getStatusOperate(order.getStatus());
        if (statusStringRes != -1) {
            tvCancelOrder.setVisibility(View.VISIBLE);
            tvCancelOrder.setText(statusStringRes);
        }


        //显示产品信息
        OrganizationProduct product = order.getProduct();
        if (product != null) {
            //服务提供商
            agencyId = StringUtils.removeSpecialSuffix(product.getOrganizationId());
            String agencyName = StringUtils.getOrganizationNameFromId(agencyId);
            tvProviderName.setText(StringUtils.filterNull(agencyName));

            //产品图
            String url = StringUtils.getFirstIcon(order.getProduct().getImage());
            ImageLoaderUtil.instance().loadImage(url, ivProductIcon);

            //产品名称
            tvProductTitle.setText(product.getTitle());

            //价格
            tvOrderTotalPrice.setText(Html.fromHtml("￥" + new DecimalFormat("######0.00").format(product.getPrice())));
            tvOrderPrice.setText(Html.fromHtml("￥" + new DecimalFormat("######0.00").format(product.getPrice())));

            String orgCategoryId = product.getOrgCategoryId();
            if (!TextUtils.isEmpty(orgCategoryId) && orgCategoryId.contains(Config.course_product_categoryId)) {
                tvProductArea.setText(showSemester(product.getExtend()));
            } else {
                tvProductArea.setText(String.format("服务范围: %1$s", StringUtils.getProductArea(product.getAreaIds())));
            }
        }
    }


    private String showSemester(Map<String, Object> extend) {
        //显示学期
        String semester = "无";
        if (extend != null) {
            String termStartDate = (String) extend.get("termStartDate");
            String termEndDate = (String) extend.get("termEndDate");
            if (!TextUtils.isEmpty(termStartDate) && !TextUtils.isEmpty(termEndDate)) {
                semester = "学期时间：" + StringUtils.IOS2ToUTC(termStartDate, 5) + "--" + StringUtils.IOS2ToUTC(termEndDate, 5);
            }
        }
        return semester;
    }


    @Override
    public void changeOrderStatusSuccess(int status) {
        //更改订单状态后的操作
        getActivity().finish();
        EventBus.getDefault().post(new Event.RefreshMyOrderData());

    }

    @Override
    public void reFundSuccess() {

    }

    private int getStatus(int status) {
        int stringRes = 0;
        if (status == Order.Status.reserved || status == Order.Status.payed) {
            stringRes = R.string.status_reserved;
        } else if (status == Order.Status.accepted) {
            stringRes = R.string.status_assigned;
        } else if (status == Order.Status.completed) {
            stringRes = R.string.status_done;
        } else if (status == Order.Status.canceled) {
            stringRes = R.string.status_cancel;
        } else if (status == Order.Status.assessed) {
            stringRes = R.string.status_assessed;
        } else if (status == Order.Status.delayed) {
            stringRes = R.string.status_delayed;
        }
        return stringRes;
    }


    private int


    getStatusOperate(int status) {
        int statusOperate = -1;
        if (status == Order.Status.reserved) {
            statusOperate = R.string.operate_cancel;
        } else if (status == Order.Status.accepted) {
            statusOperate = R.string.operate_done;
        }

//        else if (status.equals(status_done)) {
//            statusOperate = R.string.rank;
//        }

        else if (status == Order.Status.canceled) {
            statusOperate = -1;
        } else if (status == Order.Status.assessed) {
            setHadAssess();
        }
        return statusOperate;
    }


    private void setHadAssess() {
        tvCancelOrder.setBackgroundColor(_mActivity.getResources().getColor(R.color.yellowish));
        tvCancelOrder.setEnabled(false);
        tvCancelOrder.setText("已经评价");
    }

    @OnClick({R.id.tv_cancel_order, R.id.tv_provider_name, R.id.ll_product_icon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_order:
                operate();
                break;

            case R.id.tv_provider_name:
                OrganizationProduct product = mOrder.getProduct();
                if (product != null) {
                    String orgCategoryId = product.getOrgCategoryId();
                    String organizationIdSuffix = product.getOrganizationId();
                    String organizationId = StringUtils.removeSpecialSuffix(organizationIdSuffix);
                    if (orgCategoryId.contains(Config.course_product_categoryId)) {
                        start(CollegeDetailFragment.newInstance(organizationId));
                    } else {
                        start(ProviderDetailFragment.newInstance(organizationId));
                    }
                }
                break;

            case R.id.ll_product_icon:
                OrganizationProduct product1 = mOrder.getProduct();
                if (product1 != null) {
                    String id = product1.getId();
                    String orgCategoryId = product1.getOrgCategoryId();
                    if (TextUtils.isEmpty(orgCategoryId)) {
                        ToastHelper.get().showWareShort("产品脏数据");
                        return;
                    }
                    if (orgCategoryId.contains(Config.course_product_categoryId)) {
                        start(CourseDetailFragment.newInstance(id));
                    } else {
                        start(ProductDetailFragment.newInstance(id));
                    }
                }
                break;
        }
    }

    private void operate() {
//        int status = mOrder.getStatus();
//        if (status == Order.Status.reserved) {
//            changeOrderStatus(status_cancel, getAlartMsg(R.string.operate_cancel));
//        } else if (status == Order.Status.completed) {
//            changeOrderStatus(status_done, "服务已经完成？");
//        }

    }

    private void changeOrderStatus(int status, String msg) {
//        new NormalDialog().createNormal(_mActivity, msg, () -> {
//            mPresenter.changeOrderStatus(mOrderId);
//        });

    }

    public String getAlartMsg(int operate) {
        return "确定要" + getString(operate) + "吗？";
    }

    private void call(String msg) {
        String phoneDesc = tvContactMobile.getText().toString();
        if (!TextUtils.isEmpty(phoneDesc)) {
            boolean hasPermission = hasPermission(Manifest.permission.CALL_PHONE);
            if (hasPermission) {
                callPhone(msg);
            } else {
                requestPermission(Manifest.permission.CALL_PHONE, 0X11);
            }
        }
    }

    private void callPhone(String msg) {
        String mobile = tvContactMobile.getText().toString();
        new NormalDialog().createNormal(_mActivity, msg, () -> {
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mobile));
            startActivity(intent);
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastHelper.get().showShort("请给予权限");
            } else {
                callPhone("确定联系商家？");
            }
        }
    }


    @Subscribe
    public void afterRank(Event.AfterRank afterRank) {
        setHadAssess();
    }

}
