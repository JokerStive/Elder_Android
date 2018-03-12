package lilun.com.pensionlife.ui.push_info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseFragment;
import lilun.com.pensionlife.module.bean.Invitation;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.module.utils.StringUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import lilun.com.pensionlife.widget.NormalTitleBar;

/**
 * 邀请详情页面
 *
 * @author yk
 *         create at 2018/3/7 10:52
 *         email : yk_developer@163.com
 */
public class InvitationDetailFragment extends BaseFragment {
    @Bind(R.id.titleBar)
    NormalTitleBar titleBar;
    @Bind(R.id.tv_invitation_title)
    TextView tvInvitationTitle;
    @Bind(R.id.tv_invitation_status)
    TextView tvInvitationStatus;

    @Bind(R.id.tv_invitation_time)
    TextView tvInvitationTime;
    @Bind(R.id.tv_invitation_refuse)
    TextView tvInvitationRefuse;
    @Bind(R.id.tv_invitation_join)
    TextView tvInvitationJoin;
    @Bind(R.id.rl_operate)
    RelativeLayout rl;
    private Invitation mInvitation;

    private String invitationTitle = "尊敬的用户，您的平台账号被%1$s邀请加入，请确认。";

    public static InvitationDetailFragment newInstance(Invitation invitation) {
        InvitationDetailFragment fragment = new InvitationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("invitation", invitation);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initPresenter() {
        mInvitation = (Invitation) getArguments().getSerializable("invitation");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invitation_detail;
    }

    @Override
    protected void initView(LayoutInflater inflater) {
        titleBar.setOnBackClickListener(this::pop);

        tvInvitationTitle.setText(String.format(invitationTitle, mInvitation.getFrom()));


        String time = StringUtils.IOS2ToUTC(mInvitation.getTime(), 8);
        tvInvitationTime.setText(time);


        tvInvitationStatus.setText(createDescWithStatus(mInvitation.getStatus()));
        rl.setVisibility(mInvitation.getStatus() == 0 ? View.VISIBLE : View.GONE);


        tvInvitationRefuse.setOnClickListener(v -> {
            join(-2);
        });

        tvInvitationJoin.setOnClickListener(v -> {
            join(1);
        });
    }


    public String createDescWithStatus(int statusNum) {
        String status = "未处理";
        if (statusNum == 1) {
            status = "已加入";
        } else if (statusNum == -2) {
            status = "已拒绝";
        }
        return String.format("处理状态：%s", status);
    }

    private void join(int type) {
        String organizationId = mInvitation.getOrganizationId();
        NetHelper.getApi()
                .acceptInvitation(User.getUserId(), organizationId, type)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {
                        cacheInvitationInfo(mInvitation.getId(), type);
                    }
                });
    }

    private void cacheInvitationInfo(int invitationId, int status) {
        Invitation invitation = new Invitation();
        invitation.setStatus(status);
        invitation.update(invitationId);

        tvInvitationStatus.setText(createDescWithStatus(status));
        rl.setVisibility(status == 0 ? View.VISIBLE : View.GONE);
        EventBus.getDefault().post(10086);
    }


}
