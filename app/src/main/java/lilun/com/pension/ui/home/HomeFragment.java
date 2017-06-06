package lilun.com.pension.ui.home;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pension.module.bean.Information;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pension.ui.agency.classify.AgencyClassifyFragment;
import lilun.com.pension.ui.announcement.AnnouncementItemFragment;
import lilun.com.pension.ui.change_organization.ChangeOrganizationActivity;
import lilun.com.pension.ui.education.classify.EducationClassifyFragment;
import lilun.com.pension.ui.health.classify.HealthClassifyFragment;
import lilun.com.pension.ui.help.HelpRootFragment;
import lilun.com.pension.ui.home.help.AlarmDialogFragment;
import lilun.com.pension.ui.home.help.HelpProtocolDialogFragment;
import lilun.com.pension.ui.push_info.InformationCenterFragment;
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


    @Bind(R.id.iv_message)
    ImageView ivMessage;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.indicator)
    CircleIndicator indicator;


    @Subscribe
    public void changedOrganization(Event.ChangedOrganization event) {
        tvPosition.setText(User.getCurrentOrganizationName());
        mPresenter.getInformation();
    }
    //============更新用户头像
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNewSetting(Event.AccountSettingChange account){
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, ivAvatar);
    }
    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
        mPresenter.bindView(this);
        mPresenter.getInformation();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, ivAvatar);

        tvPosition.setText(User.getCurrentOrganizationName());

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

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
            // 侧滑菜单
            case R.id.iv_icon:
                switchDrawer();
                break;
            // 社区活动
            case R.id.iv_activities:
                startCommunityActivity();
                break;

            // 邻居互助
            case R.id.iv_help_each:
                startNeighborHelp();
                break;

            // 养老机构
            case R.id.iv_agency:
                startPensionAgency();
                break;

            // 一键求助
            case R.id.iv_help:
                startHelp();
                break;

            //老年教育
            case R.id.iv_education:
                startPensionEducation();
                break;

            // 政务
            case R.id.iv_government:
                startGovernment();
                break;


            // 健康服务
            case R.id.iv_health_service:
                startHealthService();
                break;

            // 居家服务
            case R.id.iv_residential_service:
                startResidentialService();
                break;

            //信息中心
            case R.id.iv_message:
                start(InformationCenterFragment.newInstance());
                break;

            // 切换社区
            case R.id.tv_position:
                if (!TextUtils.isEmpty(User.getRootOrganizationAccountId())) {
                    startActivity(new Intent(_mActivity, ChangeOrganizationActivity.class));
                } else {
                    ToastHelper.get().showWareShort("该账户没有地球村的所属组织");
                }
                break;
        }

    }

    public void switchDrawer() {
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
        start(HelpRootFragment.newInstance(getString(R.string.neighbor_help)));

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
        start(HealthClassifyFragment.newInstance(getString(R.string.government)));
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
    public void showInformation(List<Information> infos) {
        List<BaseFragment> listFragments = new ArrayList<>();
        for (Information announcement : infos) {
            AnnouncementItemFragment fragment = AnnouncementItemFragment.newInstance(announcement);
            listFragments.add(fragment);
        }
        viewPager.setAdapter(new ViewPagerFragmentAdapter(_mActivity.getSupportFragmentManager(), listFragments));
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });
    }

    @Override
    public void changeOrganizationSuccess(int clickId) {
        //需要切换默认所属组织之后
        switch (clickId) {
            case R.id.iv_agency:
                Logger.d("切换组织成功" + User.getCurrentOrganizationId());
//                start(AgencyClassifyFragment.newInstance(getClassifyAnnouncements(getString(R.string.pension_agency))));
                break;
            case -1:
                User.putCurrentOrganizationId(User.getBelongsOrganizationId());
                tvPosition.setText(User.getCurrentOrganizationName());
        }
    }


}
