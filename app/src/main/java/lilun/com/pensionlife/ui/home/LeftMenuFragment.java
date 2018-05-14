package lilun.com.pensionlife.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.contact.ContactListFragment;
import lilun.com.pensionlife.ui.home.info_setting.InfoSettingFragment;
import lilun.com.pensionlife.ui.home.personal_setting.PersonalActivity;
import lilun.com.pensionlife.ui.order.OrderListFragment;
import lilun.com.pensionlife.ui.push_info.InformationCenterFragment;
import lilun.com.pensionlife.ui.welcome.LoginActivity;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;

/**
 * 左侧侧滑菜单
 *
 * @author yk
 *         create at 2017/2/8 10:14
 *         email : yk_developer@163.com
 */
public class LeftMenuFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tv_logout)
    TextView tvLogout;

    @Bind(R.id.iv_icon)
    ImageView ivAvatar;

    @Bind(R.id.tv_sophisticated)
    TextView tvName;

    @Bind(R.id.tv_manage_order)
    TextView tvManageOrder;


    @Override
    protected void initPresenter() {

    }

    @Override


    protected int getLayoutId() {
        return R.layout.fragment_sliding_menu;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        ImageLoaderUtil.instance().loadAvatar(User.getUserId(), ivAvatar);
        tvName.setText(User.getName());
    }


    @OnClick({R.id.iv_icon,R.id.tv_my_organization, R.id.tv_logout, R.id.tv_contact_setting, R.id.tv_account_data, R.id.tv_account_info, R.id.tv_info_setting, R.id.tv_about_us, R.id.tv_manage_order, R.id.tv_my_order})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                logout();
                break;
            case R.id.tv_account_data:
                startActivity(new Intent(_mActivity, PersonalActivity.class));
                break;

            case R.id.tv_account_info:
                startTargetFragment(InformationCenterFragment.newInstance());
                break;

            case R.id.tv_my_order:
                startTargetFragment(OrderListFragment.newInstance());
                break;


            case R.id.tv_manage_order:
//                startTargetFragment(new MerchantOrderListFragment());
                break;

            case R.id.tv_about_us:
                startTargetFragment(new AboutUsFragment());
                break;

            case R.id.tv_info_setting:
                startTargetFragment(new InfoSettingFragment());
                break;

            case R.id.tv_contact_setting:
                startTargetFragment(ContactListFragment.newInstance());
                break;

            case R.id.tv_my_organization:
                startActivity(new Intent(_mActivity, MyJoinedOrganizationsActivity.class));
                break;
        }
    }


    private void startTargetFragment(BaseFragment targetFragment) {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof HomeFragment) {
            ((HomeFragment) parentFragment).startFragment(targetFragment);
        }
    }


    /**
     * 退出登录
     */
    private void logout() {
        new MaterialDialog.Builder(_mActivity)
                .content(R.string.logout_confirm)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    changeOrganization();
                })
                .onNegative((dialog1, which1) -> dialog1.dismiss())
                .show();

    }

    private void changeOrganization() {
        if (!User.getCurrentOrganizationId().equals(User.getBelongsOrganizationId())) {
            Account account = new Account();
            account.setDefaultOrganizationId(User.getBelongOrganizationAccountId());
            NetHelper.getApi()
                    .putAccount(User.getUserId(), account)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule())
                    .subscribe(new RxSubscriber<Object>(_mActivity) {
                        @Override
                        public void _next(Object o) {
                            backLogin();
                        }
                    });
        } else {
            backLogin();
        }
    }

    private void backLogin() {
        App.clear();
        startActivity(new Intent(_mActivity, LoginActivity.class));
        _mActivity.finish();
        HomeFragment fragment = findFragment(HomeFragment.class);
        if (fragment != null) {
            fragment.switchDrawer();
        }
    }

    private void personalSetting() {

    }


    //============更新用户名
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showNewSetting(Event.AccountSettingChange account) {
        tvName.setText(User.getName());
        ImageLoaderUtil.instance().loadAvatar(User.getUserId(), ivAvatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
