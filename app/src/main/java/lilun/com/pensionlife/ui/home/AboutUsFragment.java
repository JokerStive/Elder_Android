package lilun.com.pensionlife.ui.home;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.ConfigUri;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.utils.SystemUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.widget.NormalDialog;
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
    @Bind(R.id.tv_change_ip)
    TextView tvChangeIp;
    @Bind(R.id.tv_change_mqtt)
    TextView tvChangeMqtt;

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
        tvVersion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tvChangeIp.setVisibility(View.VISIBLE);
                tvChangeMqtt.setVisibility(View.VISIBLE);
                return true;
            }
        });
        tvChangeIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIp();
            }
        });

        tvChangeMqtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMqtt();
            }
        });
    }

    public void changeMqtt() {
        new NormalDialog().createEditMessage(_mActivity, "请输入完整的mqtt服务器地址", ConfigUri.BASE_URL, true, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                ConfigUri.MQTT_URL = "tcp://" + input.toString() + ":1883";
                ToastHelper.get().showWareShort("mqtt服务器地址 :" + ConfigUri.MQTT_URL);
            }
        });
    }

    public void changeIp() {
        new NormalDialog().createEditMessage(_mActivity, "请输入完整的ip地址", ConfigUri.BASE_URL, true, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                ConfigUri.BASE_URL = "http://" + input.toString() + "/api/";
                ToastHelper.get().showWareShort("mqtt服务器地址 :" + ConfigUri.BASE_URL);
            }
        });
    }


}
