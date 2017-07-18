package lilun.com.pensionlife.ui.register;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Register;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.ui.welcome.LoginModule;
import lilun.com.pensionlife.widget.InputView;
import lilun.com.pensionlife.widget.NormalTitleBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zp on 2017/7/17.
 */

public class RegisterAccountFragment extends BaseFragment<RegisterContract.PresenterAccount>
        implements RegisterContract.ViewAccount {

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.iv_register_nickname)
    InputView ivRegisterNickname;
    @Bind(R.id.iv_register_moblie)
    InputView ivRegisterMoblie;
    @Bind(R.id.iv_register_verif)
    InputView ivRegisterVerif;
    @Bind(R.id.iv_register_password_first)
    InputView ivRegisterPasswordFrist;
    @Bind(R.id.iv_register_password_second)
    InputView ivRegisterPasswordSecond;
    @Bind(R.id.bt_register)
    Button btRegister;

    private final int REGET_TIME = 60;
    CompositeSubscription cntDownRx;

    String nickname = "";
    String mobile = "";
    String verifCode = "";
    String password = "";

    @Override
    protected void initPresenter() {
        mPresenter = new RegisterAccountPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_account;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(() -> {
            _mActivity.finish();
        });
        ivRegisterNickname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ivRegisterMoblie.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ivRegisterVerif.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ivRegisterPasswordFrist.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        ivRegisterPasswordSecond.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ivRegisterNickname.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        ivRegisterMoblie.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_NUMBER);
        ivRegisterVerif.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_CLASS_NUMBER);

        ivRegisterVerif.setBtListener((v) -> {
            getIdCode();
        });
        btRegister.setOnClickListener(v -> {
            commitRegister();
        });
        ivRegisterPasswordFrist.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        ivRegisterPasswordSecond.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        ivRegisterPasswordSecond.setOnEditActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editViewEnterButton();
            }
            return false;
        });
    }

    @Override
    public void editViewEnterButton() {
        commitRegister();
    }

    /**
     * 提交注册 ，先验证昵称、手机号、验证码、密码
     */
    private void commitRegister() {

        String password1 = ivRegisterPasswordFrist.getInput().toString().trim();
        String password2 = ivRegisterPasswordSecond.getInput().toString().trim();
        nickname = ivRegisterNickname.getInput().toString().trim();
        mobile = ivRegisterMoblie.getInput().toString().trim();
        verifCode = ivRegisterVerif.getInput().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            ToastHelper.get().showShort("请输入您的昵称");
            return;
        }
        if (!StringUtils.isMobileNumber(mobile)) {
            ToastHelper.get().showShort("请输入正确的手机号码");
            return;
        }

        if (!StringUtils.isVerifCode(verifCode)) {
            ToastHelper.get().showShort("请输入正确的验证码");
            return;
        }
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)) {
            ToastHelper.get().showShort(getString(R.string.new_password_str));
            return;
        }

        if (!password1.equals(password2)) {
            ToastHelper.get().showShort(getString(R.string.password_not_equal));
            return;
        }
        password = password1;
        Account account = new Account();
        account.setName(nickname);
        account.setUsername(mobile);
        account.setMobile(mobile);
        account.setPassword(password);
        mPresenter.postRegisterAccount(verifCode, account);
    }

    /**
     * 获取验证码 先验证手机号码
     */
    private void getIdCode() {
        mobile = ivRegisterMoblie.getInput().toString().trim();
        if (!StringUtils.isMobileNumber(mobile)) {
            ToastHelper.get().showShort("请输入正确的手机号码");
            return;
        }
        mPresenter.getIDCode(mobile, "1");

    }

    @Override
    public void successOfIDCode() {
        startCnt();
    }

    @Override
    public void successOfRegisterAccount(Register register) {
        ToastHelper.get().showShort("注册成功");
        new LoginModule().putToken(register.getId());
        startWithPop(RegisterInfoFragment.newInstance(password));
    }


    /**
     * 开始倒计时
     */
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
                    if (res == 0) {
                        stop();
                        ivRegisterVerif.setButtonClickable(true);
                        ivRegisterVerif.setButtonText(getString(R.string.reget));
                        ivRegisterVerif.setButtonDrawable(R.drawable.shape_rect_red_corner_5);

                    } else {

                        ivRegisterVerif.setButtonClickable(false);
                        ivRegisterVerif.setButtonText(getString(R.string.count_down_seconds, res + ""));
                        ivRegisterVerif.setButtonDrawable(R.drawable.shape_rect_gray_corner_5);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    /**
     * 倒计时结束后置空
     */
    public void stop() {
        if (cntDownRx != null && !cntDownRx.isUnsubscribed()) {
            cntDownRx.unsubscribe();

            cntDownRx = null;
        }
    }
}
