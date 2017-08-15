package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
*养老机构下的服务对应的用户信息的扩展属性
*@author yk
*create at 2017/8/10 17:10
*email : yk_developer@163.com
*/
public class AgencyContactExtension extends BaseBean {

    /**
     * relation : 朋友
     * reservationName : 梅梅
     * sex : 女
     * birthday : 1991-11-14
     * healthStatus : 不能自理
     * healthyDescription : 完全瘫痪，植物人
     */

    private String relation;
    private String reservationName;
    private String sex;
    private String birthday;
    private String healthStatus;
    private String healthyDescription;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getHealthyDescription() {
        return healthyDescription;
    }

    public void setHealthyDescription(String healthyDescription) {
        this.healthyDescription = healthyDescription;
    }
}
