package lilun.com.pensionlife.ui.agency.detail;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Contact;
import lilun.com.pensionlife.module.bean.CourseSchedule;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.OrderLimit;
import lilun.com.pensionlife.module.bean.OrganizationProduct;
import lilun.com.pensionlife.module.bean.OrganizationProductCategory;
import lilun.com.pensionlife.module.bean.ProductOrder;
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
import lilun.com.pensionlife.ui.education.course_details.CourseDetailContract;
import lilun.com.pensionlife.ui.education.course_details.CourseDetailPresenter;
import lilun.com.pensionlife.ui.order.OrderListFragment;
import lilun.com.pensionlife.ui.order.personal_detail.OrderDetailActivity;
import lilun.com.pensionlife.ui.protocol.ProtocolView;
import lilun.com.pensionlife.ui.residential.rank.RankListFragment;
import lilun.com.pensionlife.widget.CustomRatingBar;
import lilun.com.pensionlife.widget.DividerDecoration;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.ProgressWebView;
import lilun.com.pensionlife.widget.slider.BannerPager;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 产品详情页
 *
 * @author yk
 *         create at 2017/8/3 15:06
 *         email : yk_developer@163.com
 */
public class ProductDetailFragment extends BaseFragment<CourseDetailContract.Presenter> implements CourseDetailContract.View {

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

    @Bind(R.id.tv_product_phone)
    TextView tvProductPhone;

    @Bind(R.id.wb_product_content)
    ProgressWebView wbProductContent;

    @Bind(R.id.tv_bottom_price)
    TextView tvBottomPrice;

    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @Bind(R.id.tv_rank_count)
    TextView tvRankCount;

    @Bind(R.id.tv_order_detail)
    TextView tvOrderDetail;

    @Bind(R.id.rv_rank)
    RecyclerView rvRank;

    @Bind(R.id.tv_all_rank)
    TextView tvAllRank;

    @Bind(R.id.ll_rank)
    LinearLayout llRank;

    @Bind(R.id.protocol)
    ProtocolView mProtocolView;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    private String mProductId;
    private OrganizationProduct mProduct;
    private String clickMobile;
    private String mobile;
    private String phone;
    private boolean productIsLimit = false;

    public static ProductDetailFragment newInstance(String productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("objectId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Subscribe
    public void refresh(String tx) {
        if (tx.contains("hasOrder")) {
//            setHadOrdered();
            call();
            start(OrderListFragment.newInstance());
        }
    }


    @Subscribe
    public void refresh(ProtocolView.IsAgreeProtocol isAgreeProtocol) {
        mProtocolView.agreeProtocol(isAgreeProtocol.isAgreeProtocol);
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProductId = arguments.getString("objectId");
        Preconditions.checkNull(mProductId);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CourseDetailPresenter();
        mPresenter.bindView(this);

        String filter = "{\"include\":{\"relation\":\"orgCategory\",\"scope\":{\"fields\":[\"icon\"]}}}";
        mPresenter.getProductDetail(mProductId, filter);
        mPresenter.getProtocol(mProductId);
        mPresenter.getIsOrder(mProductId);
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


    private void setHadOrdered() {
        tvReservation.setBackgroundColor(_mActivity.getResources().getColor(R.color.yellowish));
        tvReservation.setEnabled(false);
        tvReservation.setText("已经预约");
        tvOrderDetail.setVisibility(View.VISIBLE);
    }

    /**
     * 服务范围
     */
    private void showProductArea() {
        List<String> areas = mProduct.getAreaIds();
        String result = StringUtils.getProductArea(areas);
        tvProductArea.setText(String.format("服务范围: %1$s", result));
    }

    private void showBanner(OrganizationProduct product) {
        List<String> urls = new ArrayList<>();
        if (product.getImage() != null) {
            for (String url : product.getImage()) {
                urls.add(url);
            }
        } else if (product.getOrgCategory() != null) {
            OrganizationProductCategory orgCategory = product.getOrgCategory();
            urls.add(orgCategory.getIcon());
        }
        banner.setData(urls);
    }


    @Override
    public void showCourseDetail(OrganizationProduct product) {

        this.mProduct = product;


        //图片
        showBanner(product);


        //标题栏
        titleBar.setTitle(product.getTitle());

        //标题
        tvProductTitle.setText(product.getTitle());

        //二级标题
        String contextType = product.getContextType();
        if (!TextUtils.isEmpty(contextType) && (contextType.equals("3") || contextType.equals("2"))) {
            tvProductTitleExtra.setText(product.getSubTitle());
        } else {
            String context = product.getContext();
            tvProductTitleExtra.setText(context);
        }

        //星
        rbScore.setCountSelected(product.getRank());

        //星文字
        tvScore.setText((double) product.getRank() + "");

        //价格
        Double price = product.getPrice();
        String formatPriceToFree = StringUtils.formatPrice(price);
        String topPriceResult = !TextUtils.isEmpty(formatPriceToFree) ? "¥" + formatPriceToFree + product.getUnit() : StringUtils.formatPriceToFree(price);
        tvProductPrice.setText(topPriceResult);
        //底部价格
        String buttonPriceResult = !TextUtils.isEmpty(formatPriceToFree) ? formatPriceToFree : StringUtils.formatPriceToFree(price);
        tvBottomPrice.setText(Html.fromHtml("价格:<font color='#ff5000'>" + buttonPriceResult + "</font>"));

        //服务方式
        tvProductType.setText("服务方式: 线下服务");

        //服务范围
        showProductArea();


        //电话
        phone = TextUtils.isEmpty(product.getPhone()) ? "暂未提供" : product.getPhone();
        mobile = TextUtils.isEmpty(product.getMobile()) ? "暂未提供" : product.getMobile();
        tvProductMobile.setText(Html.fromHtml("手机号: <font color='#17c5c3'>" + mobile + "</font>"));
        tvProductPhone.setText(Html.fromHtml("座机号: <font color='#17c5c3'>" + phone + "</font>"));

        //内容
        wbProductContent.noProgress();
        if (contextType.equals("2")) {
            wbProductContent.loadDataWithBaseURL("", product.getContext(), "text/html", "UTF-8", "");
        } else if (contextType.equals("3")) {
            wbProductContent.loadUrl(product.getContext());
        }
    }

    @Override
    public void showProtocol(Information protocol) {
        if (protocol != null) {
//            mProtocolView.setVisibility(View.VISIBLE);
//            mProtocolView.showProtocol(this, protocol);
        }
    }

    @Override
    public void showSchedules(List<CourseSchedule> schedules) {

    }

    @Override
    public void showIsOrdered(OrderLimit orderLimit) {
        boolean ordered = orderLimit.isOrdered();
        boolean isLimit = orderLimit.isIsLimit();
        if (ordered) {
            setHadOrdered();
        } else if (isLimit) {
            productIsLimit = true;
        }
    }

    @OnClick({R.id.tv_order_detail, R.id.tv_enter_provider, R.id.tv_reservation, R.id.tv_product_phone, R.id.tv_product_mobile, R.id.tv_all_rank, R.id.tv_rank_count})
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
                connectProvider(1);
                break;

            case R.id.tv_product_phone:
                connectProvider(2);
                break;

            case R.id.tv_all_rank:
                //查看所有评价
                allRankAboutThisProduct();
                break;

            case R.id.tv_rank_count:
                //查看所有评价
                allRankAboutThisProduct();
                break;

            case R.id.tv_order_detail:
                //查看订单详情
                transferOrderDetail();
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


    /**
     * 查看订单详情
     */
    private void transferOrderDetail() {
        String filter = "{\"order\":\"createdAt DESC\",\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"status\":{\"inq\":[\"reserved\",\"assigned\",\"delay\"]}}}";
        NetHelper.getApi().getOrdersOfProduct(mProductId, filter)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<ProductOrder>>(getActivity()) {
                    @Override
                    public void _next(List<ProductOrder> orders) {
                        if (orders.size() > 0) {
                            String orderId = orders.get(0).getId();
                            Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                            intent.putExtra("orderId", orderId);
                            startActivity(intent);
                        } else {
                            ToastHelper.get().showWareShort("没有找到该产品对应的订单");
                        }
                    }
                });
    }


    /**
     * 预约
     */
    private void takeReservation() {

        if (mProtocolView.getVisibility() != View.GONE) {
            boolean agreeProtocol = mProtocolView.isAgreeProtocol();
            if (!agreeProtocol) {
                ToastHelper.get().showWareShort("请先同意协议");
                return;
            }
        }

        if (TextUtils.equals(mProduct.getCreatorId(), User.getUserId())) {
            ToastHelper.get().showWareShort("不能预约自己创建的服务");
            return;
        }

        if (productIsLimit) {
            new NormalDialog().createNormal(_mActivity, "该产品的服务时间与您预约过的产品时间有冲突,继续预约吗？", new NormalDialog.OnPositiveListener() {
                @Override
                public void onPositiveClick() {
                    reservation();
                }
            });
        }

        reservation();

    }

    private void reservation() {
        mPresenter.getContacts();
    }

    /**
     * 检查预约资料
     */
    @Override
    public void checkContact(List<Contact> contacts) {
        if (contacts.size() > 0) {
            //显示 预约者信息列表
            Contact defContact = getDefaultContact(contacts);
            if (defContact == null) {
                //没有默认信息，就进去信息列表
                start(ContactListFragment.newInstance(mProduct.getId(), ContactListFragment.RESERVATION_PRODUCT), SupportFragment.SINGLETASK);
            } else if (TextUtils.isEmpty(defContact.getMobile()) || TextUtils.isEmpty(defContact.getName()) || TextUtils.isEmpty(defContact.getAddress())) {
                defContact.setProductId(mProductId);
                //必要信息不完善
                start(AddBasicContactFragment.newInstance(defContact, ContactListFragment.RESERVATION_PRODUCT));
            } else {
                //有默认信息，并且必要信息完整，直接预约界面
                start(ReservationFragment.newInstance(mProductId, defContact));
            }
        } else {
            //新增基础信息界面
            AddBasicContactFragment addBasicContactFragment = new AddBasicContactFragment();
            Bundle args = new Bundle();
            args.putString("objectId", mProductId);
            addBasicContactFragment.setArguments(args);
            addBasicContactFragment.setOnAddBasicContactListener(contact -> start(ReservationFragment.newInstance(mProductId, contact)));
            start(addBasicContactFragment);
        }
    }

    /**
     * 获取默认信息
     */
    private Contact getDefaultContact(List<Contact> contacts) {
        for (Contact contact : contacts) {
            int index = contact.getIndex();
            if (index == 1) {
                return contact;
            }
        }
        return null;
    }

    /**
     * 查看所有评价
     */
    private void allRankAboutThisProduct() {
        start(RankListFragment.newInstance(Constants.organizationProduct, mProduct.getId(), mProduct.getTitle()));
    }


    private void call() {
        if (TextUtils.isEmpty(clickMobile)) {
            clickMobile = mobile;
        }
        if (!clickMobile.equals("暂未提供")) {
            new NormalDialog().createNormal(_mActivity, "是否联系：" + clickMobile, () -> {
                requestPermission(Manifest.permission.CALL_PHONE, new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + clickMobile.replace("-", "")));
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied() {
                        ToastHelper.get().showShort("您拒接了该权限");
                    }
                });

            });

        } else {
            ToastHelper.get().showShort("此服务商没有提供电话");
        }
    }

    private void callMobile() {
        new NormalDialog().createNormal(_mActivity, "是否联系：" + clickMobile, () -> {
            Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + clickMobile.replace("-", "")));
            startActivity(intent);
        });
    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        Logger.d("requestCode =  " + requestCode + "----" + "resultCode = " + resultCode);
        if (requestCode == ReservationFragment.requestCode && resultCode == ReservationFragment.resultCode) {
            setHadOrdered();
        }
    }


}
