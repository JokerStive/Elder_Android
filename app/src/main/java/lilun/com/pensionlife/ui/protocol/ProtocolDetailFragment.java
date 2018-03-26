package lilun.com.pensionlife.ui.protocol;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Information;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 协议详情
 */
public class ProtocolDetailFragment extends BaseFragment {


    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.tv_protocol_title)
    TextView tvProtocolTitle;
    @Bind(R.id.tv_protocol_time)
    TextView tvProtocolTime;
    @Bind(R.id.tv_protocol_desc)
    TextView tvProtocolDesc;
    @Bind(R.id.tv_protocol_disagree)
    TextView tvProtocolDisagree;
    @Bind(R.id.tv_protocol_agree)
    TextView tvProtocolAgree;
    @Bind(R.id.rl_operate)
    RelativeLayout rlOperate;
    private String mProtocolId;
    private boolean mIsFocus;

    public static ProtocolDetailFragment newInstance(String protocolId, boolean isFocus) {
        ProtocolDetailFragment fragment = new ProtocolDetailFragment();
        Bundle args = new Bundle();
        args.putString("protocolId", protocolId);
        args.putBoolean("isFocus", isFocus);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void getTransferData(Bundle arguments) {
        mProtocolId = arguments.getString("protocolId");
        mIsFocus = arguments.getBoolean("isFocus");
    }

    @Override
    protected void initPresenter() {
        getProtocol();
    }

    private void getProtocol() {
        NetHelper.getApi()
                .getInformation(mProtocolId)
                .compose(RxUtils.applySchedule())
                .compose(RxUtils.handleResult())
                .subscribe(new RxSubscriber<Information>() {
                    @Override
                    public void _next(Information information) {
                        showProtocolDetail(information);
                    }
                });
    }

    private void showProtocolDetail(Information information) {
        tvProtocolTitle.setText(information.getTitle());
        tvProtocolTime.setText(StringUtils.IOS2ToUTC(information.getCreatedAt(), 5));
        int contextType = information.getContextType();
        String context = information.getContext();
        if (TextUtils.isEmpty(context)) {
            return;
        }


        if (contextType == 0) {
            tvProtocolDesc.setText(context);
        } else if (contextType == 2) {
            tvProtocolDesc.setText(Html.fromHtml(context));
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_protocol_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);
        tvProtocolAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgreeProtocol(true);
            }
        });


        tvProtocolDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgreeProtocol(false);
            }
        });
    }

    private void isAgreeProtocol(boolean isAgreeProtocol) {
        EventBus.getDefault().post(new ProtocolView.IsAgreeProtocol(isAgreeProtocol));
        pop();
    }




}
