package lilun.com.pension.module.bean;

import java.io.Serializable;
import java.util.List;

/**
*活动详情模型
*@author yk
*create at 2017/3/14 10:10
*email : yk_developer@163.com
*/
public class ActivityDetail implements Serializable{
    private static final long serialVersionUID = 1L;
    private OrganizationActivity activity;

    public OrganizationActivity getActivity() {
        return activity;
    }

    public ActivityDetail setActivity(OrganizationActivity activity) {
        this.activity = activity;
        return this;
    }

    /**
     * isRegisterActivity : false

     * replyList : []
     */

    private boolean isRegisterActivity;
    private List<NestedReply> replyList;

    public boolean isIsRegisterActivity() {
        return isRegisterActivity;
    }

    public void setIsRegisterActivity(boolean isRegisterActivity) {
        this.isRegisterActivity = isRegisterActivity;
    }

    public List<NestedReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<NestedReply> replyList) {
        this.replyList = replyList;
    }
}
