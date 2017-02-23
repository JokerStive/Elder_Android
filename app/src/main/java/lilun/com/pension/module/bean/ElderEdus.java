package lilun.com.pension.module.bean;

import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
 * 老年教育产品模型
 * Created by zp on 2017/2/22.
 */

public class ElderEdus extends BaseBean {




    //数据来源类别
    public String source;

 //   private String name;
    private String address;
    private String mobile;
    private OrganizationActivity.LocationBean location;
    private String description;
    private String id;
    private String createdAt;
    private String updatedAt;
    private List<IconModule> picture;


    private String title;
    private int priority;
    private String startTime;
    private int druation;
    private String repeatedType;
    private int repeatedCount;
    private int maxPartner;

    private String masterId;
    private String categoryId;

    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private List<String> partners;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public OrganizationActivity.LocationBean getLocation() {
        return location;
    }

    public void setLocation(OrganizationActivity.LocationBean location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<IconModule> getPicture() {
        return picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getMaxPartner() {
        return maxPartner;
    }

    public void setMaxPartner(int maxPartner) {
        this.maxPartner = maxPartner;
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

    public List<String> getPartners() {
        return partners;
    }

    public void setPartners(List<String> partners) {
        this.partners = partners;
    }

    public void setPicture(List<IconModule> picture) {
        this.picture = picture;
    }
}
