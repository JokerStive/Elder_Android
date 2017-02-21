package lilun.com.pension.module.bean;

import lilun.com.pension.base.BaseBean;

/**
*组织模型
*@author yk
*create at 2017/2/21 11:34
*email : yk_developer@163.com
*/

public class Organization extends BaseBean {


    private String id;
    private String name;
    private String parentId;
    private String description;
    private String icon;
    private boolean isLocal;
    private String idService;
    private boolean isInherited;


    public boolean isInherited() {
        return isInherited;
    }

    public Organization setInherited(boolean inherited) {
        isInherited = inherited;
        return this;
    }

    public String getIdService() {
        return idService;
    }

    public Organization setIdService(String idService) {
        this.idService = idService;
        return this;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public Organization setLocal(boolean local) {
        isLocal = local;
        return this;
    }

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
