package lilun.com.pensionlife.ui.home;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.SystemUtils;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 关于我们
 *
 * @author yk
 *         create at 2017/7/14 10:15
 *         email : yk_developer@163.com
 */
public class AboutUsFragment extends BaseFragment {

    @Bind(R.id.iv_qr_code)
    ImageView ivQrCode;

    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;

    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        tvVersion.setText(SystemUtils.getVersionName(getContext()));
    }

}
