package lilun.com.pension.ui.help;

import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.BaseTakePhotoFragment;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.InputView;
import lilun.com.pension.widget.NormalTitleBar;
import lilun.com.pension.widget.TakePhotoLayout;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

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

    @Bind(R.id.input_price)
    InputView inputPrice;

    @Bind(R.id.input_memo)
    InputView inputMemo;

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
    @Bind(R.id.rl_choice_priority)
    RelativeLayout rlChoicePriority;
//    @Bind(R.id.input_kind)
//    InputView inputKind;

//    @Bind(R.id.rg_classify)
//    RadioGroup rgClassify;


    private Integer mKind;
    private int mPriority = 0;
    private Subscription subscription;
    private String[] helpPriority;
    private String[] helpKind = new String[]{"问邻居", "邻居帮"};

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


//        rgClassify.check(R.id.rb_ask);
//        rgClassify.setOnCheckedChangeListener((group, checkedId) -> {
//            switch (checkedId) {
//                case R.id.rb_ask:
//                    mKind = 0;
//                    break;
//                case R.id.rb_help:
//                    mKind = 1;
//                    break;
//            }
//        });
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
                    tvPriority.setText(helpPriority[which]);
                    return true;
                })
                .show();

    }


    /**
     * 新建一个求助信息
     */
    private void createHelp() {
        String priority = tvPriority.getText().toString();
        String title = inputTopic.getInput();
        String address = inputAddress.getInput();
        String price = inputPrice.getInput();
        String memo = inputMemo.getInput();

        if (!TextUtils.isEmpty(priority) && helpPriority != null) {
            if (priority.equals(helpPriority[0])) {
                mPriority = 0;
            } else if (priority.equals(helpPriority[1])) {
                mPriority = 1;
            } else if (priority.equals(helpPriority[2])) {
                mPriority = 2;
            } else if (priority.equals(helpPriority[3])) {
                mPriority = 10;
            }
        }

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

            //如果有图片，需要先上传图片
            Observable<OrganizationAid> observable = aidObservable(null);
            List<String> data = getPhotoData();
            if (data != null) {
//                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                builder.a
                Map<String, RequestBody> requestBodies = BitmapUtils.createRequestBodies(data);
                if (requestBodies != null) {
                    observable = iconObservable(requestBodies).flatMap(this::aidObservable);
//                    observable = aidObservable(requestBodies);
                }
            }

            start(observable);
        }
    }

    private void start(Observable<OrganizationAid> observable) {
        subscription = observable.compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<OrganizationAid>(_mActivity) {
                    @Override
                    public void _next(OrganizationAid aid) {
                        Logger.d("求助发布成功");
                        pop();
                        EventBus.getDefault().post(new Event.RefreshHelpData());
                    }
                });

    }

    @NonNull
    private OrganizationAid getOrganizationAid() {
        OrganizationAid organizationAid = new OrganizationAid();
        organizationAid.setTitle(inputTopic.getInput());
        organizationAid.setAddress(inputAddress.getInput());
        organizationAid.setPriority(mPriority);
        organizationAid.setMemo(inputMemo.getInput());
        organizationAid.setKind(mKind);
        //如果提供了补贴
        if (!TextUtils.isEmpty(inputPrice.getInput())) {
            organizationAid.setPrice(Integer.parseInt(inputPrice.getInput()));
        }
        organizationAid.setOrganizationId(OrganizationChildrenConfig.aid());
        return organizationAid;
    }


    private Observable<List<IconModule>> iconObservable(Map<String, RequestBody> requestBodies) {
        return NetHelper.getApi()
                .newAidIcons(requestBodies)
                .compose(RxUtils.handleResult());
    }

//    private Observable<OrganizationAid> aidObservable(Map<String, RequestBody> requestBodies) {
//        OrganizationAid aid = getOrganizationAid();
//        return NetHelper.getApi()
//                .newOrganizationAid(aid, requestBodies)
//                .compose(RxUtils.handleResult());
//    }

    private Observable<OrganizationAid> aidObservable(List<IconModule> iconModules) {
        OrganizationAid aid = getOrganizationAid();
        aid.setImage(iconModules);
        return NetHelper.getApi()
                .newOrganizationAid(aid)
                .compose(RxUtils.handleResult());
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
