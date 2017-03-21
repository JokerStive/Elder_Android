package lilun.com.pension.ui.help;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.app.Event;
import lilun.com.pension.app.OrganizationChildrenConfig;
import lilun.com.pension.base.BaseTakePhotoFragment;
import lilun.com.pension.module.adapter.ElderModuleAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.IconModule;
import lilun.com.pension.module.bean.OrganizationAid;
import lilun.com.pension.module.bean.TakePhotoResult;
import lilun.com.pension.module.utils.BitmapUtils;
import lilun.com.pension.module.utils.Preconditions;
import lilun.com.pension.module.utils.RxUtils;
import lilun.com.pension.module.utils.StringUtils;
import lilun.com.pension.module.utils.ToastHelper;
import lilun.com.pension.net.NetHelper;
import lilun.com.pension.net.RxSubscriber;
import lilun.com.pension.widget.CommonButton;
import lilun.com.pension.widget.ElderModuleClassifyDecoration;
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

    @Bind(R.id.title_bar)
    NormalTitleBar titleBar;

    @Bind(R.id.rv_help_classify)
    RecyclerView rvHelpClassify;

    @Bind(R.id.input_priority)
    InputView inputPriority;

    @Bind(R.id.input_topic)
    InputView inputTopic;

    @Bind(R.id.input_address)
    InputView inputAddress;

    @Bind(R.id.input_price)
    InputView inputPrice;

    @Bind(R.id.input_memo)
    InputView inputMemo;

    @Bind(R.id.btn_create)
    CommonButton btnCreate;
    @Bind(R.id.take_photo)
    TakePhotoLayout takePhotoLayout;

    private List<ElderModule> elderModules;

    private Integer mKind;
    private int mPriority = 0;
    private Subscription subscription;
    private String[] helpPriority;

    public static AddHelpFragment newInstance(List<ElderModule> elderModules) {
        AddHelpFragment fragment = new AddHelpFragment();
        Bundle args = new Bundle();
        args.putSerializable("elderModules", (Serializable) elderModules);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        elderModules = (List<ElderModule>) arguments.getSerializable("elderModules");
        Preconditions.checkNull(elderModules);
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
        inputPriority.setOnClickListener(this);


        rvHelpClassify.setLayoutManager(new GridLayoutManager(_mActivity, StringUtils.spanCountByData(elderModules)));
        rvHelpClassify.addItemDecoration(new ElderModuleClassifyDecoration());
        ElderModuleAdapter adapter = new ElderModuleAdapter(this, elderModules);
        adapter.setIsRadioModule(true);
        adapter.setOnItemClickListener(elderModule -> {
            this.mKind = elderModule.getServiceConfig().getKind();
            inputAddress.setVisibility(mKind == 0 ? View.GONE : View.VISIBLE);
        });
        rvHelpClassify.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_priority:
                choosePriority();
                break;
            case R.id.btn_create:
                createHelp();
                break;
        }
    }

    /**
     * 选择求助信息的优先级
     */
    private void choosePriority() {
        if (helpPriority == null) {
            helpPriority = getResources().getStringArray(R.array.help_priority);
        }
        new MaterialDialog.Builder(_mActivity)
                .title(R.string.choose_priority)
                .items(helpPriority)
                .itemsCallbackSingleChoice(0, (dialog, view, which, text) -> {
                    inputPriority.setInput(helpPriority[which]);
                    return true;
                })
                .positiveText(R.string.choose)
                .show();

    }


    /**
     * 新建一个求助信息
     */
    private void createHelp() {
        String priority = inputPriority.getInput();
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
                Map<String, RequestBody> requestBodies = BitmapUtils.createRequestBodies(data);
                if (requestBodies != null) {
                    observable = iconObservable(requestBodies).flatMap(this::aidObservable);
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

    private Observable<OrganizationAid> aidObservable(List<IconModule> iconModules) {
        OrganizationAid aid = getOrganizationAid();
        aid.setPicture(iconModules);
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
