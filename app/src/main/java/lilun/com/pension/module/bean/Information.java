package lilun.com.pension.module.bean;

import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
*信息模型
*@author yk
*create at 2017/3/2 11:24
*email : yk_developer@163.com
*/
public class Information extends BaseBean {

    /**
     * id : string
     * name : string
     * slug : string
     * title : string
     * replyable : true
     * isCat : true
     * isDraft : false
     * contextType : txt
     * context : string
     * parentId : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     * picture : {}
     */

    private String id;
    private String name;
    private String slug;
    private String title;
    private boolean replyable;
    private boolean isCat;
    private boolean isDraft;
    private String contextType;
    private String context;
    private String parentId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private List<IconModule> picture;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReplyable() {
        return replyable;
    }

    public void setReplyable(boolean replyable) {
        this.replyable = replyable;
    }

    public boolean isIsCat() {
        return isCat;
    }

    public void setIsCat(boolean isCat) {
        this.isCat = isCat;
    }

    public boolean isIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public List<IconModule> getPicture() {
        return picture;
    }

    public void setPicture(List<IconModule> picture) {
        this.picture = picture;
    }

    public static class PictureBean {
    }
}
