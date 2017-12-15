package lilun.com.pensionlife.ui.home;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.BuildConfig;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.ConfigUri;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.adapter.ViewPagerFragmentAdapter;
import lilun.com.pensionlife.module.bean.ActivityCategory;
import lilun.com.pensionlife.module.bean.AppVersion;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.bean.QuestionNaire;
import lilun.com.pensionlife.module.utils.CacheMsgClassify;
import lilun.com.pensionlife.module.utils.PreUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.VersionCheck;
import lilun.com.pensionlife.module.utils.mqtt.MqttNotificationExtra;
import lilun.com.pensionlife.module.utils.mqtt.MqttTopic;
import lilun.com.pensionlife.ui.WebActivity;
import lilun.com.pensionlife.ui.activity.activity_list.ActivityListFragment;
import lilun.com.pensionlife.ui.activity.classify.ActivityClassifyFragment;
import lilun.com.pensionlife.ui.agency.classify.AgencyClassifyFragment;
import lilun.com.pensionlife.ui.announcement.AnnouncementItemFragment;
import lilun.com.pensionlife.ui.change_organization.ChangeOrganizationActivity;
import lilun.com.pensionlife.ui.education.classify.EducationClassifyFragment;
import lilun.com.pensionlife.ui.health.classify.HealthClassifyFragment;
import lilun.com.pensionlife.ui.help.HelpRootFragment;
import lilun.com.pensionlife.ui.home.help.AlarmDialogFragment;
import lilun.com.pensionlife.ui.home.help.HelpProtocolDialogFragment;
import lilun.com.pensionlife.ui.home.personal_setting.PersonalSettingFragment;
import lilun.com.pensionlife.ui.push_info.CacheInfoListActivity;
import lilun.com.pensionlife.ui.push_info.InformationCenterFragment;
import lilun.com.pensionlife.ui.residential.classify.ResidentialClassifyFragment;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import me.relex.circleindicator.CircleIndicator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 首页V
 *
 * @author yk
 *         create at 2017/2/6 16:48
 *         email : yk_developer@163.com
 *         2017/6/30 进入主页后，发送请求获取最近版本，判断是否弹出升级框，使用外部浏览器下载新版本；
 *         问卷星：http://www.wjx.cn/jq/3795229.aspx?sojumpparm=userid
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements View.OnClickListener, EasyPermissions.PermissionCallbacks, HomeContract.View {
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

    @Bind(R.id.iv_get_prize)
    ImageView ivGetPrize;



    @Subscribe
    public void changedOrganization(Event.ChangedOrganization event) {
        tvPosition.setText(User.getCurrentOrganizationName());
        mPresenter.getInformation();
    }

    //============更新用户头像
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNewSetting(Event.AccountSettingChange account) {
        ImageLoaderUtil.instance().loadAvatar(User.getUserId(), ivAvatar);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new HomePresenter();
        mPresenter.bindView(this);
        mPresenter.getInformation();
        mPresenter.getVersionInfo(Constants.appName, Constants.version_latest);
        mPresenter.getQuestionNaire();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        if (Constants.unSettingLocation.equals(User.getLocation())) {
            new NormalDialog().createShowMessage(_mActivity, "您还未设置默认小区", "去设置", () -> {
                start(PersonalSettingFragment.newInstance(PersonalSettingFragment.UN_SETTING));
            }, getString(R.string.cancel), false);
        }
        ImageLoaderUtil.instance().loadAvatar(User.getUserId(), ivAvatar);

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
        mPresenter.needChangeToDefOrganization();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

            // 紧急求助
            case R.id.iv_help:
                //  startHelp();
                locationPermission();
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
     * 紧急求助
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

    /**
     * 显示版本升级信息
     *
     * @param version
     */
    @Override
    public void showVersionInfo(AppVersion version) {
        if (version == null) return;
        if (VersionCheck.compareVersion(BuildConfig.VERSION_NAME, version.getVersion()))
            VersionDialogFragment.newInstance(version).show(_mActivity.getFragmentManager(), VersionDialogFragment.class.getSimpleName());
    }

    @Override
    public void saveQuestionNaire(QuestionNaire questionNaire) {
        showQuestionNaire(questionNaire);
    }

    public void showQuestionNaire(QuestionNaire questionNaire) {
        if (questionNaire == null || questionNaire.getPrizedraw() == null) return;
        Date startDate = StringUtils.string2Date(questionNaire.getPrizedraw().getStartTime());
        Date endDate = StringUtils.string2Date(questionNaire.getPrizedraw().getEndTime());
        if (startDate == null || endDate == null) return;


        if (new Date().getTime() < startDate.getTime() || new Date().getTime() > endDate.getTime())
            ivGetPrize.setVisibility(View.GONE);
        else {
            ivGetPrize.setVisibility(View.VISIBLE);
            ivGetPrize.setOnClickListener(v -> {
                String url = ConfigUri.LOTTERY_BASE_URL + "/Lotterys/myLottery" +
                        "?token=" + User.getToken() +
                        "&accountId=" + User.getUserId() +
                        "&organizationActivityId=" + questionNaire.getPrizedraw().getId()+
                        "&sIP=" + ConfigUri.BASE_URL.replace("/api/","");
                Logger.d(url);
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", "兑奖详情");
                startActivity(intent);
            });
        }
    }

    @Subscribe
    public void mqttNotificationOperate(MqttNotificationExtra extra) {
        String topic = extra.getTopic();
        if (!TextUtils.isEmpty(topic)) {
            MqttTopic mqttTopic = new MqttTopic();
            if (topic.contains(mqttTopic.topic_activity_suffix)) {
                String activityCatrgoryId = extra.getId();
                ActivityCategory activityCategory = new ActivityCategory();
                activityCategory.setId(activityCatrgoryId);
                start(ActivityListFragment.newInstance(activityCategory));
            } else if (topic.contains(mqttTopic.topic_information_edit)) {
                Intent intent = new Intent(_mActivity, CacheInfoListActivity.class);
                intent.putExtra("title", "社区公告");
                intent.putExtra("classify", CacheMsgClassify.announce);
                startActivity(intent);
            } else if (topic.equals(mqttTopic.urgent_help)) {
                Intent intent = new Intent(_mActivity, CacheInfoListActivity.class);
                intent.putExtra("title", "紧急求助");
                intent.putExtra("classify", CacheMsgClassify.urgent_help);
                startActivity(intent);
            }
        }
    }

    public void startFragment(BaseFragment targetFragment) {
        start(targetFragment);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //    startHelp();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        new NormalDialog().createNormal(getActivity(), "您没有给予定位权限，可能导致功能不全，要继续么？", new NormalDialog.OnPositiveListener() {
            @Override
            public void onPositiveClick() {
                startHelp();
            }
        });
    }

    private static final int RC_CAMERA_AND_LOCATION = 124;


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    public void locationPermission() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            startHelp();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_permission),
                    RC_CAMERA_AND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
}
