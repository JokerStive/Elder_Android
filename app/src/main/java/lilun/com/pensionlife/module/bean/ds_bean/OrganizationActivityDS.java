package lilun.com.pensionlife.module.bean.ds_bean;

import java.util.ArrayList;

import lilun.com.pensionlife.module.bean.Account;

/**
 * MQTT消息新建活动 存于数据库
 *
 * @author yk
 *         create at 2017/2/14 11:35
 *         email : yk_developer@163.com
 */
public class OrganizationActivityDS extends PushBaseMsg {
    private String title;
    private String address;
    private int priority;
    private String startTime;
    private String endTime;
    private int druation;
    private String repeatedType;
    private String repeatedDesc;
    private String description;
    private int repeatedCount;
    private Integer maxPartner;

    private String actId;
    private String status;
    private String masterId;
    private String categoryId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private ArrayList<String> icon;
//    private List<String> partnerList;
//    private List<String> blackList;
    private Account contact;
    private int partnerCount;


    public String getDescription() {
        return description;
    }

    public OrganizationActivityDS setDescription(String description) {
        this.description = description;
        return this;
    }


    public String getRepeatedDesc() {
        return repeatedDesc;
    }

    public void setRepeatedDesc(String repeatedDesc) {
        this.repeatedDesc = repeatedDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public int getDruation() {
        return druation;
    }

    public void setDruation(int druation) {
        this.druation = druation;
    }

    public String getRepeatedType() {
        return repeatedType;
    }

    public void setRepeatedType(String repeatedType) {
        this.repeatedType = repeatedType;
    }

    public int getRepeatedCount() {
        return repeatedCount;
    }

    public void setRepeatedCount(int repeatedCount) {
        this.repeatedCount = repeatedCount;
    }

    public Integer getMaxPartner() {
        if (maxPartner == null) maxPartner = 0;
        return maxPartner;
    }

    public void setMaxPartner(Integer maxPartner) {
        this.maxPartner = maxPartner;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getMasterId() {
        return masterId;
    }

    public void setMasterId(String masterId) {
        this.masterId = masterId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public ArrayList<String> getIcon() {
        return icon;
    }

    public void setIcon(ArrayList<String> icon) {
        this.icon = icon;
    }

//    public List<String> getPartnerList() {
//        return partnerList;
//    }
//
//    public void setPartnerList(List<String> partnerList) {
//        this.partnerList = partnerList;
//    }

    public Account getContact() {
        return contact;
    }

    public void setContact(Account contact) {
        this.contact = contact;
    }

    public int getPartnerCount() {
        return partnerCount;
    }

    public void setPartnerCount(int partnerCount) {
        this.partnerCount = partnerCount;
    }

//    public List<String> getBlackList() {
//        return blackList;
//    }
//
//    public void setBlackList(List<String> blackList) {
//        this.blackList = blackList;
//    }
}
