package lilun.com.pensionlife.ui.agency.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.slider.BannerPager;

/**
 * 产品详情页
 *
 * @author yk
 *         create at 2017/8/3 15:06
 *         email : yk_developer@163.com
 */
public class ProductDetailFragment extends BaseFragment {

    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Bind(R.id.banner)
    BannerPager banner;

    @Bind(R.id.tv_product_title)
    TextView tvProductTitle;

    @Bind(R.id.tv_product_title_extra)
    TextView tvProductTitleExtra;

    @Bind(R.id.rb_score)
    RatingBar rbScore;

    @Bind(R.id.tv_score)
    TextView tvScore;

    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;

    @Bind(R.id.tv_product_type)
    TextView tvProductType;

    @Bind(R.id.tv_product_area)
    TextView tvProductArea;

    @Bind(R.id.tv_product_mobile)
    TextView tvProductMobile;

    @Bind(R.id.tv_product_content)
    TextView tvProductContent;

    @Bind(R.id.tv_bottom_price)
    TextView tvBottomPrice;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private String mProductId;
    private OrganizationProduct mProduct;

    public static ProductDetailFragment newInstance(String productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("productId");
        Preconditions.checkNull(mProductId);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        swipeLayout.setEnabled(false);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProduct();
    }

    private void getProduct() {
//        if (!swipeLayout.isRefreshing()) {
//            swipeLayout.setRefreshing(true);
//        }
        NetHelper.getApi().getProduct(mProductId, null)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationProduct>(getActivity()) {
                    @Override
                    public void _next(OrganizationProduct product) {
//                        swipeLayout.setRefreshing(false);
                        showProductDetail(product);
                    }

//                    @Override
//                    public void onNext(OrganizationProduct organizationProduct) {
//                        super.onNext(organizationProduct);
//                        swipeLayout.setRefreshing(false);
//                    }
                });

    }

    private void showProductDetail(OrganizationProduct product) {

        this.mProduct = product;


        //图片
        showBanner(product);


        //标题栏
        titleBar.setTitle(product.getTitle());

        //标题
        tvProductTitle.setText(product.getTitle());

        //二级标题
        tvProductTitleExtra.setText(product.getTitle());

        //星
        rbScore.setRating(product.getScore());

        //星文字
        tvScore.setText((double) product.getScore() + "");

        //价格
        tvProductPrice.setText(new DecimalFormat("######0.00").format(product.getPrice()) + "");

        //服务方式
        tvProductType.setText(String.format("服务方式: %1$s", product.getContextType()));

        //服务范围
        tvProductArea.setText(String.format("服务范围: %1$s", "待定"));

        //服务电话
        tvProductMobile.setText(Html.fromHtml("服务热线: <font color='#17c5b4'>" + product.getMobile() + "</font>"));

        //内容
        tvProductContent.setText(product.getContext());

        //底部价格
        tvBottomPrice.setText(Html.fromHtml("价格:<font color='#ff5000'>" + product.getPrice() + "</font>"));

    }

    private void showBanner(OrganizationProduct product) {
        List<String> urls = new ArrayList<>();
        if (product.getImage() != null) {
            for (IconModule iconModule : product.getImage()) {
                String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, product.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);
    }


    @OnClick({R.id.tv_enter_provider, R.id.tv_reservation, R.id.tv_product_mobile})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_enter_provider:
                String organizationId = mProduct.getOrganizationId();
                start(ProviderDetailFragment.newInstance(StringUtils.removeSpecialSuffix(organizationId)),SINGLETASK);
                break;

            case R.id.tv_reservation:
                break;

            case R.id.tv_product_mobile:
                call();
                break;
        }
    }


    private void call() {
        String mobile = mProduct.getMobile();
        if (!TextUtils.isEmpty(mobile)) {
            boolean hasPermission = hasPermission(Manifest.permission.CALL_PHONE);
            if (hasPermission) {
                callMobile();
            } else {
                requestPermission(Manifest.permission.CALL_PHONE, 0X11);
            }
        } else {
            ToastHelper.get().showShort("此服务商没有提供电话");
        }
    }

    private void callMobile() {
        String mobile = mProduct.getMobile();
        new NormalDialog().createNormal(_mActivity, "是否联系：" + mobile, () -> {
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
                callMobile();
            }
        }
    }

}
