package lilun.com.pensionlife.ui.home.help;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import lilun.com.pensionlife.BuildConfig;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.utils.RegexUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;

/**
 * 输入第一求助人电话dialog
 *
 * @author yk
 *         create at 2017/1/24 16:05
 *         email : yk_developer@163.com
 */
public class FirstHelperPhoneDialogFragment extends DialogFragment implements View.OnClickListener {


    private EditText etPhone;
    private String firstHelper;

    public static FirstHelperPhoneDialogFragment newInstance(String firstHelper) {
        FirstHelperPhoneDialogFragment fragment = new FirstHelperPhoneDialogFragment();
        Bundle args = new Bundle();
        args.putString("firstHelper", firstHelper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstHelper = StringUtils.checkNotEmpty(getArguments().getString("firstHelper"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_first_helper_phone, container);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && App.widthDP > 820) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void initView(View view) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        TextView tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        String desc = getActivity().getResources().getString(R.string.first_help_phone_desc);
        String format = String.format(desc, TextUtils.isEmpty(firstHelper) ? "" : firstHelper);
        tvDesc.setText(format);

        etPhone = (EditText) view.findViewById(R.id.et_phone);
        view.findViewById(R.id.btn_confirm_phone).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_phone:
                help();
                break;

        }
    }

    private void help() {
        if (checkPhone()) {
            String phone = etPhone.getText().toString();
            //该功能在10106上实现
            if (BuildConfig.VERSION_CODE >= 10106) {
                Account postAccount = new Account();
                postAccount.setProfile(new Account.ProfileBean(User.getLocation(), phone, User.getAddress()));
                NetHelper.getApi()
                        .putAccount(User.getUserId(), postAccount)
                        .compose(RxUtils.handleResult())
                        .compose(RxUtils.applySchedule())
                        .subscribe(new RxSubscriber<Account>() {
                            @Override
                            public void _next(Account account) {
                                User.putHelpPhone(phone);
                                AlarmDialogFragment.newInstance(phone).show(getActivity().getFragmentManager(), AlarmDialogFragment.class.getSimpleName());
                                dismiss();
                            }
                        });
            } else {
                User.putHelpPhone(phone);
                AlarmDialogFragment.newInstance(phone).show(getActivity().getFragmentManager(), AlarmDialogFragment.class.getSimpleName());
                dismiss();
            }
        }
    }

    private boolean checkPhone() {
        String phone = etPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (RegexUtils.checkMobile(phone)) {
                if (TextUtils.equals(phone, User.getMobile())) {
                    ToastHelper.get().showWareShort("不能是自己的手机");
                } else {
                    return true;
                }
            } else {
                ToastHelper.get().showWareShort("手机号格式错误");
            }
        } else {
            ToastHelper.get().showWareShort("手机号不能为空");
        }
        return false;
    }
}
