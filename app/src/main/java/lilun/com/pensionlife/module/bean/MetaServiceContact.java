package lilun.com.pensionlife.module.bean;

import com.alibaba.fastjson.JSONObject;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 联系人模板
 */
public class MetaServiceContact extends BaseBean {

    /**
     * id : string
     * name : string
     * organizationId : string
     * title : string
     * category : string
     * settings : {}
     * health : 1
     * age : 0
     * education : 1
     * political : 1
     * guide : Y
     * photo : string
     * relations : {}
     */

    private String id;
    private String name;
    private String organizationId;
    private String title;
    private String category;
    private JSONObject settings;
    private int health;
    private int age;
    private int education;
    private int political;
    private String guide;
    private String photo;
    private RelationsBean relations;

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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public JSONObject getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = settings;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEducation() {
        return education;
    }

    public void setEducation(int education) {
        this.education = education;
    }

    public int getPolitical() {
        return political;
    }

    public void setPolitical(int political) {
        this.political = political;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public RelationsBean getRelations() {
        return relations;
    }

    public void setRelations(RelationsBean relations) {
        this.relations = relations;
    }

    public static class SettingsBean {
    }

    public static class RelationsBean {
    }
}
