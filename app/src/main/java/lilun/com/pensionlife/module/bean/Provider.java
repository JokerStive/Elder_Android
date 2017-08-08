package lilun.com.pensionlife.module.bean;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
*组织提供者信息
*@author yk
*create at 2017/8/8 11:19
*email : yk_developer@163.com
*/
public class Provider extends BaseBean {

    /**
     * id : string
     * type : 0
     * name : string
     * capital : 0
     * currency : CNY
     * city : string
     * license : string
     * address : string
     * phone : string
     * mobile : string
     * licenseExpired : 2017-08-08T03:17:54.341Z
     * orderType : [{}]
     * context : string
     * contextType : string
     * legalPersonId : string
     * pendingAreaIds : ["string"]
     * areaIds : ["string"]
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     */

    private String id;
    private int type;
    private String name;
    private int capital;
    private int maxPriceRange ;
    private int minPriceRange ;
    private int score ;
    private String currency;
    private String city;
    private String license;
    private String address;
    private String phone;

    public int getMaxPriceRange() {
        return maxPriceRange;
    }

    public Provider setMaxPriceRange(int maxPriceRange) {
        this.maxPriceRange = maxPriceRange;
        return this;
    }

    public int getMinPriceRange() {
        return minPriceRange;
    }

    public Provider setMinPriceRange(int minPriceRange) {
        this.minPriceRange = minPriceRange;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Provider setScore(int score) {
        this.score = score;
        return this;
    }

    private String mobile;
    private String licenseExpired;
    private String context;
    private String contextType;
    private String legalPersonId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private List<OrderTypeBean> orderType;
    private List<String> pendingAreaIds;
    private List<String> areaIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLicenseExpired() {
        return licenseExpired;
    }

    public void setLicenseExpired(String licenseExpired) {
        this.licenseExpired = licenseExpired;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getLegalPersonId() {
        return legalPersonId;
    }

    public void setLegalPersonId(String legalPersonId) {
        this.legalPersonId = legalPersonId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public List<OrderTypeBean> getOrderType() {
        return orderType;
    }

    public void setOrderType(List<OrderTypeBean> orderType) {
        this.orderType = orderType;
    }

    public List<String> getPendingAreaIds() {
        return pendingAreaIds;
    }

    public void setPendingAreaIds(List<String> pendingAreaIds) {
        this.pendingAreaIds = pendingAreaIds;
    }

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }

    public static class OrderTypeBean {
    }
}
