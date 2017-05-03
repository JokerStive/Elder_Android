package lilun.com.pension.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.ui.welcome.WelcomeActivity;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;

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

    @Bind(R.id.tv_product_name)
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
        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), null), R.drawable.icon_def, ivAvatar);
        tvName.setText(User.getName());
    }


    @OnClick({R.id.tv_logout, R.id.tv_account_info})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                logout();
                break;

            case R.id.tv_account_info:
//                start(InformationCenterFragment.newInstance());
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
        if (fragment!=null){
            fragment.switchDrawer();
        }
    }


}
