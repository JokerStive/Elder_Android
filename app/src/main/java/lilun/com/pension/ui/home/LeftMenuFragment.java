package lilun.com.pension.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.utils.PreUtils;
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

    @Bind(R.id.tv_account_data)
    TextView tvAccountData;

    @Bind(R.id.tv_account_setting)
    TextView tvAccountSetting;

    @Bind(R.id.tv_account_share)
    TextView tvAccountShare;

    @Bind(R.id.tv_account_info)
    TextView tvAccountInfo;

    @Bind(R.id.tv_account_safe)
    TextView tvAccountSafe;

    @Bind(R.id.tv_logout)
    TextView tvLogout;

    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.tv_name)
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
        ImageLoaderUtil.instance().loadImage(IconUrl.account(User.getUserId(),null),R.drawable.avatar,ivAvatar);
        tvName.setText(User.getName());

        tvAccountData.setOnClickListener(this);
        tvAccountSetting.setOnClickListener(this);
        tvAccountShare.setOnClickListener(this);
        tvAccountInfo.setOnClickListener(this);
        tvAccountSafe.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        new MaterialDialog.Builder(_mActivity)
//                .title(R.string.logout_confirm)
                .content(R.string.logout_confirm)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive((dialog, which) -> {
                    PreUtils.clear();
                    startActivity(new Intent(_mActivity, WelcomeActivity.class));
                })
                .onNegative((dialog1, which1) -> dialog1.dismiss())
                .show();

    }


}
