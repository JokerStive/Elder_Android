package lilun.com.pension.ui.help;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import lilun.com.pension.R;
import lilun.com.pension.base.BaseFragment;
import lilun.com.pension.module.adapter.ElderModuleAdapter;
import lilun.com.pension.module.bean.ElderModule;
import lilun.com.pension.module.bean.OrganizationAid;
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
import rx.Subscription;

/**
 * 新建求助V
 *
 * @author yk
 *         create at 2017/2/16 17:33
 *         email : yk_developer@163.com
 */
public class AddHelpFragment extends BaseFragment implements View.OnClickListener {

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

        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER);

        titleBar.setOnBackClickListener(this::pop);
        btnCreate.setOnClickListener(this);
        inputPriority.setOnClickListener(this);


        rvHelpClassify.setLayoutManager(new GridLayoutManager(_mActivity, StringUtils.spanCountByData(elderModules)));
        rvHelpClassify.addItemDecoration(new ElderModuleClassifyDecoration());
        ElderModuleAdapter adapter = new ElderModuleAdapter(this, elderModules);
        adapter.setIsRadioModule(true);
        adapter.setOnItemClickListener(elderModule -> {
            Logger.d("选择的类型==" + elderModule.getServiceConfig().getKind());
            this.mKind = elderModule.getServiceConfig().getKind();
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

        if (mKind == null) {
            ToastHelper.get().showWareShort("请选择求助类型");
            return;
        }

        if (StringUtils.checkNotEmptyWithMessage(title, "请输入求助主题") && StringUtils.checkNotEmptyWithMessage(address, "请输入地址")) {
            OrganizationAid organizationAid = new OrganizationAid();
            organizationAid.setTitle(title);
            organizationAid.setAddress(address);
            organizationAid.setPriority(mPriority);
            organizationAid.setMemo(memo);
            organizationAid.setKind(mKind);
            if(!TextUtils.isEmpty(price)){
                organizationAid.setPrice(Integer.parseInt(price));
            }

            subscription = NetHelper.getApi()
                    .newOrganizationAid(organizationAid)
                    .compose(RxUtils.handleResult())
                    .compose(RxUtils.applySchedule()).subscribe(new RxSubscriber<OrganizationAid>(_mActivity) {
                        @Override
                        public void _next(OrganizationAid organizationAid) {
                            Logger.d("求助发布成功");
                            pop();
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();

        }
    }
}
