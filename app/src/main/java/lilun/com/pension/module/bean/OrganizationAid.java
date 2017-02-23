package lilun.com.pension.module.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.app.OrganizationChildrenConfig;

/**
*互助模型
*@author yk
*create at 2017/2/13 10:49
*email : yk_developer@163.com
*/
public class OrganizationAid extends MultiItemEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * title : string
     * kind : 0
     * address : string
     * priority : 0
     * price : 0
     * memo : string
     * id : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     */

    private String title;

    public int getStatus() {
        return status;
    }

    public OrganizationAid setStatus(int status) {
        this.status = status;
        return this;
    }

    private int status;
    private int kind;
    private String address;
    private int priority;
    private int price;
    private String memo;
    private String id;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;

    public List<OrganizationReply> getReplies() {
        return replyList;
    }

    public OrganizationAid setReplies(List<OrganizationReply> replies) {
        this.replyList = replies;
        return this;
    }

    private List<OrganizationReply> replyList;

    public OrganizationAid() {
        setOrganizationId(OrganizationChildrenConfig.aid());
//        setItemType(getKind());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
}
