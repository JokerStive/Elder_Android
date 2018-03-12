package lilun.com.pensionlife.module.utils.mqtt;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.TimeUnit;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.Invitation;
import lilun.com.pensionlife.module.utils.RxUtils;
import lilun.com.pensionlife.net.NetHelper;
import lilun.com.pensionlife.net.RxSubscriber;
import rx.Observable;

/**
 * Created by Admin on 2018/1/25.
 */
public class GlobalShowDialog {

    private static GlobalShowDialog instance;
    private MaterialDialog show;

    private GlobalShowDialog() {
    }

    public static GlobalShowDialog newInstance() {
        if (instance == null) {
            synchronized (GlobalShowDialog.class) {
                if (instance == null) {
                    instance = new GlobalShowDialog();
                }
            }
        }

        return instance;
    }

    public void joinOrganizationInvite(Activity activity, String inviteData) {
        String text = "尊敬的用户，您的平台账号被%1$s邀请加入，请确认。";
        Invitation invitation = JSONObject.parseObject(inviteData, Invitation.class);
        invitation.save();

        View view = activity.getLayoutInflater().inflate(R.layout.invite, null);
        ((TextView) view.findViewById(R.id.tv_desc)).setText(String.format(text, invitation.getFrom()));
        view.findViewById(R.id.tv_refuse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join(invitation, -2);
            }
        });

        view.findViewById(R.id.tv_join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join(invitation, 1);
            }
        });

        show = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .customView(view, false)
                .show();

        timer();

    }

    private void timer() {
        Observable.timer(6, TimeUnit.SECONDS)
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Long>() {
                    @Override
                    public void _next(Long aLong) {
                        if (show != null && show.isShowing()) {
                            show.dismiss();
                        }
                    }
                });
    }

    private void join(Invitation invitation, int type) {
        String organizationId = invitation.getOrganizationId();
        NetHelper.getApi()
                .acceptInvitation(User.getUserId(), organizationId, type)
                .compose(RxUtils.handleResult())
                .compose(RxUtils.applySchedule())
                .subscribe(new RxSubscriber<Object>() {
                    @Override
                    public void _next(Object o) {
                        cacheInvitationInfo(invitation.getId(), type);
                    }
                });
    }

    private void cacheInvitationInfo(int invitationId, int status) {
        if (show != null && show.isShowing()) {
            show.dismiss();
        }
        Invitation invitation = new Invitation();
        invitation.setStatus(status);
        invitation.update(invitationId);
    }


}
