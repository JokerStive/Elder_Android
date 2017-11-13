package lilun.com.pensionlife.ui.register;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseTakePhotoFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.TakePhotoResult;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.ui.welcome.LoginActivity;
import lilun.com.pensionlife.widget.CircleImageView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.TakePhotoDialogFragment;
import rx.Observable;

/**
 * 注册成功后 finish回登录界面并自动登录
 * Created by zp on 2017/4/13.
 */

public class RegisterAvatorFragment extends BaseTakePhotoFragment<RegisterContract.PresenterAvator>
        implements RegisterContract.ViewAvator {
    private FragmentManager fragmentManager;
    private TakePhotoDialogFragment fragment;

    Account account;  //此时 已注册成功 的
    @Bind(R.id.civ_account_avatar)
    CircleImageView civAccountAvatar;
    private String path;

    @OnClick({R.id.civ_account_avatar, R.id.iv_commit})
    public void onClick(View v) {
        if (v.getId() == R.id.civ_account_avatar) {
            if (fragmentManager != null) {
                fragment = TakePhotoDialogFragment.newInstance();
                fragment.setOnResultListener(this);
                fragment.show(fragmentManager, null);
            }
        } else if (v.getId() == R.id.iv_commit) {
            //不选择头像
            if (TextUtils.isEmpty(path)) {
                new NormalDialog().createNormal(_mActivity, R.string.confirm_no_ivatar, () -> {
                    successOfUpdateImage();
                });
            } else {
                String imageName = "";
                if (account.getImage() != null && account.getImage().size() > 0) {
                    // TODO
//                    imageName = account.getImage().get(0).getFileName();
                }
                mPresenter.updateImage(User.getUserId(), imageName, path);
            }
        }
    }

    public static RegisterAvatorFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putSerializable("account", account);
        RegisterAvatorFragment fragment = new RegisterAvatorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getTransferData(Bundle arguments) {
        account = (Account) arguments.getSerializable("account");
    }


    @Override
    protected void initPresenter() {
        mPresenter = new RegisterAvatorPresenter();
        mPresenter.bindView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_avator;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        fragmentManager = _mActivity.getFragmentManager();
    }

    @Override
    public void successOfUpdateImage() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.putExtra("autologin", true);
        intent.putExtra("account", account);
        _mActivity.setResult(Activity.RESULT_OK, intent);
        _mActivity.finish();
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
