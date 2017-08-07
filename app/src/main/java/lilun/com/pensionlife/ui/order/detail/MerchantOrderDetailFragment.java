package lilun.com.pensionlife.ui.order.detail;

import android.os.Bundle;
import android.text.TextUtils;
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
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Config;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.ui.agency.detail.ServiceDetailFragment;
import lilun.com.pensionlife.ui.agency.merchant.MemoFragment;
import lilun.com.pensionlife.ui.agency.reservation.AddServiceInfoFragment;
import lilun.com.pensionlife.ui.tourism.detail.TourismDetailFragment;
import lilun.com.pensionlife.ui.tourism.info.AddTourismInfoFragment;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

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

    @Bind(R.id.tv_sophisticated)
    TextView tvProductName;

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
        tvProductName.setText(product.getName());
        tvHealthStatus.setText(StringUtils.getOrderStatusValue(mOrder.getStatus()));
        tvName.setText(product.getContext());
        rbBar.setRating(product.getScore());
        tvPrice.setText("价格:" + product.getPrice());

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), StringUtils.getFirstIconNameFromIcon(product.getImage())), R.drawable.icon_def, ivImage);
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
                String contactId = mOrder.getUserProfileId();
                if (!TextUtils.isEmpty(contactId)) {
                    String categoryId = mOrder.getCategoryId();
                    if (categoryId.equals(Config.tourism_product_categoryId)) {
                        start(AddTourismInfoFragment.newInstance(mOrder.getUserProfile(), false));
                    } else {
                        start(AddServiceInfoFragment.newInstance(mOrder.getUserProfile(),categoryId, false));
                    }
                }
                break;

            //产品详情界面
            case R.id.ll_product_item:
                String categoryId = mOrder.getCategoryId();
                if (!categoryId.equals(Config.tourism_product_categoryId)) {
                    start(ServiceDetailFragment.newInstance(mOrder.getProduct()));
                } else {
                    start(TourismDetailFragment.newInstance(mOrder.getProduct().getId()));
                }
                break;
        }
    }

    private void saveMemo() {
        memoFragment.saveMemo();
    }


}
