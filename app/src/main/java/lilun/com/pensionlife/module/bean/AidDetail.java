package lilun.com.pensionlife.module.bean;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
*互助详情模型
*@author yk
*create at 2017/3/2 9:35
*email : yk_developer@163.com
*/

public class AidDetail extends BaseBean{
    private OrganizationAid aid;
    private int replyCount;
    private boolean isCancelable;

    public boolean isCancelable() {
        return isCancelable;
    }

    public AidDetail setCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    private List<OrganizationReply> replyList;

    public OrganizationAid getAid() {
        return aid;
    }

    public AidDetail setAid(OrganizationAid aid) {
        this.aid = aid;
        return this;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public AidDetail setReplyCount(int replyCount) {
        this.replyCount = replyCount;
        return this;
    }

    public List<OrganizationReply> getReplyList() {
        return replyList;
    }

    public AidDetail setReplyList(List<OrganizationReply> replyList) {
        this.replyList = replyList;
        return this;
    }
}
