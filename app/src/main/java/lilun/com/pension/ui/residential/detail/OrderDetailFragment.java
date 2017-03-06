package lilun.com.pension.ui.residential.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.ui.agency.detail.AgencyDetailFragment;
import lilun.com.pension.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pension.ui.help.RankFragment;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

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

//    @Bind(R.id.tv_order_id)
//    TextView tvOrderId;

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

    @Bind(R.id.tv_product_name)
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

    private String mOrderId;
    private String status_reserved = "reserved";
    private String status_assigned = "assigned";
    private String status_done = "done";
    private String status_cancel = "cancel";
    private ProductOrder mOrder;
    private String agencyId;

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


        ivBack.setOnClickListener(this);
        tvOperate.setOnClickListener(this);
        rlProductPhone.setOnClickListener(this);
        rlProductDetail.setOnClickListener(this);
        rlProviderDetail.setOnClickListener(this);
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


        tvProductPhone.setText(order.getMobile());
        if (order.getStatus().equals(status_reserved)) {
            llOperate.setVisibility(View.VISIBLE);
            tvOperate.setText(R.string.cancel_order);
        }else if (order.getStatus().equals(status_done)){
            llOperate.setVisibility(View.VISIBLE);
            tvOperate.setText(R.string.rank);
        }

        //show product
        OrganizationProduct product = order.getProduct();
        if (product == null) {
            return;
        }

        agencyId = StringUtils.removeSpecialSuffix(product.getOrganizationId());
        String agencyName = StringUtils.getOrganizationNameFromId(agencyId);
        ImageLoaderUtil.instance().loadImage(IconUrl.organization(agencyId, null), R.drawable.avatar, ivProviderAvatar);
        //TODO 现在是获取组织的icon，也可能是information的icon
        tvProviderName.setText(StringUtils.filterNull(agencyName));

        ImageLoaderUtil.instance().loadImage(IconUrl.organizationProduct(product.getId(), null), R.drawable.icon_def, ivProductIcon);
        tvProductName.setText(product.getName());
        tvProductPrice.setText(String.format(getString(R.string.format_price), product.getPrice()));
        rbProduct.setRating(product.getScore());


    }

    @Override
    public void changeOrderStatusSuccess(String status) {
        //取消订单后的操作
        if (status.equals(status_cancel)) {
            llOperate.setVisibility(View.GONE);
            tvOrderStatus.setText(getStatus(status_cancel));
            EventBus.getDefault().post(new Event.RefreshMyOrderData());
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;

            case R.id.rl_provider_detail:
                start(AgencyDetailFragment.newInstance(agencyId,null));
                break;

            case R.id.rl_product_detail:
                if (mOrder.getProduct()!=null){
                    start(ServiceDetailFragment.newInstance(mOrder.getProduct()));
                }
                break;

            case R.id.rl_product_phone:
                //TODO 拨打电话
                call();
                break;

            case R.id.tv_operate:
                //TODO 根据不同状态做不同的操作
                operate();

                break;
        }
    }

    private void operate() {
        String status = mOrder.getStatus();
        if (status.equals(status_reserved)){
            cancelOrder();
        }else if (status.equals(status_done)){
            start(RankFragment.newInstance(Constants.organizationProduct,mOrder.getProductId()));
        }
    }

    private void cancelOrder() {
        new NormalDialog().createNormal(_mActivity, "确定取消订单？", () -> {
            mPresenter.changeOrderStatus(mOrderId, status_cancel);
        });

    }

    private void call() {
        String phoneDesc = tvProductPhone.getText().toString();
        if (!TextUtils.isEmpty(phoneDesc)) {
            new NormalDialog().createNormal(_mActivity, "确定联系商家？", () -> {
                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneDesc));
                startActivity(intent);
            });
        }
    }


}
