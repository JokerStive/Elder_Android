package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
*产品订单模型
*@author yk
*create at 2017/2/16 10:18
*email : yk_developer@163.com
*/
public class ProductOrder extends BaseBean {

    private String id;
    private String name;
    private String status;
    private String mobile;
    private String address;
    private String description;
    private String callStatus;
    private String remark;
    private String registerDate;
    private String productId;
    private String assigneeId;
    private String canceledById;
    private String rankId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private Account assignee;
    private Contact contact;
    private String categoryId;
    private Contact userProfile;

    public Contact getUserProfile() {
        return userProfile;
    }

    public ProductOrder setUserProfile(Contact userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public ProductOrder setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
        return this;
    }

    private String userProfileId;

    public String getCategoryId() {
        return categoryId;
    }

    public ProductOrder setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Contact getContact() {
        return contact;
    }

    public ProductOrder setContact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public Account getAssignee() {
        return assignee;
    }

    public ProductOrder setAssignee(Account assignee) {
        assignee = assignee;
        return this;
    }

    public OrganizationProduct getProduct() {
        return product;
    }

    public ProductOrder setProduct(OrganizationProduct product) {
        this.product = product;
        return this;
    }

    private OrganizationProduct product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(String callStatus) {
        this.callStatus = callStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getCanceledById() {
        return canceledById;
    }

    public void setCanceledById(String canceledById) {
        this.canceledById = canceledById;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
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
}
