package lilun.com.pensionlife.ui.agency.detail;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ProductRankAdapter;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.Count;
import lilun.com.pensionlife.module.bean.IconModule;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.ProductOrder;
import lilun.com.pensionlife.module.bean.Rank;
import lilun.com.pensionlife.module.utils.Preconditions;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.agency.reservation.ReservationFragment;
import lilun.com.pensionlife.ui.contact.AddBasicContactFragment;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
import lilun.com.pensionlife.ui.residential.rank.RankListFragment;
import lilun.com.pensionlife.widget.CustomRatingBar;
import lilun.com.pensionlife.widget.DividerDecoration;
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
    CustomRatingBar rbScore;

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

    @Bind(R.id.wb_product_content)
    WebView wbProductContent;

    @Bind(R.id.tv_bottom_price)
    TextView tvBottomPrice;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.tv_rank_count)
    TextView tvRankCount;

    @Bind(R.id.rv_rank)
    RecyclerView rvRank;

    @Bind(R.id.tv_all_rank)
    TextView tvAllRank;
    @Bind(R.id.ll_rank)
    LinearLayout llRank;
    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    private String mProductId;
    private OrganizationProduct mProduct;

    public static ProductDetailFragment newInstance(String productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refresh(String tx) {
        if (tx.contains("hasOrder")) {
            setHadOrdered();
            call();
        }
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

        UIUtils.setBold(tvRankCount);

        rvRank.setLayoutManager(new LinearLayoutManager(App.context, LinearLayoutManager.VERTICAL, false));
        rvRank.addItemDecoration(new DividerDecoration(App.context, LinearLayoutManager.VERTICAL, 1, App.context.getResources().getColor(R.color.gray)));
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getProduct();
        get2Rank();
        getRankCount();
        getIsOrder();
    }


    private void getProduct() {
        NetHelper.getApi().getProduct(mProductId, null)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationProduct>(getActivity()) {
                    @Override
                    public void _next(OrganizationProduct product) {
                        showProductDetail(product);
//                        get2Rank();
                    }
                });

    }

    private void getIsOrder() {
        String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"or\":[{\"status\":\"reserved\"},{\"status\":\"assigned\"}]}}";
        NetHelper.getApi()
                .getOrdersOfProduct(mProductId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductOrder>>(_mActivity) {
                    @Override
                    public void _next(List<ProductOrder> orders) {
                        if (orders.size() != 0) {
                            setHadOrdered();
                        }
                    }
                });
    }

    private void setHadOrdered() {
        tvReservation.setBackgroundColor(_mActivity.getResources().getColor(R.color.yellowish));
        tvReservation.setEnabled(false);
        tvReservation.setText("已经预约");
    }

    private void get2Rank() {
        String filter = "{\"limit\":\"2\",\"order\":\"createdAt DESC\",\"where\":{\"whatModel\":\"OrganizationProduct\",\"whatId\":\"" + mProductId + "\"}}";
        NetHelper.getApi()
                .getRanks(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Rank>>() {
                    @Override
                    public void _next(List<Rank> ranks) {
                        show2Rank(ranks);
                    }
                });
    }


    private void getRankCount() {
        String filter = "{\"whatId\":\"" + mProductId + "\"}";
        NetHelper.getApi()
                .getRanksCount(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Count>() {
                    @Override
                    public void _next(Count count) {
                        showRankCount(count.getCount());
                    }
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
        rbScore.setCountSelected(product.getScore());

        //星文字
        tvScore.setText((double) product.getScore() + "");

        //价格
        tvProductPrice.setText(new DecimalFormat("######0.00").format(product.getPrice()) + "/次");

        //服务方式
        tvProductType.setText("服务方式: 线下服务");

        //服务范围
        showProductArea();


        //服务电话
        tvProductMobile.setText(Html.fromHtml("服务热线: <font color='#17c5c3'>" + product.getMobile() + "</font>"));

        //内容
        wbProductContent.getSettings().setJavaScriptEnabled(true);
        wbProductContent.loadDataWithBaseURL("", product.getContext(), "text/html", "UTF-8", "");

        //底部价格
        tvBottomPrice.setText(Html.fromHtml("价格:<font color='#ff5000'>" + product.getPrice() + "</font>"));

    }

    /**
     * 服务范围
     */
    private void showProductArea() {
        List<String> areas = mProduct.getAreaIds();
        String result = "无";
        if (areas != null) {
            for (int i = 0; i < areas.size(); i++) {
                String area = StringUtils.getOrganizationNameFromId(areas.get(i));
                if (!TextUtils.isEmpty(area)) {
                    if (i == 0) {
                        result = result + area;
                    } else {
                        result = result + "、" + area;
                    }
                }
            }
        }
        tvProductArea.setText(String.format("服务范围: %1$s", result));
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


    private void show2Rank(List<Rank> ranks) {
        if (ranks.size() != 0) {
            llRank.setVisibility(View.VISIBLE);
            ProductRankAdapter adapter = new ProductRankAdapter(ranks);
            rvRank.setAdapter(adapter);
        }
    }


    private void showRankCount(int count) {
        if (count > 0) {
            tvRankCount.setText(String.format("评价 （ %1$s ）", count));
        }
    }


    @OnClick({R.id.tv_enter_provider, R.id.tv_reservation, R.id.tv_product_mobile, R.id.tv_all_rank, R.id.tv_rank_count})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_enter_provider:
                String organizationId = mProduct.getOrganizationId();
                start(ProviderDetailFragment.newInstance(StringUtils.removeSpecialSuffix(organizationId)), SINGLETASK);
                break;

            case R.id.tv_reservation:
                //立即预约
                takeReservation();
                break;

            case R.id.tv_product_mobile:
                call();
                break;

            case R.id.tv_all_rank:
                //查看所有评价
                allRankAboutThisProduct();
                break;

            case R.id.tv_rank_count:
                //查看所有评价
                allRankAboutThisProduct();
                break;
        }
    }

    /**
     * 预约
     */
    private void takeReservation() {
        if (TextUtils.equals(mProduct.getCreatorId(), User.getUserId())) {
            ToastHelper.get().showWareShort("不能预约自己创建的服务");
            return;
        }
        String filter = "{\"where\":{\"accountId\":\"" + User.getUserId() + "\"}}";
        NetHelper.getApi().getContacts(filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Contact>>(getActivity()) {
                    @Override
                    public void _next(List<Contact> contacts) {
                        if (contacts.size() > 0) {
                            //显示 预约者信息列表
                            start(ContactListFragment.newInstance(mProduct.getId()));
                        } else {
                            //新增基础信息界面
                            start(AddBasicContactFragment.newInstance(mProduct.getId()));
                        }
                    }
                });
    }

    /**
     * 查看所有评价
     */
    private void allRankAboutThisProduct() {
        start(RankListFragment.newInstance(Constants.organizationProduct, mProduct.getId(), mProduct.getTitle()));
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
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + mobile.replace("-", "")));
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


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Logger.d("requestCode =  " + requestCode + "----" + "resultCode = " + resultCode);
        if (requestCode == ReservationFragment.requestCode && resultCode == ReservationFragment.resultCode) {
            setHadOrdered();
        }
    }


}
