package lilun.com.pensionlife.module.bean.ds_bean;

/**
 * 将OrganizationActivityDS对象数据存入数据库 已读后该数据删除
 * Created by zp on 2017/8/15.
 */

public class ActivityCategoryMsg extends PushBaseMsg {
    OrganizationActivityDS data;

    public OrganizationActivityDS getData() {
        data.setModel(getModel());
        data.setVerb(getVerb());
        return data;
    }

    public void setData(OrganizationActivityDS data) {
        this.data = data;
    }
}
