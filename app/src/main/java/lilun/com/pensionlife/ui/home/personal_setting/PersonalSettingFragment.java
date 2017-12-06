package lilun.com.pensionlife.ui.home.personal_setting;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.vanzh.library.BaseBean;
import com.vanzh.library.BottomDialog;
import com.vanzh.library.DataInterface;
import com.vanzh.library.OnAddressSelectedListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import lilun.com.pensionlife.BuildConfig;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.Constants;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseTakePhotoFragment;
import lilun.com.pensionlife.module.bean.Account;
import lilun.com.pensionlife.module.bean.Area;
import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.bean.QINiuToken;
import lilun.com.pensionlife.module.bean.TakePhotoResult;
import lilun.com.pensionlife.module.utils.PreUtils;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.UIUtils;
import lilun.com.pensionlife.module.utils.mqtt.MQTTManager;
import lilun.com.pensionlife.module.utils.qiniu.QINiuEngine;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.ui.welcome.LoginModule;
import lilun.com.pensionlife.widget.AddressGuidePopupWindow;
import lilun.com.pensionlife.widget.CircleImageView;
import lilun.com.pensionlife.widget.CircleProgressView;
import lilun.com.pensionlife.widget.NormalDialog;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.TakePhotoDialogFragment;
import lilun.com.pensionlife.widget.image_loader.ImageLoaderUtil;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Observable;

/**
 * 居住地址是系统默认的演示社区，HomeFragment弹框进入该页面设置居住地址及头像-->本页面显示提示标签
 * 2017-11-29 09:42:00 图片上传服务器更新为七牛服务器，没有用户头像时使用上传图片token,有图片时使用更新图片token
 * Created by zp on 2017/6/5.
 */

public class PersonalSettingFragment extends BaseTakePhotoFragment<PersonalSettingContract.Persenter>
        implements DataInterface<BaseBean>, OnAddressSelectedListener, PersonalSettingContract.View {
    private static final String IN_TYPE = "IN_TYPE";
    public static final int UN_SETTING = 1;  // 居住地址是演示社区
    public static final int HAS_SETTING = 0;  //
    final int RECYCLERLEVEL = 3;
    int curLevel = -1;
    private FragmentManager fragmentManager;
    private TakePhotoDialogFragment fragment;
    BottomDialog dialog;
    BaseBean area, distrect;
    private boolean hasAvator;   //是否有头像地址
    int[] skipArray = new int[6];

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;
    @Bind(R.id.civ_account_avatar)
    CircleImageView civAvator;
    @Bind(R.id.cpv_upload)
    CircleProgressView cpvUpload;
    @Bind(R.id.tv_nickname)
    TextView tvNickName;
    @Bind(R.id.tv_belong_area)
    TextView tvBelongArea;
    @Bind(R.id.ll_belong_area)
    LinearLayout llBelongArea;
    @Bind(R.id.tv_belong_stress)
    TextView tvBelongStress;
    @Bind(R.id.ll_belong_stress)
    LinearLayout llBelongStress;


    @Bind(R.id.tv_first_help_phone)
    TextView tvFirstHelpPhone;

    private String areaStr = "", stressStr = "";
    private int inType;  //默认为0
    private AddressGuidePopupWindow addressGuidePopupWindow;
    ViewTreeObserver viewTreeObserver;
    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private int limitSkip = 20;
    private String imagePath;

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

    public static PersonalSettingFragment newInstance(int inType) {

        Bundle args = new Bundle();
        args.putInt(IN_TYPE, inType);
        PersonalSettingFragment fragment = new PersonalSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_personal_setting;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        super.getTransferData(arguments);
        inType = getArguments().getInt(IN_TYPE);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PersonalSettingPersenter();
        mPresenter.bindView(this);
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        initView();
    }


    protected void initView() {
        fragmentManager = _mActivity.getFragmentManager();
        titleBar.setOnBackClickListener(() -> {
            if (_mActivity instanceof PersonalActivity)
                _mActivity.finish();
            else pop();
        });
        tvNickName.setText(User.getName());
        hasAvator = !TextUtils.isEmpty(User.getUserAvatar());
        String[] split = User.getBelongsOrganizationId().replace(getString(R.string.common_address), "").split("/");
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

        ImageLoaderUtil.instance().loadAvator(User.getUserAvatar(), R.drawable.icon_def, civAvator);

        //绘制完成
        viewTreeObserver = llBelongArea.getViewTreeObserver();
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int[] locationOnScreen = new int[2];
                int[] locationOnScreen2 = new int[2];
                llBelongArea.getLocationInWindow(locationOnScreen);
                llBelongStress.getLocationInWindow(locationOnScreen2);
                int headerHeight = locationOnScreen[1];
                int midHeight = locationOnScreen2[1] + llBelongStress.getMeasuredHeight() - locationOnScreen[1];
                if (inType == UN_SETTING) {  //显示发动标签
                    if (addressGuidePopupWindow == null)
                        addressGuidePopupWindow = new AddressGuidePopupWindow(_mActivity);
                    //api 25 status_bar_height =25dip  其他都 是24
                    int stautsBarHeight = UIUtils.dp2px(_mActivity, Build.VERSION.SDK_INT == 25 ? 25 : 24);
                    addressGuidePopupWindow.setHeaderXYHeight(0, 0, headerHeight - stautsBarHeight);
                    addressGuidePopupWindow.setMidXYHeight(0, headerHeight, midHeight);
                    addressGuidePopupWindow.showAtLocation(tvBelongStress, Gravity.CENTER, 0, 0);
                    llBelongArea.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        };
        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener);
    }


    private void settingOfIvatar() {
        if (fragmentManager != null) {
            fragment = TakePhotoDialogFragment.newInstance();
            fragment.setOnResultListener(this);
            fragment.show(fragmentManager, null);
        }
    }

    private void settingOfNickName() {
        new NormalDialog().createEditMessage(_mActivity, "修改昵称", true,
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
        skipArray = new int[6];
        dialog = new BottomDialog(this, -1, limitSkip);
        dialog.setOnAddressSelectedListener(this);
        dialog.show();
    }

    private void settingOfStress() {
        skipArray = new int[6];
        if (curLevel < RECYCLERLEVEL - 1) {
            ToastHelper.get(getContext()).showWareShort("该地区未开通服务，请重新选择");
            return;
        }

        dialog = new BottomDialog(this, RECYCLERLEVEL, limitSkip);
        dialog.setOnAddressSelectedListener(this);
        dialog.setButtonVisiableLevels(new int[]{2}, View.VISIBLE);
        dialog.show();
    }

    private void settingOfFirstPhone() {
        if (TextUtils.isEmpty(PreUtils.getString("firstHelperPhone", ""))) {
            ToastHelper.get().showWareShort("您需要先在首页--紧急求助模块下设置联系人电话");
            return;
        }
        new NormalDialog().createEditMessage(_mActivity, "请输入新紧急救助人电话", true,
                new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (!StringUtils.isMobileNumber(input.toString())) {
                            ToastHelper.get().showWareShort("手机号格式错误");
                            return;
                        }
                        if (TextUtils.equals(input.toString(), User.getMobile())) {
                            ToastHelper.get().showWareShort("手机号不能是自己的");
                            return;
                        }

                        //该功能在10106上实现
                        if (BuildConfig.VERSION_CODE >= 10106) {
                            Account postAccount = new Account();
                            postAccount.setProfile(new Account.ProfileBean(User.getLocation(), input.toString(), User.getAddress()));
                            NetHelper.getApi()
                                    .putAccount(User.getUserId(), postAccount)
                                    .compose(RxUtils.handleResult())
                                    .compose(RxUtils.applySchedule())
                                    .subscribe(new RxSubscriber<Account>() {
                                        @Override
                                        public void _next(Account account) {
                                            PreUtils.putString("firstHelperPhone", input.toString());
                                            tvFirstHelpPhone.setText(input.toString());
                                        }
                                    });
                        } else {
                            PreUtils.putString("firstHelperPhone", input.toString());
                            tvFirstHelpPhone.setText(input.toString());
                        }
                    }
                });
    }


    /**
     * 修改居住地
     *
     * @param distrect
     */
    private void requestSettingAccountDistract(BaseBean distrect) {
        LoginModule loginModule = new LoginModule();
        NetHelper.getApi()
                .putAccountLocation(distrect.getId())
                .compose(RxUtils.handleResult())
                .flatMap(loginModule::getBelongOrganizations)
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<OrganizationAccount>>() {
                    @Override
                    public void _next(List<OrganizationAccount> organizationAccounts) {
                        //取消所有订阅
                        MQTTManager.unSubscribeAllTopic();

                        loginModule.putBelongOrganizations(organizationAccounts);
                        if (loginModule.saveUserAboutOrganization(loginModule.getOrganizationIdMappingOrganizationAccountId(distrect.getId()))) {
                            tvBelongStress.setText(distrect.getName());
                            EventBus.getDefault().post(new Event.ChangedOrganization());
                            EventBus.getDefault().post(new Event.AccountSettingChange());

                            //重新订阅所在地
                            MQTTManager.subscribeAllTopic();
                        } else {
                            ToastHelper.get().showShort("脏数据");
                        }
                    }
                });
    }

    @Override
    public void onCameraClick() {
        File file;
        file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(_mActivity, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        }
        CropOptions options = new CropOptions.Builder()
                .setAspectX(800)
                .setOutputX(800)
                .create();

        getTakePhoto().onPickFromCaptureWithCrop(uri,options);
    }

    @Override
    public void onAlbumClick() {
        CropOptions options = new CropOptions.Builder()
                .setAspectX(800)
                .setOutputX(800)
                .create();
        getTakePhoto().onPickMultipleWithCrop(1, options);
    }

    @Override
    protected void onTakePhotoSuccess(TResult tResult) {
        List<TakePhotoResult> results = new ArrayList<>();
        for (TImage tImage : tResult.getImages()) {
            TakePhotoResult result1 = TakePhotoResult.of(tImage.getOriginalPath(), tImage.getCompressPath(), tImage.getFromType(), TakePhotoResult.TYPE_PHOTO);
            results.add(result1);
            imagePath = results.get(0).getCompressPath();
            break;
        }

        Observable.just("")
                .compose(RxUtils.applySchedule())
                .subscribe(s -> {
                    if (hasAvator) {
                        mPresenter.getUpdateToken("Accounts", User.getUserId(), "image");
                    } else
                        mPresenter.getUploadToken("Accounts", User.getUserId(), "image");
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
    public void requestData(BaseBean baseBean, Response<BaseBean> response, int level, int recyclerIndex, int startIndex, int reqCount) {

        if (baseBean == null) {
            if (recyclerIndex == -1) {
                getChildLocation(_mActivity, "", response, level, recyclerIndex, startIndex, reqCount);
            } else {
                getChildLocation(_mActivity, area.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
            }
        } else {
            getChildLocation(_mActivity, baseBean.getId().replace(getString(R.string.common_address), ""), response, level, recyclerIndex, startIndex, reqCount);
        }
    }

    public void getChildLocation(SupportActivity _mActivity, String locationName, DataInterface.Response<BaseBean> response, int level, int recyclerIndex, int skip, int reqCount) {
        NetHelper.getApi()
                .getChildLocation(locationName, skip, reqCount)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<List<Area>>() {
                    @Override
                    public void _next(List<Area> areas) {
                        skipArray[level] += areas.size();
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
            response.send(level, null, false);
            return;
        }

        response.send(level, data, data != null && data.size() == limitSkip);
    }

    @Override
    public void uploadImages(QINiuToken qiNiuToken) {

        QINiuEngine engine = new QINiuEngine(_mActivity, 1, qiNiuToken, new QINiuEngine.UploadListener() {
            @Override
            public void onAllSuccess() {
                //未设置过头像，则显示，重新获取个人数据，再显示头像
                ToastHelper.get().showShort("更新图片成功");
                if (hasAvator) {
                    ImageLoaderUtil.instance().loadAvator(User.getUserAvatar(), R.drawable.icon_def, civAvator);
                    EventBus.getDefault().post(new Event.AccountSettingChange());
                } else
                    mPresenter.getMe();
            }
        });
        String updateKey = null;
        if (hasAvator) updateKey = User.getUserAvatar().replace(Constants.fileBaseUri, "");
        engine.uploadOnlyOne(imagePath, User.getUserId(), updateKey, cpvUpload);
    }


    @Override
    public void showNewAvator(Account account) {
        User.puttUserAvatar(account.getImage());
        hasAvator = !TextUtils.isEmpty(account.getImage());
        ImageLoaderUtil.instance().loadAvator(account.getImage(), R.drawable.icon_def, civAvator);
        EventBus.getDefault().post(new Event.AccountSettingChange());
    }

}
