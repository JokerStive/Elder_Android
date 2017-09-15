package lilun.com.pensionlife.module.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 课程的类别
 *
 * @author yk
 *         create at 2017/9/12 13:29
 *         email : yk_developer@163.com
 */
public class OrganizationProductCategory extends AbstractExpandableItem<OrganizationProductCategory> implements MultiItemEntity,Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id : string
     * name : string
     * tag : {}
     * parentId : string
     * organizationId : string
     * visible : 0
     */

    private String id;
    private String name;
    private String title;
    private int level = 0;
    private Map<String, String> tag;

    public String getTitle() {
        return title;
    }

    public OrganizationProductCategory setTitle(String title) {
        this.title = title;
        return this;
    }

    private String parentId;
    private String organizationId;
    private int visible;
    private List<IconModule> icon;

    public List<IconModule> getIcon() {
        return icon;
    }

    public OrganizationProductCategory setIcon(List<IconModule> icon) {
        this.icon = icon;
        return this;
    }
//    @Override
//    public int getLevel() {
//        return 0;
//    }
//
//    @Override
//    public int getItemType() {
//        return 0;
//    }

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

    public Map<String, String> getTag() {
        return tag;
    }

    public void setTag(Map<String, String> tag) {
        this.tag = tag;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
