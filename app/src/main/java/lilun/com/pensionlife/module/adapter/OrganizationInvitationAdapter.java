package lilun.com.pensionlife.module.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import lilun.com.pensionlife.R;
import lilun.com.pensionlife.base.QuickAdapter;
import lilun.com.pensionlife.module.bean.Invitation;
import lilun.com.pensionlife.module.utils.StringUtils;

/**
 * 展示互助列表的adapter
 *
 * @author yk
 *         create at 2017/2/13 11:27
 *         email : yk_developer@163.com
 */
public class OrganizationInvitationAdapter extends QuickAdapter<Invitation> {

    private String invitationInfo = "尊敬的用户，您的平台账号被%1$s邀请加入，请确认。";
    private String invitationTime = "%1$s  处理状态：%2$s";

    public OrganizationInvitationAdapter(List<Invitation> data) {
        super(R.layout.item_organization_invitation, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Invitation invitation) {
        String from = invitation.getFrom();
        helper.setText(R.id.tv_invitation_info, String.format(invitationInfo, from));

        String time = StringUtils.IOS2ToUTC(invitation.getTime(), 8);
        String status = "未处理";
        int statusNum = invitation.getStatus();
        if (statusNum == 1) {
            status = "加入";
        } else if (statusNum == -2) {
            status = "拒绝";
        }
        helper.setText(R.id.tv_invitation_time, String.format(invitationTime, time, status));
    }


}
