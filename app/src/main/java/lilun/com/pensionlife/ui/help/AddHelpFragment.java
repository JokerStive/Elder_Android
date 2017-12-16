package lilun.com.pensionlife.ui.help;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.OrganizationChildrenConfig;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseTakePhotoFragment;
import lilun.com.pensionlife.module.bean.OrganizationAid;
import lilun.com.pensionlife.module.bean.TakePhotoResult;
import lilun.com.pensionlife.module.bean.TokenParams;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.module.utils.ToastHelper;
import lilun.com.pensionlife.module.utils.qiniu.QINiuEngine;
import lilun.com.pensionlife.module.utils.qiniu.QiNiuUploadView;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.InputView;
import lilun.com.pensionlife.widget.NormalTitleBar;
import lilun.com.pensionlife.widget.QinNiuPop;
import lilun.com.pensionlife.widget.TakePhotoLayout;
import rx.Observable;

/**
 * 新建求助V
 *
 * @author yk
 *         create at 2017/2/16 17:33
 *         email : yk_developer@163.com
 */
public class AddHelpFragment extends BaseTakePhotoFragment implements View.OnClickListener {

    @Bind(R.id.title_bar

    )
    NormalTitleBar titleBar;


    @Bind(R.id.input_topic)
    InputView inputTopic;

    @Bind(R.id.input_address)
    InputView inputAddress;

    @Bind(R.id.input_mobile)
    InputView inputMobile;


    @Bind(R.id.input_price)
    InputView inputPrice;

//    @Bind(R.id.input_memo)
//    InputView inputMemo;

    @Bind(R.id.tv_create)
    TextView btnCreate;

    @Bind(R.id.take_photo)
    TakePhotoLayout takePhotoLayout;
    @Bind(R.id.tv_kind)
    TextView tvKind;
    @Bind(R.id.rl_choice_kind)
    RelativeLayout rlChoiceKind;
    @Bind(R.id.tv_priority)


    TextView tvPriority;

    @Bind(R.id.tv_priority_value)
    TextView tvPriorityValue;


    @Bind(R.id.rl_choice_priority)
    RelativeLayout rlChoicePriority;


    @Bind(R.id.et_content)
    EditText etContent;


    private Integer mKind;
    private int mPriority = 0;
    private String[] helpPriority = App.context.getResources().getStringArray(R.array.help_priority);
    private String[] helpKind = new String[]{"问邻居", "邻居帮"};
    private QinNiuPop pop;
    private String mAidId;

    public static AddHelpFragment newInstance() {
        return new AddHelpFragment();
    }


    @Override

    protected void initPresenter() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_help;
    }

    @Override
    protected void initView(LayoutInflater inflater) {

        takePhotoLayout.setFragmentManager(_mActivity.getFragmentManager());
        takePhotoLayout.setOnResultListener(this);
        setTakePhotoLayout(takePhotoLayout);

        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        titleBar.setOnBackClickListener(this::pop);
        btnCreate.setOnClickListener(this);
        tvPriority.setOnClickListener(this);
        rlChoiceKind.setOnClickListener(this);
        rlChoicePriority.setOnClickListener(this);


        tvPriorityValue.setText(helpPriority[0]);
        inputMobile.setInput(User.getMobile());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_choice_priority:
                choosePriority();
                break;

            case R.id.rl_choice_kind:
                chooseKind();
                break;
            case R.id.tv_create:
                createHelp();
                break;
        }
    }


    /**
     * 选择求助信息的优先级
     */
    private void chooseKind() {
        new MaterialDialog.Builder(_mActivity)
                .items(helpKind)
                .title("-选择求助类型-")
                .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
                    inputAddress.setVisibility(which == 0 ? View.GONE : View.VISIBLE);
                    inputMobile.setVisibility(which == 0 ? View.GONE : View.VISIBLE);
                    tvKind.setText(text);
                    mKind = which;
                    return true;
                })
                .show();

    }

    /**
     * 选择求助信息的优先级
     */
    private void choosePriority() {
        if (helpPriority == null) {
            helpPriority = getResources().getStringArray(R.array.help_priority);
        }
        new MaterialDialog.Builder(_mActivity)
                .items(helpPriority)
                .title("-选择求助优先级-")
                .itemsCallbackSingleChoice(-1, (dialog, view, which, text) -> {
                    tvPriorityValue.setText(helpPriority[which]);
                    mPriority = which == helpPriority.length - 1 ? 10 : which;
                    return true;
                })
                .show();

    }


    /**
     * 新建一个求助信息
     */
    private void createHelp() {
        String title = inputTopic.getInput();
        String address = inputAddress.getInput();

        //必须选择求助类型
        if (mKind == null) {
            ToastHelper.get().showWareShort("请选择求助类型");
            return;
        }


        if (StringUtils.checkNotEmptyWithMessage(title, "请输入求助主题")) {
            //如果是帮，必须填求助的地址
            if (mKind == 1) {
                if (TextUtils.isEmpty(address)) {
                    ToastHelper.get().showWareShort("请输入地址");
                    return;
                }
            }

            newAid(getOrganizationAid());
        }
    }


    /**
     * 创建模型
     */
    private void newAid(OrganizationAid aid) {
        NetHelper.getApi()
                .newOrganizationAid(aid)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationAid>(_mActivity) {
                    @Override
                    public void _next(OrganizationAid organizationAid) {
                        Boolean isDraft = organizationAid.isDraft;
                        if (isDraft) {
                            mAidId = organizationAid.getId();
                            uploadImages();
                        } else {
                            popAndRefresh();
                        }
                    }
                });

    }

//    /**
//     * 获取token
//     */
//    private void getToken() {
//        ArrayList<String> photoPath = getPhotoData();
//        if (photoPath.size() > 0) {
//            ArrayList<String> fileNames = createFileNames(photoPath);
//            NetHelper.getApi().
//                    getPostFileToken("OrganizationAids", mAidId, "image", fileNames)
//                    .compose(RxUtils.handleResult())
//                    .compose(RxUtils.applySchedule())
//                    .subscribe(new RxSubscriber<QINiuToken>() {
//                        @Override
//                        public void _next(QINiuToken qiNiuToken) {
//                            uploadImages(qiNiuToken);
//                        }
//                    });
//        }
//    }
//
//    private ArrayList<String> createFileNames(ArrayList<String> photoPath) {
//        ArrayList<String> fileNames = new ArrayList<>();
//        for (int i = 0; i < photoPath.size(); i++) {
//            String fileName = i + QINiuEngine.format;
//            fileNames.add(fileName);
//        }
//        return fileNames;
//    }

    /**
     * 上传图片
     */
    private void uploadImages() {
        TokenParams tokenParams = createTokenParam();
        ArrayList<String> filePaths = getPhotoData();
        ArrayList<QiNiuUploadView> views = getQiNiuUploadViews(filePaths);
        QINiuEngine engine = new QINiuEngine(_mActivity, tokenParams, filePaths, views, new QINiuEngine.UploadListener() {
            @Override
            public void onAllSuccess() {
                putDataAndPop();
            }
        });
        engine.postMultipleFile();
//        QINiuEngine engine = new QINiuEngine(_mActivity, photoData.size(), qiNiuToken, new QINiuEngine.UploadListener() {
//            @Override
//            public void onAllSuccess() {
//                putDataAndPop();
//            }
//
//            @Override
//            public void tokenInvalid(ArrayList<String> uploadFileNames) {
//                NetHelper.getApi().
//                        getPutFileToken("OrganizationAids", mAidId, "image", uploadFileNames, true)
//                        .compose(RxUtils.handleResult())
//                        .compose(RxUtils.applySchedule())
//                        .subscribe(new RxSubscriber<QINiuToken>() {
//                            @Override
//                            public void _next(QINiuToken qiNiuToken) {
//                                engine.continueUpload(qiNiuToken);
//                            }
//                        });
//            }
//        });
//        for (int i = 0; i < photoData.size(); i++) {
//            String path = photoData.get(i);
//            QiNiuUploadView view = takePhotoLayout.getView(i);
//            engine.upload(path, i, view);
//        }
    }

    private ArrayList<QiNiuUploadView> getQiNiuUploadViews(ArrayList<String> filePaths) {
        ArrayList<QiNiuUploadView> views = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            QiNiuUploadView view = takePhotoLayout.getView(i);
            views.add(view);
        }
        return views;
    }

    @NonNull
    private TokenParams createTokenParam() {
        TokenParams tokenParams = new TokenParams();
        tokenParams.setModelName("OrganizationAids");
        tokenParams.setModelId(mAidId);
        tokenParams.setTag("image");
        return tokenParams;
    }


    /**
     * 修改aid的状态，退出并且刷新界面
     */
    private void putDataAndPop() {
        OrganizationAid organizationAid = new OrganizationAid();
        organizationAid.setDraft(false);
        NetHelper.getApi()
                .putOrganizationAid(mAidId, organizationAid)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>(_mActivity) {
                    @Override
                    public void _next(Object o) {
                        popAndRefresh();
                    }
                });


    }

    private void popAndRefresh() {
        pop();
        Event.RefreshHelpData refreshHelpData = new Event.RefreshHelpData();
        EventBus.getDefault().post(refreshHelpData);
    }


    @NonNull
    private OrganizationAid getOrganizationAid() {
        OrganizationAid organizationAid = new OrganizationAid();
        organizationAid.setTitle(inputTopic.getInput());
        organizationAid.setAddress(inputAddress.getInput());
        organizationAid.setPriority(mPriority);
        organizationAid.setMemo(etContent.getText().toString());
        organizationAid.setKind(mKind);
        organizationAid.setMobile(inputMobile.getInput());
        organizationAid.setDraft(getPhotoData() != null && getPhotoData().size() != 0);
        //如果提供了补贴
        if (!TextUtils.isEmpty(inputPrice.getInput())) {
            organizationAid.setPrice(Double.parseDouble(inputPrice.getInput()));
        }
        organizationAid.setOrganizationId(OrganizationChildrenConfig.aid());
        return organizationAid;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
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
                    takePhotoLayout.showPhotos(results);
                });
    }


}