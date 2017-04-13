package lilun.com.pension.ui.order.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Contact;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pension.ui.agency.merchant.MemoFragment;
import lilun.com.pension.ui.agency.reservation.AddServiceInfoFragment;
import lilun.com.pension.widget.NormalTitleBar;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

/**
 * 商家订单详情V
 *
 * @author yk
 *         create at 2017/4/6 15:25
 *         email : yk_developer@163.com
 */
public class MerchantOrderDetailFragment extends BaseFragment<MerchantOrderDetailContract.Presenter> implements MerchantOrderDetailContract.View {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.tv_product_name)
    TextView tvProviderName;
    @Bind(R.id.tv_health_status)
    TextView tvHealthStatus;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.rb_bar)
    RatingBar rbBar;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.ll_product_item)
    LinearLayout llProductItem;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    @Bind(R.id.tv_user_info)
    TextView tvUserInfo;
    @Bind(R.id.sv)
    ScrollView sv;
    private ProductOrder mOrder;
    private MemoFragment memoFragment;


    public static MerchantOrderDetailFragment newInstance(ProductOrder order) {
        MerchantOrderDetailFragment fragment = new MerchantOrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("order", order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        mOrder = (ProductOrder) arguments.getSerializable("order");
        Preconditions.checkNull(mOrder);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MerchantOrderDetailPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_merchant_order_detail;
    }


    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(() -> getActivity().finish());


        memoFragment = MemoFragment.newInstance(mOrder);
        replaceLoadRootFragment(R.id.fl_container, memoFragment, false);

        setInitView();

    }

    private void setInitView() {
        OrganizationProduct product = mOrder.getProduct();
        tvProviderName.setText(product.getName());
        tvProviderName.setTextColor(Color.BLUE);
        tvHealthStatus.setText(StringUtils.getOrderStatusValue(mOrder.getStatus()));
        tvName.setText(product.getContext());
        rbBar.setRating(product.getScore());
        tvPrice.setText("价格:" + product.getPrice());

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), StringUtils.getFirstIconNameFromIcon(product.getImages())), R.drawable.icon_def, ivImage);
    }


    @OnClick({R.id.btn_confirm, R.id.tv_user_info, R.id.ll_product_item})
    public void onClick(View v) {
        switch (v.getId()) {


            //保存信息
            case R.id.btn_confirm:
                saveMemo();
                break;


            //跳转用户资料界面
            case R.id.tv_user_info:
                Contact contact = mOrder.getContact();
                if (contact != null) {
                    start(AddServiceInfoFragment.newInstance(mOrder.getProduct().getCategoryId(), contact, false));
                }
                break;

            //产品详情界面
            case R.id.ll_product_item:
                start(ServiceDetailFragment.newInstance(mOrder.getProduct()));
                break;
        }
    }

    private void saveMemo() {
        memoFragment.saveMemo();
    }


}
