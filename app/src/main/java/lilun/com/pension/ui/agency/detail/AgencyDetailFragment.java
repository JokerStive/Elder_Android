package lilun.com.pension.ui.agency.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.Organization;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.UIUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.agency.list.AgencyServiceListFragment;
import lilun.com.pension.widget.slider.BannerPager;
import rx.Subscription;

/**
 * 养老机构提供的服务详情V
 *
 * @author yk
 *         create at 2017/2/23 13:19
 *         email : yk_developer@163.com
 */
public class AgencyDetailFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Bind(R.id.tv_reservation)
    TextView tvReservation;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.tv_desc)
    TextView tvDesc;

    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Bind(R.id.tv_mobile)
    TextView tvPrice;


    @Bind(R.id.tv_environment_title)
    TextView tvAddressTitle;

    @Bind(R.id.tv_environment)
    TextView tvAddress;

    @Bind(R.id.rb_bar)
    RatingBar rbBar;

    @Bind(R.id.tv_provide_service)
    TextView tvProvideService;
    @Bind(R.id.iv_icon)
    BannerPager banner;
    @Bind(R.id.tv_introduction_title)
    TextView tvIntroductionTitle;
    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;
    @Bind(R.id.tv_equipment_title)
    TextView tvRequirementTitle;
    @Bind(R.id.tv_equipment)
    TextView tvRequirement;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    private Organization mAgency;
    private String mId;
    private Subscription subscription;


    public static AgencyDetailFragment newInstance(@Nullable String id, Organization agency) {
        AgencyDetailFragment fragment = new AgencyDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("agency", agency);
        args.putString("id", id);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mAgency = (Organization) arguments.getSerializable("agency");
        mId = arguments.getString("id");
        if (TextUtils.isEmpty(mId)) {
            Preconditions.checkNull(mAgency);
            Preconditions.checkNull(mAgency.getDescription());
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agency_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //加粗
        UIUtils.setBold(tvTitle);
        UIUtils.setBold(tvIntroductionTitle);
        UIUtils.setBold(tvRequirementTitle);
        UIUtils.setBold(tvAddressTitle);
        UIUtils.setBold(tvProvideService);

        if (mAgency != null) {
            setData();
        }


        //事件
        tvReservation.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvProvideService.setOnClickListener(this);

    }

    private void setData() {
        Organization.DescriptionBean description = mAgency.getDescription();

        //显示
        tvTitle.setText(mAgency.getName());
//        tvDesc.setText(StringUtils.filterNull(description.getDescription()));
        tvPhone.setText(String.format(getString(R.string.format_phone), "18012325354"));
//        tvPhone.setText(String.format(getString(R.string.format_bedsCount), StringUtils.filterNull(description.getBedsCount())));
        tvPrice.setText(String.format("价格区间：%1$s元——%2$s元", description.getChargingStandard().getMin(), description.getChargingStandard().getMax()));
        tvIntroduction.setText(StringUtils.filterNull(description.getDescription()));
        tvRequirement.setText(StringUtils.filterNull(StringUtils.filterNull(description.getRequirements())));
        tvAddress.setText(StringUtils.filterNull(description.getAdress()));

        rbBar.setRating(description.getRanking());

        setIcon();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (mAgency == null) {
            subscription = NetHelper.getApi().getOrganizationById(mId)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Organization>(_mActivity) {
                        @Override
                        public void _next(Organization agency) {
                            if (agency != null && agency.getDescription() != null) {
                                mAgency = agency;
                                setData();
                            }
                        }
                    });
        }

        //图片
        setIcon();
    }

    private void setIcon() {
        if (mAgency != null) {
            List<String> urls = new ArrayList<>();
            if (mAgency.getIcon() != null) {
                for (IconModule iconModule : mAgency.getIcon()) {
                    String url = IconUrl.moduleIconUrl(IconUrl.Organizations, mAgency.getId(), iconModule.getFileName());
                    urls.add(url);
                }
            } else {
                String url = IconUrl.moduleIconUrl(IconUrl.Organizations, mAgency.getId(), null);
                urls.add(url);
            }
            banner.setData(urls);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                //TODO 预约
                pop();
                break;
            case R.id.tv_reservation:
                //TODO 预约
                break;

            case R.id.tv_provide_service:
                //TODO 进入提供的服务列表页面
                start(AgencyServiceListFragment.newInstance(mAgency.getName(), mAgency.getId(), 1));
                break;


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
