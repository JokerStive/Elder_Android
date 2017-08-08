package lilun.com.pensionlife.module.bean;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 组织模型
 *
 * @author yk
 *         create at 2017/2/21 11:34
 *         email : yk_developer@163.com
 */

public class Organization extends BaseBean {


    /**
     * id : string
     * name : string
     * parentId : string
     * description : {"description":"","adress":"","property":"","requirements":"","bedsCount":"","chargingStandard":{"min":"","max":""}}
     * isInherited : true
     * icon : {}
     */

    private String id;
    private String name;
    private String parentId;


    /**
     * description :
     * adress :
     * property :
     * requirements :
     * bedsCount :
     * chargingStandard : {"min":"","max":""}
     */

    private boolean isInherited;
    private List<IconModule> icon;

    private Provider extension;

    public Provider getExtension() {
        return extension;
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


    public boolean isIsInherited() {
        return isInherited;
    }

    public void setIsInherited(boolean isInherited) {
        this.isInherited = isInherited;
    }

    public List<IconModule> getIcon() {
        return icon;
    }

    public void setIcon(List<IconModule> icon) {
        this.icon = icon;
    }


}
