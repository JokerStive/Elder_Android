package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
 * Created by zp on 2017/4/13.
 */

public class Phone extends BaseBean {
    private String mobile;

    public Phone(String phone) {
        mobile = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
