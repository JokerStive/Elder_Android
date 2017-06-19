package lilun.com.pensionlife.module.bean;

import lilun.com.pensionlife.base.BaseBean;

/**
*回复模型
*@author yk
*create at 2017/2/20 9:30
*email : yk_developer@163.com
*/

public class OrganizationReply extends BaseBean {

    /**
     * id : string
     * whatModel : string
     * whatId : string
     * content : string
     * enabled : true
     * createdAt : $now
     * updatedAt : $now
     * isDraft : false
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     */

    private String id;
    private String whatModel;
    private String whatId;
    private String content;
    private boolean enabled;
    private String createdAt;
    private String updatedAt;
    private boolean isDraft;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;

    public OrganizationReply() {
    }

    public OrganizationReply(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWhatModel() {
        return whatModel;
    }

    public void setWhatModel(String whatModel) {
        this.whatModel = whatModel;
    }

    public String getWhatId() {
        return whatId;
    }

    public void setWhatId(String whatId) {
        this.whatId = whatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public boolean isIsDraft() {
        return isDraft;
    }

    public void setIsDraft(boolean isDraft) {
        this.isDraft = isDraft;
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
