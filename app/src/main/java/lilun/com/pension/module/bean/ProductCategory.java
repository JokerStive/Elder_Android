package lilun.com.pension.module.bean;

import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
*产品服务(那些老年教育，那些养老机构)分类模型
*@author yk
*create at 2017/2/13 9:26
*email : yk_developer@163.com
*/
public class ProductCategory extends BaseBean{

    /**
     * id : string
     * name : string （名称）
     * orderId : 0 (排序号)
     * description : string（显示的描述信息）
     * parentId : string（所属那个类别）
     * icon : string （图标地址）
     */

    private String id;
    private String name;
    private int orderId;
    private String description;
    private String parentId;
    private List<IconModule> icon;

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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<IconModule> getIcon() {
        return icon;
    }

    public void setIcon(List<IconModule> icon) {
        this.icon = icon;
    }
}
