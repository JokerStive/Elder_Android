package lilun.com.pensionlife.ui.agency.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.OrganizationChildrenConfig;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.AllProductAdapter;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.bean.Organization;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.Provider;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.CustomRatingBar;
import lilun.com.pensionlife.widget.ElderModuleClassifyDecoration;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.slider.BannerPager;

/**
 * 养老机构提供的服务详情V
 * 居家服务详情
 *
 * @author yk
 *         create at 2017/2/23 13:19
 *         email : yk_developer@163.com
 */
public class ProviderDetailFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.banner)
    BannerPager banner;
    @Bind(R.id.tv_provider_title)
    TextView tvProviderTitle;
    @Bind(R.id.tv_provider_address)
    TextView tvProviderAddress;
    @Bind(R.id.rb_score)
    CustomRatingBar rbScore;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.tv_product_price)
    TextView tvProductPrice;
    @Bind(R.id.tv_product_time)
    TextView tvProductTime;
    @Bind(R.id.tv_provider_mobile)
    TextView tvProviderMobile;
    @Bind(R.id.tv_provider_phone)
    TextView tvProviderPhone;
    @Bind(R.id.tv_all_product)
    TextView tvAllProduct;
    @Bind(R.id.tv_provider_attention)
    TextView tvProviderAttention;
    @Bind(R.id.wb_provider_attention)
    WebView wbProviderAttention;
    @Bind(R.id.rv_all_product)
    RecyclerView rvAllProduct;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    private String mProviderId;
    private String clickMobile;
    private ArrayList<TextView> changeLayoutTextViews;
    private AllProductAdapter adapter;
    private String phone;
    private String mobile;

    public static ProviderDetailFragment newInstance(String providerId) {
        ProviderDetailFragment fragment = new ProviderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("providerId", providerId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProviderId = arguments.getString("providerId");
        Preconditions.checkNull(mProviderId);
    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_provider_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        titleBar.setOnBackClickListener(this::pop);

        swipeLayout.setEnabled(false);

        tvAllProduct.setSelected(true);

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProvider();
        getAllProduct(0);

        changeLayoutTextViews = new ArrayList<>();
        changeLayoutTextViews.add(tvAllProduct);
        changeLayoutTextViews.add(tvProviderAttention);

        rvAllProduct.setLayoutManager(new GridLayoutManager(App.context, 2));
        rvAllProduct.addItemDecoration(new ElderModuleClassifyDecoration(10));
    }


    private void getProvider() {
        String filter = "{\"include\":\"extension\"}";
        NetHelper.getApi().getOrganizationById(mProviderId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Organization>(getActivity()) {
                    @Override
                    public void _next(Organization provider) {
                        showProvider(provider);
                    }
                });
    }

    private void getAllProduct(int skip) {
        String filter = "{\"where\":{\"organizationId\":\"" + OrganizationChildrenConfig.product(mProviderId) + "\"}}";
        NetHelper.getApi().getProducts(StringUtils.addFilterWithDef(filter, skip))
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationProduct>>() {
                    @Override
                    public void _next(List<OrganizationProduct> products) {
                        showAllProduct(products, skip != 0);
                    }
                });
    }


    private void showProvider(Organization provider) {
        //图片
        showBanner(provider);

        //标题
        tvProviderTitle.setText(provider.getName());

        Provider extension = provider.getExtension();
        if (extension != null) {

            //地址
            tvProviderAddress.setText(extension.getAddress());

            //星
            rbScore.setCountSelected(extension.getScore());

            //文字星
            tvScore.setText((double) extension.getScore() + "");

            //价格区间
            String price = "价格区间: <font color='#ff9d09'>" + "¥" + extension.getMinPriceRange() + " - " + extension.getMaxPriceRange() + "</font>";
            tvProductPrice.setText(Html.fromHtml(price));

            //电话
            phone = extension.getPhone();
            mobile = extension.getMobile();
            if (!TextUtils.isEmpty(phone)) {
                String mobileDes = "手机号: <font color='#17c5b4'>" + mobile + "</font>";
                String phoneDes = "座机号: <font color='#17c5b4'>" + phone + "</font>";
                tvProviderPhone.setText(Html.fromHtml(phoneDes));
                tvProviderMobile.setText(Html.fromHtml(mobileDes));
            }

            //商家介紹
            String context = extension.getContext();
            if (!TextUtils.isEmpty(context)) {
                wbProviderAttention.getSettings().setJavaScriptEnabled(true);
                wbProviderAttention.loadDataWithBaseURL("", extension.getContext(), "text/html", "UTF-8", "");
            }
        }


    }


    private void showAllProduct(List<OrganizationProduct> products, boolean isLoadMore) {
        if (!isLoadMore) {
            adapter = new AllProductAdapter(products);
            adapter.openLoadMore(50, true);
            adapter.setOnItemClickListener(product -> start(ProductDetailFragment.newInstance(product.getId()), SINGLETASK));
            rvAllProduct.setAdapter(adapter);

        } else {
            adapter.replaceAll(products);
        }
        adapter.notifyDataChangedAfterLoadMore(true);
        if (products.size() < adapter.getPageSize()) {
            adapter.notifyDataChangedAfterLoadMore(false);
        }
    }


    @OnClick({R.id.tv_provider_mobile, R.id.tv_provider_phone, R.id.tv_all_product, R.id.tv_provider_attention})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_provider_mobile:
                connectProvider(1);
                break;

            case R.id.tv_provider_phone:
                connectProvider(2);
                break;

            case R.id.tv_all_product:
                changeLayout(R.id.tv_all_product);
                break;

            case R.id.tv_provider_attention:
                changeLayout(R.id.tv_provider_attention);
                break;

        }
    }

    private void changeLayout(int clickTextViewId) {
        for (TextView target : changeLayoutTextViews) {
            int id = target.getId();
            target.setSelected(clickTextViewId == id);
        }
        switch (clickTextViewId) {
            case R.id.tv_all_product:
                rvAllProduct.setVisibility(View.VISIBLE);
                wbProviderAttention.setVisibility(View.INVISIBLE);
                break;

            case R.id.tv_provider_attention:
                rvAllProduct.setVisibility(View.INVISIBLE);
                wbProviderAttention.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void connectProvider(int flag) {
        switch (flag) {
            case 1:
                clickMobile = mobile;
                break;
            case 2:
                clickMobile = phone;
                break;
        }
        call();
    }


    private void showBanner(Organization provider) {
        List<String> urls = new ArrayList<>();
        if (provider.getIcon() != null) {
            for (IconModule iconModule : provider.getIcon()) {
                String url = IconUrl.moduleIconUrl(IconUrl.Organizations, provider.getId(), iconModule.getFileName(), "icon");
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.Organizations, provider.getId(), "icon");
            urls.add(url);
        }
        banner.setData(urls);
    }


    private void call() {
        if (!TextUtils.isEmpty(clickMobile)) {
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
        new NormalDialog().createNormal(_mActivity, "是否联系：" + clickMobile, () -> {
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + clickMobile));
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
