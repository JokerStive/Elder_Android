package lilun.com.pension.ui.agency.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationProduct;
import lilun.com.pension.module.bean.ProductOrder;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.NormalDialog;
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

    @Bind(R.id.tv_price)
    TextView tvPrice;

    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;

    @Bind(R.id.tv_requirement)
    TextView tvContent;

    @Bind(R.id.tv_enter_rank)
    TextView tvEnterRank;

    @Bind(R.id.rb_bar)
    RatingBar rbBar;

    @Bind(R.id.tv_provider_name)
    TextView tvProviderName;
    @Bind(R.id.tv_enter_provider)
    TextView tvEnterProvider;
    @Bind(R.id.iv_icon)
    BannerPager banner;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    private OrganizationProduct mProduct;
    private String mId;


    public static ServiceDetailFragment newInstance(OrganizationProduct product) {
        ServiceDetailFragment fragment = new ServiceDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("product", product);
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
        tvPrice.setText(String.format("价格：%1$d元", mProduct.getPrice()));
        tvContent.setText(StringUtils.filterNull(mProduct.getContext()));

        rbBar.setRating(mProduct.getScore());

        tvProviderName.setText(agencyName);


        //事件
        ivBack.setOnClickListener(this);
        tvReservation.setOnClickListener(this);
        tvEnterRank.setOnClickListener(this);
        tvEnterProvider.setOnClickListener(this);

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        List<String> urls = new ArrayList<>();
        if (mProduct.getImages() != null) {
            for (IconModule iconModule : mProduct.getImages()) {
                String url = IconUrl.organizationProduct(mProduct.getId(), iconModule.getFileName());
                urls.add(url);
            }
        } else {
            String url = IconUrl.organizationAid(mProduct.getId(), null);
            urls.add(url);
        }
        banner.setData(urls);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                pop();
                break;

            case R.id.tv_reservation:
                reservation();
                break;

            case R.id.tv_enter_provider:
                String organizationId = mProduct.getOrganizationId();
                Logger.d("提供商的id==" + StringUtils.removeSpecialSuffix(organizationId));
                start(AgencyDetailFragment.newInstance(StringUtils.removeSpecialSuffix(organizationId), null), SINGLETASK);
                break;

            case R.id.tv_enter_rank:
                //TODO 进入评价列表页面

                break;
        }
    }

    /**
    *预约服务
    */
    private void reservation() {
        if (tvReservation.getText().equals(getString(R.string.cancel))){
            
        }else if (tvReservation.getText().equals(getString(R.string.reservation))){
            new NormalDialog().createNormal(_mActivity, getString(R.string.reservation_desc), () -> {
                NetHelper.getApi()
                        .createOrder(mProduct.getId())
                        .compose(RxUtils.handleResult())
                        .compose(RxUtils.applySchedule())
                        .subscribe(new RxSubscriber<ProductOrder>() {
                            @Override
                            public void _next(ProductOrder productOrder) {
                                ToastHelper.get().showShort("预约成功");
                                setReservation();

                            }
                        });
            });
        }
    }

    private void setReservation() {
        tvReservation.setText(R.string.cancel);
        tvReservation.setTextColor(getResources().getColor(R.color.white));
        tvReservation.setBackgroundResource(R.drawable.shape_circle_red);
    }


}
