package lilun.com.pension.ui.agency.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Constants;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.agency.reservation.ServiceUserInfoFragment;
import lilun.com.pension.ui.order.MerchantOrderListFragment;
import lilun.com.pension.ui.residential.rank.RankListFragment;
import lilun.com.pension.widget.slider.BannerPager;

/**
 * 养老机构提供的服务详情V
 *
 * @author yk
 *         create at 2017/2/23 13:19
 *         email : yk_developer@163.com
 */
public class ServiceDetailFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_desc)
    TextView tvProvider;

    @Bind(R.id.tv_mobile)
    TextView tvPrice;

    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;

    @Bind(R.id.tv_equipment)
    TextView tvContent;

    @Bind(R.id.tv_enter_rank)
    TextView tvEnterRank;

    @Bind(R.id.rb_bar)
    RatingBar rbBar;

    @Bind(R.id.tv_product_name)
    TextView tvProviderName;

    @Bind(R.id.tv_enter_provider)
    TextView tvEnterProvider;

    @Bind(R.id.iv_icon)
    BannerPager banner;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    @Bind(R.id.ll_reply)
    LinearLayout llReply;

    private OrganizationProduct mProduct;
    private String mId;


    public static ServiceDetailFragment newInstance(OrganizationProduct product) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("pro" +
                "duct", product);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProduct = (OrganizationProduct) arguments.getSerializable("product");
        Preconditions.checkNull(mProduct);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agency_service_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        if (!User.isCustomer()) {
            tvReservation.setVisibility(View.GONE);
        }

        //加粗
        UIUtils.setBold(tvTitle);
        UIUtils.setBold(tvIntroduction);
        UIUtils.setBold(tvProviderName);
        UIUtils.setBold(tvEnterRank);

        String agencyId = StringUtils.removeSpecialSuffix(mProduct.getOrganizationId());
        String agencyName = StringUtils.getOrganizationNameFromId(agencyId);

        //显示
        tvTitle.setText(mProduct.getTitle());
        tvProvider.setText(String.format(getString(R.string.format_provider), agencyName));
        tvPrice.setText(String.format(getString(R.string.format_price), mProduct.getPrice()));
        tvContent.setText(StringUtils.filterNull(mProduct.getContext()));

        rbBar.setRating(mProduct.getScore());

        tvProviderName.setText(agencyName);


        //事件
        ivBack.setOnClickListener(this);
        tvReservation.setOnClickListener(this);
        llReply.setOnClickListener(this);
        tvEnterProvider.setOnClickListener(this);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //展示product图片
        List<String> urls = new ArrayList<>();
        if (mProduct.getImages() != null) {
            for (IconModule iconModule : mProduct.getImages()) {
                String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, mProduct.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.moduleIconUrl(IconUrl.OrganizationProducts, mProduct.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);

        //如果这个服务不是自己创建的，就要去判断是否能够预约
        if (!User.creatorIsOwn(mProduct.getCreatorId())) {
            String filter = "{\"where\":{\"creatorId\":\"" + User.getUserId() + "\",\"or\":[{\"status\":\"reserved\"},{\"status\":\"assigned\"}]}}";
            NetHelper.getApi()
                    .getOrdersOfProduct(mProduct.getId(), filter)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<List<ProductOrder>>(_mActivity) {
                        @Override
                        public void _next(List<ProductOrder> orders) {
                            if (orders.size() != 0) {
                                setHadReservation();
                            }
                        }
                    });
        }


    }

    private void setHadReservation() {
        tvReservation.setBackgroundResource(R.drawable.shape_circle_red);
        tvReservation.setTextColor(Color.WHITE);
        tvReservation.setText("已预约");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;

            case R.id.tv_reservation:
                if (tvReservation.getText().equals(getString(R.string.reservation))) {
                    ServiceUserInfoFragment fragment = ServiceUserInfoFragment.newInstance(mProduct.getCategoryId(), mProduct.getId());
                    startForResult(fragment, 0);
                }
                break;

            case R.id.tv_enter_provider:
                next();
                break;

            case R.id.ll_reply:
                //TODO 进入评价列表页面
                start(RankListFragment.newInstance(Constants.organizationProduct, mProduct.getId(), mProduct.getTitle()));
                break;
        }
    }

    private void next() {
        if (User.isCustomer()) {
            String organizationId = mProduct.getOrganizationId();
            start(AgencyDetailFragment.newInstance(StringUtils.removeSpecialSuffix(organizationId), null), SINGLETASK);
        } else {
            start(MerchantOrderListFragment.newInstance(mProduct.getId()));
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            setHadReservation();
        }
    }

    //    private void setReservation() {
//        tvReservation.setText(R.string.cancel);
//        tvReservation.setTextColor(getResources().getColor(R.color.white));
//        tvReservation.setBackgroundResource(R.drawable.shape_circle_red);
//    }


}
