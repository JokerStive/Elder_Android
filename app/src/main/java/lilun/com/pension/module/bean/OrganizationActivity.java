package lilun.com.pension.module.bean;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
 * 活动模型
 *
 * @author yk
 *         create at 2017/2/14 11:35
 *         email : yk_developer@163.com
 */
public class OrganizationActivity extends BaseBean {
    public static int FINISHED = 2;  //已结束
    public static int STARTED = 1;  //已开始
    public static int UNSTARTED = 0; //未开始

    /**
     * title : string
     * address : string
     * location : {"lat":0,"lng":0}
     * priority : 0
     * startTime : 2017-02-14
     * druation : 0
     * repeatedType : string
     * repeatedCount : 0
     * partnerList : ["string"]
     * maxPartner : 0
     * id : string
     * masterId : string
     * categoryId : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     * icon : string
     */

    private String title;
    private String address;
    /**
     * lat : 0
     * lng : 0
     */

    private LocationBean location;
    private int priority;
    private String startTime;


    private String endTime;
    private int druation;
    private String repeatedType;


    private String repeatedDesc;
    private String description;
    private int repeatedCount;
    private Integer maxPartner;

    private String id;
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
    private ArrayList<IconModule> icon;
    private List<String> partnerList;
    private Account contact;
    private int partnerCount;

    public String getDescription() {
        return description;
    }

    public OrganizationActivity setDescription(String description) {
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

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
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
        return maxPartner;
    }

    public void setMaxPartner(Integer maxPartner) {
        this.maxPartner = maxPartner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<IconModule> getIcon() {
        return icon;
    }

    public void setIcon(ArrayList<IconModule> icon) {
        this.icon = icon;
    }

    public List<String> getPartnerList() {
        return partnerList;
    }

    public void setPartnerList(List<String> partnerList) {
        this.partnerList = partnerList;
    }

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

    public static int TYPE = 1;

    public ElderEdus toElderEdus() {
        ElderEdus edus = new ElderEdus();
        edus.setType(TYPE);
        edus.setTitle(title);
        edus.setAddress(address);

        edus.setLocation(location);
        edus.setPriority(priority);
        edus.setStartDate(startTime);
        edus.setDruation(druation);
        edus.setRepeatedType(repeatedType);
        edus.setRepeatedCount(repeatedCount);
        edus.setMaxPartner(maxPartner);
        edus.setId(id);
        edus.setMasterId(masterId);
        edus.setCategoryId(categoryId);
        edus.setCreatedAt(createdAt);
        edus.setUpdatedAt(updatedAt);
        edus.setCreatorId(creatorId);
        edus.setCreatorName(creatorName);
        edus.setUpdatorId(updatorId);
        edus.setUpdatorName(updatorName);
        edus.setOrganizationId(organizationId);
        edus.setPicture(icon);
        edus.setPartners(partnerList);
        edus.setContact(contact);
        return edus;
    }
}
