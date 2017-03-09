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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.iv_position)
    ImageView ivPosition;

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

        ImageLoaderUtil.instance().loadImage(IconUrl.account(User.getUserId(), null), R.drawable.avatar, ivAvatar);

        tvPosition.setText(User.getCurrentOrganizationName());

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
//        drawer.setScrimColor(Color.TRANSPARENT);

        ivAvatar.setOnClickListener(this);
        ivActivities.setOnClickListener(this);
        ivAgency.setOnClickListener(this);
        ivEducation.setOnClickListener(this);
        ivHealthService.setOnClickListener(this);
        ivHelp.setOnClickListener(this);
        ivHelpEach.setOnClickListener(this);
        ivResidentialService.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
//                View mContent = drawer.getChildAt(0);
//                View mMenu = drawerView;
//                float scale = 1 - slideOffset;
//                float rightScale = 0.8f + scale * 0.2f;
//                if (drawerView.getTag().equals("LEFT")) {

//                    float leftScale = 1 - 0.3f * scale;

//                    ViewHelper.setScaleX(mMenu, leftScale);
//                    ViewHelper.setScaleY(mMenu, leftScale);
//                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
//                    ViewHelper.setTranslationX(mContent,
//                            mMenu.getMeasuredWidth() * (1 - scale));
//                    ViewHelper.setPivotX(mContent, 0);
//                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
//                    mContent.invalidate();
//                    ViewHelper.setScaleX(mContent, rightScale);
//                    ViewHelper.setScaleY(mContent, rightScale);
//                }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                //TODO 侧滑菜单
//               drawer.stol
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
//                startActivity(new Intent(_mActivity, ChatActivity.class));
                startHelp();
                break;

            case R.id.iv_education:
                //TODO 老年教育
                startPensionEducation();
                break;

            case R.id.iv_health_service:
                //TODO 健康服务
                startHealthService();
                break;

            case R.id.iv_residential_service:
                //TODO 居家服务
                startResidentialService();
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
        start(HelpRootFragment.newInstance(getClassifyAnnouncements(getString(R.string.neighbor_help))));

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

            if (mInformationData.size()==0){
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
        start(EducationClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.pension_education))));
    }


    /**
     * 健康服务
     */
    private void startHealthService() {
        start(HealthClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.health_service))));
    }

    /**
     * 社区活动
     */
    private void startCommunityActivity() {
        start(ActivityClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.community_activity))));
    }

    /**
     * 养老机构
     */
    private void startPensionAgency() {
        start(AgencyClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.pension_agency))));
    }


    /**
     * 居家服务
     */
    private void startResidentialService() {
        start(ResidentialClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.residential_service))));
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
            if (data.size()==0){
                Information information = new Information();
                data.add(information);
            }
            setInformation(data);
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
