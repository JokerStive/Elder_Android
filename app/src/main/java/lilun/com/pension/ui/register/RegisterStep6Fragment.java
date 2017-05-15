package lilun.com.pension.ui.register;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseTakePhotoFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.callback.TakePhotoClickListener;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.ui.welcome.LoginActivity;
import lilun.com.pension.ui.welcome.WelcomeActivity;
import lilun.com.pension.widget.CircleImageView;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.TakePhotoDialogFragment;
import rx.Observable;

/**
 * 注册成功后 到登录界面
 * Created by zp on 2017/4/13.
 */

public class RegisterStep6Fragment extends BaseTakePhotoFragment<RegisterContract.PresenterStep6>
        implements RegisterContract.ViewStep6 {
    private FragmentManager fragmentManager;
    private TakePhotoDialogFragment fragment;
    private TakePhotoClickListener listener;

    Account account;  //此时 已注册成功 的
    @Bind(R.id.et_account_id)
    EditText etAccountId;
    @Bind(R.id.et_image_name)
    EditText etImageName;
    @Bind(R.id.et_token)
    EditText etToken;
    @Bind(R.id.civ_account_avatar)
    CircleImageView civAccountAvatar;
    private String path;

    @OnClick({R.id.civ_account_avatar, R.id.fab_go_next, R.id.bt_token})
    public void onClick(View v) {
        if (v.getId() == R.id.civ_account_avatar) {
            if (fragmentManager != null) {
                fragment = TakePhotoDialogFragment.newInstance();
                fragment.setOnResultListener(this);
                fragment.show(fragmentManager, null);
            }
        } else if (v.getId() == R.id.fab_go_next) {
            //不选择头像
            if (TextUtils.isEmpty(path)) {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_no_ivatar, () -> {
                    successOfUpdateImage();
                });
            } else {
                String imageName = "";
                if (account.getImage() != null && account.getImage().size() > 0) {
                    imageName = account.getImage().get(0).getFileName();
                }

                //   mPresenter.updateImage(account.getId(), imageName, path);
                mPresenter.updateImage(_mActivity, User.getUserId(), imageName, path);
            }
        } else if (v.getId() == R.id.bt_token) {
            PreUtils.putString(User.token, etToken.getText().toString().trim());
            Log.d("zp", User.getToken());
        }


    }

    private String getImageName() {

        return etImageName.getText().toString().trim();
    }

    private String getAccountId() {
        return etAccountId.getText().toString().trim();
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
    }


    @Override
    protected void initPresenter() {
        mPresenter = new RegisterStep6Presenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_step6;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        fragmentManager = _mActivity.getFragmentManager();
        _mActivity.findViewById(R.id.iv_back).setVisibility(View.GONE);
    }

    @Override
    public void successOfUpdateImage() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("autologin", true);
        intent.putExtra("account", account);
        startActivity(intent);
        _mActivity.finish();
        if (WelcomeActivity.welcomeActivity != null) {
            WelcomeActivity.welcomeActivity.finish();
            WelcomeActivity.welcomeActivity = null;
        }
    }

    @Override
    public void onAlbumClick() {
        getTakePhoto().onPickMultiple(1);
    }

    @Override
    protected void onTakePhotoSuccess(TResult tResult) {
        List<TakePhotoResult> results = new ArrayList<>();
        for (TImage tImage : tResult.getImages()) {
            TakePhotoResult result1 = TakePhotoResult.of(tImage.getOriginalPath(), tImage.getCompressPath(), tImage.getFromType(), TakePhotoResult.TYPE_PHOTO);
            results.add(result1);
        }
        Observable.just("")
                .compose(RxUtils.applySchedule())
                .subscribe(s -> {
                    TakePhotoResult result = results.get(0);

                    if (result.getFrom() == TImage.FromType.CAMERA) {
                        path = result.getOriginalPath();
                    } else {
                        path = result.getOriginalPath();
                    }
                    Glide.with(App.context).load(path)
                            .error(R.drawable.icon_def)
                            .into(civAccountAvatar);

                });
    }

    @Override
    protected ArrayList<String> getPhotoData() {
        return super.getPhotoData();
    }

}
