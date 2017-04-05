package lilun.com.pension.module.bean;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
 * 老年教育-  大学模型
 * Created by zp on 2017/2/22.
 */

public class ElderEdusColleage extends BaseBean {

    /**
     * {
     * "name": "华南理工大学",
     * "address": "天河区五山路华南理工大学教工活动中心",
     * "mobile": "18888888888",
     * "location": {
     * "lat": 23.0503413921,
     * "lng": 113.4010890138
     * },
     * "description": "华南理工大学（South China University of Technology），简称华南理工，位于广东省广州市，创建于1934年，是历史悠久、享有盛誉的中国著名高等学府。是中华人民共和国教育部直属的全国重点大学、首批国家“211工程”、“985工程”重点建设院校之一，入选“千人计划”、“111计划”和“卓越工程师教育培养计划”、“卓越法律人才教育培养计划”，也是“建筑老八校”之一、“卓越大学联盟”、“中俄工科大学联盟”、“中欧工程教育平台”主要成员。 经过60多年的建设和发展，华南理工大学成为以工见长，理工结合，管、经、文、法、医等多学科协调发展的综合性研究型大学。轻工技术与工程、食品科学与工程、城乡规划学、材料科学与工程、建筑学、化学工程与技术、风景园林学等学科整体水平进入全国前十位。化学、材料学、工程学、农业科学、物理学、生物学与生物化学、计算机科学7个学科进入国际ESI全球排名前1%。[1] 截止2016年1月，华南理工大学（不含民办独立广州学院）共设有25个学院；25个博士学位授权一级学科，42个硕士学位授权一级学科，107个博士点，193个硕士点，2个一级学科国家重点学科，3个二级学科国家重点学科，2个国家重点（培育）学科，3个国家重点实验室、2个国家工程研究中心、3个国家工程技术研究中心。[1] “十三五”规划对学校一流大学、一流学科的建设定出了明确的目标，在建校100周年即2052年学校建成世界一流大学。",
     * "id": 1,
     * "createdAt": "2017-02-22T07:18:27.490Z",
     * "updatedAt": "2017-02-22T07:18:27.490Z",
     * "visible": 0,
     * "picture": [
     * {
     * "description": "大图标",
     * "fileName": "老年大学_01.png"
     * }
     */
    private String name;
    private String address;
    private String mobile;
    private LocationBean location;
    private String description;
    private String id;
    private String createdAt;
    private String updatedAt;
    private ArrayList<IconModule> image;
    private Account contact;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
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

    public ArrayList<IconModule> getPicture() {
        return image;
    }

    public void setPicture(ArrayList<IconModule> picture) {
        this.image = picture;
    }

    public Account getContact() {
        return contact;
    }

    public void setContact(Account contact) {
        this.contact = contact;
    }

    public ElderEdus toElderEdus() {
        ElderEdus edus = new ElderEdus();
        edus.setTitle(name);
        edus.setAddress(address);
        edus.setCreatedAt(createdAt);
        edus.setDescription(description);
        edus.setId(id);
        edus.setLocation(location);
        edus.setMobile(mobile);
        edus.setUpdatedAt(updatedAt);
        edus.setPicture(image);
        edus.setVisible(visible);
        edus.setSelected(isSelected);
        edus.setContact(contact);
        return edus;
    }
}
