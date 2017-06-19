package lilun.com.pensionlife.ui.residential.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.ui.agency.detail.AgencyDetailFragment;
import lilun.com.pensionlife.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pensionlife.ui.help.RankFragment;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 订单详情V
 *
 * @author yk
 *         create at 2017/3/6 8:55
 *         email : yk_developer@163.com
 */

public class OrderDetailFragment extends BaseFragment<OrderDetailContract.Presenter> implements OrderDetailContract.View, View.OnClickListener {


    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Bind(R.id.tv_order_status)
    TextView tvOrderStatus;


    @Bind(R.id.tv_order_name)
    TextView tvOrderName;

    @Bind(R.id.tv_order_address)
    TextView tvOrderAddress;

    @Bind(R.id.iv_provider_avatar)
    ImageView ivProviderAvatar;

    @Bind(R.id.tv_provider_name)
    TextView tvProviderName;

    @Bind(R.id.iv_product_icon)
    ImageView ivProductIcon;

    @Bind(R.id.tv_sophisticated)
    TextView tvProductName;

    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;

    @Bind(R.id.rb_product)
    RatingBar rbProduct;

    @Bind(R.id.tv_product_phone)
    TextView tvProductPhone;

    @Bind(R.id.rl_product_phone)
    RelativeLayout rlProductPhone;

    @Bind(R.id.tv_operate)
    TextView tvOperate;

    @Bind(R.id.rl_provider_detail)
    RelativeLayout rlProviderDetail;

    @Bind(R.id.rl_product_detail)
    RelativeLayout rlProductDetail;

    @Bind(R.id.ll_operate)
    LinearLayout llOperate;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

    private String mOrderId;
    private String status_reserved = "reserved";
    private String status_assigned = "assigned";
    private String status_done = "done";
    private String status_cancel = "cancel";
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
        UIUtils.setBold(tvProviderName);
        UIUtils.setBold(tvOperate);
        UIUtils.setBold(tvCancel);


        ivBack.setOnClickListener(this);
        tvOperate.setOnClickListener(this);
        rlProductPhone.setOnClickListener(this);
        rlProductDetail.setOnClickListener(this);
        rlProviderDetail.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
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
        int status = getStatus(order.getStatus());
        if (status != 0) {
            tvOrderStatus.setText(status);
        }

        int statusStringRes= getStatusOperate(order.getStatus());
        if (statusStringRes!=-1){
            llOperate.setVisibility(View.VISIBLE);
            tvOperate.setText(statusStringRes);
        }



        //show product
        OrganizationProduct product = order.getProduct();
        Account account = order.getAssignee();

        if (product == null || account == null) {
            return;
        }

        if (User.isCustomer()) {
            agencyId = StringUtils.removeSpecialSuffix(product.getOrganizationId());
            String agencyName = StringUtils.getOrganizationNameFromId(agencyId);
            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Organizations, agencyId, null), R.drawable.icon_def, ivProviderAvatar);
            //TODO 现在是获取组织的icon，也可能是information的icon
            tvProviderName.setText(StringUtils.filterNull(agencyName));
            tvProductPhone.setText(account.getMobile());
        } else {
            ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null), R.drawable.icon_def, ivProviderAvatar);
            tvProviderName.setText(mOrder.getCreatorName());
            tvProductPhone.setText(mOrder.getMobile());
        }

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), null), R.drawable.icon_def, ivProductIcon);
        tvProductName.setText(product.getName());
        tvProductPrice.setText(String.format(getString(R.string.format_price), product.getPrice()));
        rbProduct.setRating(product.getScore());


    }

    @Override
    public void changeOrderStatusSuccess(String status) {
        //更改订单状态后的操作
        getActivity().finish();
        EventBus.getDefault().post(new Event.RefreshMyOrderData());

    }

    private int getStatus(String status) {
        int stringRes = 0;
        if (status.equals(status_reserved)) {
            stringRes = R.string.status_reserved;
        } else if (status.equals(status_assigned)) {
            stringRes = R.string.status_assigned;
        } else if (status.equals(status_done)) {
            stringRes = R.string.status_done;
        } else if (status.equals(status_cancel)) {
            stringRes = R.string.status_cancel;
        }
        return stringRes;
    }


    private int getStatusOperate(String status) {
        int statusOperate = 0;
        if (status.equals(status_reserved)) {
            statusOperate = R.string.operate_cancel;
        } else if (status.equals(status_assigned)) {
            statusOperate = R.string.operate_done;
        } else if (status.equals(status_done)) {
            statusOperate = R.string.rank;
        } else if (status.equals(status_cancel)) {
            statusOperate = -1;
        }
        return statusOperate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;

            case R.id.rl_provider_detail:
                if (User.isCustomer()) {
                    start(AgencyDetailFragment.newInstance(agencyId, null));
                }
                break;

            case R.id.rl_product_detail:
                if (mOrder.getProduct() != null) {
                    start(ServiceDetailFragment.newInstance(mOrder.getProduct()));
                }
                break;

            case R.id.rl_product_phone:
                //TODO 拨打电话
                call("确定联系商家？");
                break;

            case R.id.tv_operate:
                operate();
                break;

            case R.id.tv_cancel:
                changeOrderStatus(status_cancel, getAlartMsg(R.string.operate_cancel));
                break;
        }
    }

    private void operate() {
        String operate = tvOperate.getText().toString();
        if (!TextUtils.isEmpty(operate)) {
            //取消订单
            if (operate.equals(getString(R.string.operate_cancel))) {
                changeOrderStatus(status_cancel, getAlartMsg(R.string.operate_cancel));
            }

            //受理订单
            if (operate.equals(getString(R.string.operate_assigned))) {
                changeOrderStatus(status_assigned, getAlartMsg(R.string.operate_assigned));
            }

            //完成订单
            if (operate.equals(getString(R.string.operate_done))) {
                changeOrderStatus(status_done, "确定已经给顾客服务过了吗？不然会收到差评的哟！");
            }


            //评价
            if (operate.equals(getString(R.string.rank))) {
                start(RankFragment.newInstance(Constants.organizationProduct, mOrder.getProductId()));
            }
        }
    }

    private void changeOrderStatus(String status, String msg) {
        new NormalDialog().createNormal(_mActivity, msg, () -> {
            mPresenter.changeOrderStatus(mOrderId, status);
        });

    }

    public String getAlartMsg(int operate) {
        return "确定要" + getString(operate) + "吗？";
    }

    private void call(String msg) {
        String phoneDesc = tvProductPhone.getText().toString();
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
        String mobile = tvProductPhone.getText().toString();
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
}