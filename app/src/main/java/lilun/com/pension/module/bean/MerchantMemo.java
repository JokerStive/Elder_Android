package lilun.com.pension.module.bean;

/**
*商家对订单的备注
*@author yk
*create at 2017/4/12 13:18
*email : yk_developer@163.com
*/
public class MerchantMemo {
    private String status;
    private String callStatus;
    private String remark;

    public String getStatus() {
        return status;
    }

    public MerchantMemo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public MerchantMemo setCallStatus(String callStatus) {
        this.callStatus = callStatus;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public MerchantMemo setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public MerchantMemo setDelayTime(String delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    private String delayTime;
}
