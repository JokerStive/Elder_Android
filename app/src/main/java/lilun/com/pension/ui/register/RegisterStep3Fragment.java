package lilun.com.pension.ui.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.utils.ToastHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * 验证码 验证
 * Created by zp on 2017/4/13.
 */

public class RegisterStep3Fragment extends BaseFragment<RegisterContract.PresenterStep3>
        implements RegisterContract.ViewStep3 {

    private final int REGET_TIME = 60;
    CompositeSubscription cntDownRx;

    Account account;
    String IDCode;
    @Bind(R.id.ll_input)
    LinearLayout llRegisterName;
    @Bind(R.id.tv_input_title)
    TextView tvInputTitle;
    @Bind(R.id.actv_show_count_down)
    AppCompatTextView actvShowCntDown;
    @Bind(R.id.acet_input)
    AppCompatEditText acetRegisterCode;

    @OnClick({R.id.fab_go_next, R.id.actv_show_count_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_go_next:
                IDCode = acetRegisterCode.getText().toString().trim();
                if (checkRegisterCode(IDCode)) {
                    mPresenter.checkIDCode(_mActivity, account.getMobile(), IDCode);
                }
                break;
            case R.id.actv_show_count_down:
                if (actvShowCntDown.getText().toString().trim().equals(getString(R.string.reget))) {
                    mPresenter.getIDCode(_mActivity, account.getMobile());
                    return;
                }

                break;
        }
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep3Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step1;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        tvInputTitle.setText(R.string.register_code);
        acetRegisterCode.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        actvShowCntDown.setVisibility(View.VISIBLE);
        startCnt();
    }


    private boolean checkRegisterCode(String IDCode) {
        if (IDCode.length() != 6) {
            ToastHelper.get(getContext()).showWareShort("请输入6位验证码");
            return false;
        }
        return true;
    }

    private void goStep4() {
        ((RegisterActivity) _mActivity).setTitle();
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);
        bundle.putString("IDCode", IDCode);
        RegisterStep4Fragment fragmentStep4 = new RegisterStep4Fragment();
        fragmentStep4.setArguments(bundle);
        _mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentStep4)
                .setCustomAnimations(R.anim.pop_container_in, R.anim.pop_container_out)
                .addToBackStack("")
                .commit();
    }

    @Override
    public void successOfCheckIDCode(Boolean success) {
        if (!success) {
            ToastHelper.get(getContext()).showWareShort("验证码错误");
            return;
        }
        goStep4();

    }

    @Override
    public void successOfIDCode() {
        startCnt();
    }

    public void startCnt() {
        if (cntDownRx == null)
            cntDownRx = new CompositeSubscription();
        cntDownRx.add(Observable.timer(0, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return REGET_TIME - aLong;
                    }
                })
                .take(REGET_TIME + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    Log.d("zp", res + "");

                    if (res == 0) {
                        stop();

                        actvShowCntDown.setText(getString(R.string.reget));
                        actvShowCntDown.setBackgroundResource(R.drawable.shape_rect_red_corner_5);

                    } else {
                        actvShowCntDown.setText(getString(R.string.count_down_seconds, res + ""));
                        actvShowCntDown.setBackgroundResource(R.drawable.shape_rect_gray_corner_5);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void stop() {
        if (cntDownRx != null && !cntDownRx.isUnsubscribed()) {
            cntDownRx.unsubscribe();

            cntDownRx = null;
        }
    }
}
