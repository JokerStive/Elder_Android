package lilun.com.pensionlife.module.bean;

/**
 * Created by Admin on 2018/5/21.
 */
public class ServiceTerm {

    /**
     * id : string
     * moduleId : string
     * moduleName : string
     * moduleField : string
     * fkId : string
     * organizationId : string
     * visible : 0
     */

    private String id;
    private String moduleId;
    private String moduleName;
    private String moduleField;
    private String fkId;
    private String organizationId;
    private int visible;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleField() {
        return moduleField;
    }

    public void setModuleField(String moduleField) {
        this.moduleField = moduleField;
    }

    public String getFkId() {
        return fkId;
    }

    public void setFkId(String fkId) {
        this.fkId = fkId;
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
}
