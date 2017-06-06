package lilun.com.pension.ui.home;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;
import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.IconUrl;
import lilun.com.pension.app.User;
import lilun.com.pension.base.BaseTakePhotoFragment;
import lilun.com.pension.module.bean.Account;
import lilun.com.pension.module.bean.Area;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.callback.TakePhotoClickListener;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.CircleImageView;
import lilun.com.pension.widget.NormalDialog;
import lilun.com.pension.widget.NormalTitleBar;
import lilun.com.pension.widget.TakePhotoDialogFragment;
import lilun.com.pension.widget.image_loader.ImageLoaderUtil;
import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zp on 2017/6/5.
 */

public class PersonalSettingFragment extends BaseTakePhotoFragment implements DataInterface<BaseBean>, OnAddressSelectedListener {
    final int RECYCLERLEVEL = 3;
    int curLevel = -1;
    private FragmentManager fragmentManager;
    private TakePhotoDialogFragment fragment;
    BottomDialog dialog;
    BaseBean area, distrect;

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.civ_account_avatar)
    CircleImageView civAccountAvatar;
    @Bind(R.id.tv_nickname)
    TextView tvNickName;
    @Bind(R.id.tv_belong_area)
    TextView tvBelongArea;
    @Bind(R.id.tv_belong_stress)
    TextView tvBelongStress;
    @Bind(R.id.tv_first_help_phone)
    TextView tvFirstHelpPhone;

    private String areaStr = "", stressStr = "";


    @OnClick({R.id.ll_account_avatar, R.id.ll_nickname, R.id.ll_belong_area, R.id.ll_belong_stress, R.id.ll_first_help_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_account_avatar:
                settingOfIvatar();
                break;
            case R.id.ll_nickname:
                settingOfNickName();
                break;
            case R.id.ll_belong_area:
                settingOfArea();
                break;
            case R.id.ll_belong_stress:
                settingOfStress();
                break;
            case R.id.ll_first_help_phone:
                settingOfFirstPhone();
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_setting;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initView();
    }

    @Override
    protected void initPresenter() {

    }


    protected void initView() {
        fragmentManager = _mActivity.getFragmentManager();
        titleBar.setOnBackClickListener(() -> {
            _mActivity.finish();
        });
        tvNickName.setText(User.getName());
        String[] split = User.getCurrentOrganizationId().replace(getString(R.string.common_address), "").split("/");
        if (split.length > 4) {
            areaStr = split[0] + "/" + split[1] + "/" + split[2];
            stressStr = split[split.length - 1];
            area = new BaseBean(getString(R.string.common_address) + areaStr, split[2]);
            curLevel = 2;
        }
        tvBelongArea.setText(areaStr);
        tvBelongStress.setText(stressStr);
        String helpPhone = PreUtils.getString("firstHelperPhone", "");
        tvFirstHelpPhone.setText(TextUtils.isEmpty(helpPhone) ? "未设置" : helpPhone);

        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, civAccountAvatar);
    }


    private void settingOfIvatar() {
        if (fragmentManager != null) {
            fragment = TakePhotoDialogFragment.newInstance();
            fragment.setOnResultListener(this);
            fragment.show(fragmentManager, null);
        }
    }

    private void settingOfNickName() {
        new NormalDialog().createEditMessage(_mActivity, "修改昵称",
                false,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Account account = new Account();
                        account.setName(input.toString());
                        NetHelper.getApi()
                                .putAccount(User.getUserId(), account)
                                .compose(RxUtils.handleResult())
                                .compose(RxUtils.applySchedule())
                                .subscribe(new RxSubscriber<Account>() {
                                    @Override
                                    public void _next(Account account) {
                                        //修改成功  更新显示名
                                        tvNickName.setText(input.toString());
                                        User.putName(input.toString());
                                        EventBus.getDefault().post(new Event.AccountSettingChange());
                                    }
                                });
                    }
                });
    }

    private void settingOfArea() {
        dialog = new BottomDialog(this, -1);
        dialog.setOnAddressSelectedListener(this);
        dialog.show();
    }

    private void settingOfStress() {
        if (curLevel < RECYCLERLEVEL - 1) {
            ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
            return;
        }
        dialog = new BottomDialog(this, RECYCLERLEVEL);
        dialog.setOnAddressSelectedListener(this);
        dialog.setButtonVisiableLevels(new int[]{2}, View.VISIBLE);
        dialog.show();
    }

    private void settingOfFirstPhone() {
        new NormalDialog().createEditMessage(_mActivity, "请输入新紧急救助人电话",
                false,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (!StringUtils.isMobileNumber(input.toString())) {
                            ToastHelper.get().showWareShort("手机号格式错误");
                            return;
                        }
                        PreUtils.putString("firstHelperPhone", input.toString());
                        tvFirstHelpPhone.setText(input.toString());
                    }
                });
    }


    /**
     * 修改居住地
     *
     * @param distrect
     */
    private void requestSettingAccountDistract(BaseBean distrect) {
        Account account = new Account();
        account.setProfile(new Account.ProfileBean().setBelongToDistrict(distrect.getId()));
        NetHelper.getApi()
                .putAccount(User.getUserId(), account)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Account>(_mActivity) {
                               @Override
                               public void _next(Account account) {
                                   //修改成功  更新显示名
                                   tvBelongStress.setText(distrect.getName());
                                   if (account != null && account.getProfile() != null) {
                                       User.putBelongToDistrict(account.getProfile().getBelongToDistrict());
                                       //默认组织id
                                       User.putBelongsOrganizationId(account.getProfile().getBelongToDistrict());
                                       User.putCurrentOrganizationId(account.getProfile().getBelongToDistrict());
                                       //默认组织账号id
                                       User.putBelongOrganizationAccountId(account.getDefaultContactId());
                                       //当前组织账号id
                                       User.putCurrentOrganizationAccountId(account.getDefaultContactId());
                                   }
                                   EventBus.getDefault().post(new Event.AccountSettingChange());
                                   EventBus.getDefault().post(new Event.ChangedOrganization());
                               }
                           }

                );
    }

    public void updateImage(String id, String imageName, String path) {
        Logger.d("zp", User.getToken() + "   \n" + id + "     \n" + imageName + "   \n" + path);
        if (TextUtils.isEmpty(imageName))
            imageName = "{imageName}";
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        byte[] pathString = BitmapUtils.bitmapToBytes(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), pathString);
        NetHelper.getApi()
                .updateImage(id, imageName, requestBody)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<IconModule>(_mActivity) {
                    @Override
                    public void _next(IconModule icon) {
                        ToastHelper.get().showShort("修改头像成功");
                        User.puttUserAvatar(icon.getFileName());
                        ImageLoaderUtil.instance().loadImage(IconUrl.moduleIconUrl(IconUrl.Accounts, User.getUserId(), User.getUserAvatar()), R.drawable.icon_def, civAccountAvatar);
                        EventBus.getDefault().post(new Event.AccountSettingChange());
                        //  EventBus.getDefault().post(new Event.AccountSettingChange(acc));
                    }
                });
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

                    String path;
                    if (result.getFrom() == TImage.FromType.CAMERA) {
                        path = result.getOriginalPath();
                    } else {
                        path = result.getOriginalPath();
                    }
                    updateImage(User.getUserId(), User.getUserAvatar(), path);
                });
    }

    @Override
    public void onAddressSelected(int recyclerIndex, BaseBean... baseBeen) {
        if (baseBeen.length == 0) {
            ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
            // dialog.dismiss();
            return;
        }
        if (recyclerIndex != RECYCLERLEVEL) {
            area = baseBeen[baseBeen.length - 1];
            tvBelongArea.setText(area.getId().replace(getString(R.string.common_address), ""));
            if (distrect == null || !distrect.getId().contains(area.getId())) {
                tvBelongStress.setText("");
                distrect = null;
            }
            curLevel = baseBeen.length - 1;
        } else {
            distrect = baseBeen[baseBeen.length - 1];
            //   tvBelongStress.setText(distrect.getName());
            curLevel = baseBeen.length - 1 + RECYCLERLEVEL;
            requestSettingAccountDistract(distrect);
        }
        dialog.dismiss();

    }

    @Override
    public void onConfirm(BaseBean baseBean) {
        dialog.dismiss();
        distrect = baseBean;
        requestSettingAccountDistract(distrect);
    }

    @Override
    public void requestData(BaseBean baseBean, Response<BaseBean> response, int level, int recyclerIndex) {

        if (baseBean == null) {
            if (recyclerIndex == -1) {
                getChildLocation(_mActivity, "", response, level, recyclerIndex);
            } else {
                getChildLocation(_mActivity, area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex);
            }
        } else {
            getChildLocation(_mActivity, baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex);
        }
    }

    public void getChildLocation(SupportActivity _mActivity, String locationName, DataInterface.Response<BaseBean> response, int level, int recyclerIndex) {
        NetHelper.getApi()
                .getChildLocation(locationName)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        successOfChildLocation(areas, response, level, recyclerIndex);
                    }
                });
    }

    public void successOfChildLocation(List<Area> areas, Response<BaseBean> response, int level, int recyclerIndex) {
        ArrayList<BaseBean> data = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            data.add(new BaseBean(areas.get(i).getId(), areas.get(i).getName()));
        }
        if (level == recyclerIndex || level == RECYCLERLEVEL) {
            response.send(level, null);
            return;
        }

        response.send(level, data);
    }

}
