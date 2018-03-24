package lilun.com.pensionlife.module.bean;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.base.BaseBean;
import lilun.com.pensionlife.module.utils.StringUtils;

/**
 * 产品订单模型
 *
 * @author yk
 *         create at 2017/2/16 10:18
 *         email : yk_developer@163.com
 */
public class ProductOrder extends BaseBean {

    /**
     * id : string
     * name : string
     * status : string
     * mobile : string
     * address : string
     * description : string
     * callStatus : string
     * remark : string
     * registerDate : 2017-08-14T10:26:48.474Z
     * extend : {}
     * contact : {}
     * product : {}
     * objectId : string
     * assigneeId : string
     * orgCategoryId : string
     * canceledById : string
     * rankId : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * userProfileId : string
     */

    private String id;
    private String name;
    private String status;
    private String mobile;
    private String address;
    private String description;
    private String callStatus;
    private String remark;
    private String registerDate;
    private Contact contact;
    private OrganizationProduct productBackup;
    private String productBackupId;
    private OrganizationProduct productInfo;
    private String productId;
    private String assigneeId;
    private String orgCategoryId;
    private String canceledById;
    private String rankId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String userProfileId;

    //私有参数
    private int unRead=0;

    public String getProductBackupId() {
        return productBackupId;
    }

    public ProductOrder setProductBackupId(String productBackupId) {
        this.productBackupId = productBackupId;
        return this;
    }

    public OrganizationProduct getProductBackup() {
        return productBackup;
    }

    public void setProductBackup(OrganizationProduct productBackup) {
        this.productBackup = productBackup;
    }

    public String getId() {
        return id;
    }

    public ProductOrder setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductOrder setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ProductOrder setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public ProductOrder setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ProductOrder setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductOrder setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCallStatus() {
        return callStatus;
    }

    public ProductOrder setCallStatus(String callStatus) {
        this.callStatus = callStatus;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public ProductOrder setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public ProductOrder setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public Contact getContact() {
        return contact;
    }

    public ProductOrder setContact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public OrganizationProduct getProduct() {
        return productInfo;
    }

    public ProductOrder setProduct(OrganizationProduct product) {
        this.productInfo = product;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public ProductOrder setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public ProductOrder setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
        return this;
    }

    public String getOrgCategoryId() {
        return orgCategoryId;
    }

    public ProductOrder setOrgCategoryId(String orgCategoryId) {
        this.orgCategoryId = orgCategoryId;
        return this;
    }

    public String getCanceledById() {
        return canceledById;
    }

    public ProductOrder setCanceledById(String canceledById) {
        this.canceledById = canceledById;
        return this;
    }

    public String getRankId() {
        return rankId;
    }

    public ProductOrder setRankId(String rankId) {
        this.rankId = rankId;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public ProductOrder setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ProductOrder setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public ProductOrder setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public ProductOrder setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return this;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public ProductOrder setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
        return this;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public ProductOrder setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
        return this;
    }

    public String getUserProfileId() {
        return userProfileId;
    }

    public ProductOrder setUserProfileId(String userProfileId) {
        this.userProfileId = userProfileId;
        return this;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    /**
     * 获取订单关联产品的第一张图片
     * 若产品没有图片，则显示产品所在的类别图片
     */
    public String getProductImage() {
        if (productBackup == null) return null;
        if (productBackup.getImage() != null)
            return StringUtils.getFirstIcon(productBackup.getImage());
        if (productBackup.getImage() == null && productBackup.getOrgCategory() != null) {
            return productBackup.getOrgCategory().getIcon();
        }
        return null;
    }
}
