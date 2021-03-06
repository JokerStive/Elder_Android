package lilun.com.pensionlife.module.bean;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * 互助模型
 *
 * @author yk
 *         create at 2017/2/13 10:49
 *         email : yk_developer@163.com
 */
public class OrganizationAid extends DataSupport implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id : string
     * title : string
     * kind : 0
     * address : string
     * priority : 0
     * status : 0
     * tags : ["string"]
     * price : 0
     * memo : string
     * mobile : string
     * answerId : string
     * rankId : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     * visible : 0
     * image : {}
     */

    private String id;
    private String title;
    private Integer kind;
    private String address;
    private Integer priority;
    private Integer status;
    private Double price;
    private String memo;
    private String mobile;
    private String answerId;
    private String rankId;
    private String createdAt;
    public String updatedAt;
    public String creatorId;
    public String creatorName;
    public String updatorId;
    public String updatorName;
    private String organizationId;
    public Integer visible;

    public Boolean getDraft() {
        return isDraft;
    }

    public OrganizationAid setDraft(Boolean draft) {
        isDraft = draft;
        return this;
    }

    public Boolean isDraft;
    public List<String> tags;
    public List<String> partnerList;

    public List<String> getPartnerList() {
        return partnerList;
    }

    public OrganizationAid setPartnerList(List<String> partnerList) {
        this.partnerList = partnerList;
        return this;
    }

    public List<String> image;

    public List<String> getImage() {
        return image;
    }

    public OrganizationAid setImage(List<String> image) {
        this.image = image;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
