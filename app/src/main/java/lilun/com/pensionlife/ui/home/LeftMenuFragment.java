package lilun.com.pensionlife.ui.home;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.IconUrl;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.welcome.WelcomeActivity;
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

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sliding_menu;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        //    ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null), R.drawable.icon_def, ivAvatar);
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, ivAvatar);
        tvName.setText(User.getName());
    }


    @OnClick({R.id.tv_logout, R.id.tv_account_data, R.id.tv_account_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                logout();
                break;
            case R.id.tv_account_data:
                startActivity(new Intent(_mActivity, PersonalActivity.class));
                break;

            case R.id.tv_account_info:
                Fragment parentFragment = getParentFragment();
                if(parentFragment instanceof HomeFragment) {
                    ((HomeFragment)parentFragment).startInformationCenterFragment();
                }
                break;
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
        startActivity(new Intent(_mActivity, WelcomeActivity.class));
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
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, ivAvatar);
    }
}