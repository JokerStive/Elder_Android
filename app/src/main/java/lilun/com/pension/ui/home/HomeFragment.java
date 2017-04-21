package lilun.com.pension.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pension.ui.agency.classify.AgencyClassifyFragment;
import lilun.com.pension.ui.announcement.AnnouncementItemFragment;
import lilun.com.pension.ui.education.classify.EducationClassifyFragment;
import lilun.com.pension.ui.health.classify.HealthClassifyFragment;
import lilun.com.pension.ui.help.HelpRootFragment;
import lilun.com.pension.ui.home.help.AlarmDialogFragment;
import lilun.com.pension.ui.home.help.HelpProtocolDialogFragment;
import lilun.com.pension.ui.personal_center.InformationCenterFragment;
import lilun.com.pension.ui.residential.classify.ResidentialClassifyFragment;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;
import me.relex.circleindicator.CircleIndicator;

/**
 * 首页V
 *
 * @author yk
 *         create at 2017/2/6 16:48
 *         email : yk_developer@163.com
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements View.OnClickListener, HomeContract.View {

    @Bind(R.id.iv_activities)
    ImageView ivActivities;

    @Bind(R.id.iv_help_each)
    ImageView ivHelpEach;

    @Bind(R.id.iv_agency)
    ImageView ivAgency;

    @Bind(R.id.iv_help)
    ImageView ivHelp;

    @Bind(R.id.iv_education)
    ImageView ivEducation;

    @Bind(R.id.iv_health_service)
    ImageView ivHealthService;

    @Bind(R.id.iv_residential_service)
    ImageView ivResidentialService;

    @Bind(R.id.drawer)
    DrawerLayout drawer;

    @Bind(R.id.tv_position)
    TextView tvPosition;

    @Bind(R.id.iv_icon)
    ImageView ivAvatar;

//    @Bind(R.id.iv_position)
//    ImageView ivPosition;

    @Bind(R.id.iv_message)
    ImageView ivMessage;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    //    @Bind(R.id.vp_container)
//    ViewPager viewPager;
//
    @Bind(R.id.indicator)
    CircleIndicator indicator;
//
//    @Bind(R.id.rv_push_info)
//    RecyclerView rvPushInfo;


    private List<Information> informations;
    private ArrayList<Information> mInformationData;


    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
        mPresenter.bindView(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null), R.drawable.icon_def, ivAvatar);

        tvPosition.setText(User.getCurrentOrganizationName());

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        ivAvatar.setOnClickListener(this);
        ivActivities.setOnClickListener(this);
        ivAgency.setOnClickListener(this);
        ivEducation.setOnClickListener(this);
        ivHealthService.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        ivHelpEach.setOnClickListener(this);
        ivResidentialService.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getInformation();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("当前默认组织id = " + User.getBelongsOrganizationId());
        Logger.d("当前组织id = " + User.getCurrentOrganizationId());
        mPresenter.needChangeToDefOrganization();
    }

    @OnClick({R.id.iv_icon, R.id.iv_activities, R.id.iv_help_each, R.id.iv_agency, R.id.iv_help, R.id.iv_education,
            R.id.iv_health_service, R.id.iv_residential_service, R.id.iv_message, R.id.tv_position, R.id.iv_government

    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                //TODO 侧滑菜单
                switchDrawer();
                break;
            case R.id.iv_activities:
                //TODO 社区活动
                startCommunityActivity();
                break;

            case R.id.iv_help_each:
                //TODO 邻居互助
                startNeighborHelp();
                break;

            case R.id.iv_agency:
                //TODO 养老机构
                startPensionAgency();
                break;

            case R.id.iv_help:
                //TODO 一键求助
                startHelp();
                break;

            case R.id.iv_education:
                //TODO 老年教育
                startPensionEducation();
                break;

            case R.id.iv_government:
                //TODO 政务
                startGovernment();
                break;


            case R.id.iv_health_service:
                //TODO 健康服务
                startHealthService();
                break;

            case R.id.iv_residential_service:
                //TODO 居家服务
                startResidentialService();
                break;

            //信息中心
            case R.id.iv_message:
                start(InformationCenterFragment.newInstance());
                break;

            // 切换社区
            case R.id.tv_position:
//                startActivity(new Intent(_mActivity, ChangeOrganizationActivity.class));
                break;
        }

    }

    private void switchDrawer() {
        if (!drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.openDrawer(Gravity.LEFT);
        } else {
            drawer.closeDrawer(Gravity.LEFT);
        }

    }

    /**
     * 邻居互助
     */
    private void startNeighborHelp() {
//        start(HelpRootFragment.newInstance(getClassifyAnnouncements(getString(R.string.neighbor_help))));
        start(HelpRootFragment.newInstance(getString(R.string.neighbor_help)));

    }


    private List<Information> getClassifyAnnouncements(String moduleName) {
        if (informations != null) {
            if (mInformationData == null) {
                mInformationData = new ArrayList<>();
            }
            mInformationData.clear();
            for (Information info : informations) {
                if (info.getParentId().contains(moduleName)) {
                    mInformationData.add(info);
                }
            }

            if (mInformationData.size() == 0) {
                Information information = new Information();
                mInformationData.add(information);
            }
            return mInformationData;
        }
        return null;
    }


    /**
     * 老年教育
     */
    private void startPensionEducation() {
        start(EducationClassifyFragment.newInstance(getString(R.string.pension_education)));
    }


    /**
     * 政务
     */
    private void startGovernment() {
        start(HealthClassifyFragment.newInstance(getString(R.string.health_service)));
    }

    /**
     * 健康服务
     */
    private void startHealthService() {
        start(HealthClassifyFragment.newInstance(getString(R.string.health_service)));
    }

    /**
     * 社区活动
     */
    private void startCommunityActivity() {
        start(ActivityClassifyFragment.newInstance(getString(R.string.community_activity)));
    }

    /**
     * 养老机构
     */
    private void startPensionAgency() {
        start(AgencyClassifyFragment.newInstance(getString(R.string.pension_agency)));
    }


    /**
     * 居家服务
     */
    private void startResidentialService() {
        start(ResidentialClassifyFragment.newInstance(getString(R.string.residential_service)));
    }

    /**
     * 一键求助
     */
    private void startHelp() {
        String phone = PreUtils.getString("firstHelperPhone", "");
        if (TextUtils.isEmpty(phone)) {
            new HelpProtocolDialogFragment().show(_mActivity.getFragmentManager(), HelpProtocolDialogFragment.class.toString());
        } else {
            AlarmDialogFragment.newInstance(phone).show(_mActivity.getFragmentManager(), AlarmDialogFragment.class.getSimpleName());
        }
    }


    @Override
    public void showInformation(List<Information> informations) {
        this.informations = informations;
        List<Information> data = new ArrayList<>();
        for (Information info : informations) {
            if (info.getParentId().equals(OrganizationChildrenConfig.information() + "/公告")) {
                data.add(info);
            }
        }
        if (data.size() == 0) {
            Information information = new Information();
            data.add(information);
        }
        setInformation(data);
    }

    @Override
    public void changeOrganizationSuccess(int clickId) {
        //需要切换默认所属组织之后
        switch (clickId) {
            case R.id.iv_agency:
                Logger.d("切换组织成功" + User.getCurrentOrganizationId());
//                start(AgencyClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.pension_agency))));
                break;
        }
    }


    private void setInformation(List<Information> informations) {
        List<BaseFragment> listFragments = new ArrayList<>();
        for (Information announcement : informations) {
            AnnouncementItemFragment fragment = AnnouncementItemFragment.newInstance(announcement);
            listFragments.add(fragment);
        }
        viewPager.setAdapter(new ViewPagerFragmentAdapter(_mActivity.getSupportFragmentManager(), listFragments));
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
//                currentPosition = position;
            }
        });

    }


}
