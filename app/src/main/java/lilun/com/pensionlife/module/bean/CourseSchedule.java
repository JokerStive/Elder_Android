package lilun.com.pensionlife.module.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 课程表
 */
public class CourseSchedule extends BaseBean {

    /**
     * id : string
     * name : string
     * content : string
     * plan : string
     * extend : {}
     * startTime : 2018-03-20T02:49:08.011Z
     * endTime : 2018-03-20T02:49:08.011Z
     * courseTime : {}
     * location : string
     * contactId : string
     * schoolId : string
     * courseId : string
     * joinerList : ["string"]
     * organizationEduCourseId : string
     * createdAt : $now
     * updatedAt : $now
     * creatorId : string
     * creatorName : string
     * updatorId : string
     * updatorName : string
     * organizationId : string
     */

    private String id;
    private String name;
    private String content;
    private String plan;
    private JSONObject extend;
    private String startTime;
    private String endTime;
    private String location;
    private String classId;
    private String courseStartTime;
    private String courseEndTime;
    private String courseWeek;



    private String contactId;
    private String schoolId;
    private String courseId;
    private String organizationEduCourseId;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private List<String> joinerList;
    private List<OrganizationAccount> teachers;


    public String getCourseStartTime() {
        return courseStartTime;
    }

    public CourseSchedule setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
        return this;
    }

    public String getCourseEndTime() {
        return courseEndTime;
    }

    public CourseSchedule setCourseEndTime(String courseEndTime) {
        this.courseEndTime = courseEndTime;
        return this;
    }

    public String getCourseWeek() {
        return courseWeek;
    }

    public CourseSchedule setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
        return this;
    }

    public String getClassId() {
        return classId;
    }

    public CourseSchedule setClassId(String classId) {
        this.classId = classId;
        return this;
    }

    public List<OrganizationAccount> getTeachers() {
        return teachers;
    }

    public CourseSchedule setTeachers(List<OrganizationAccount> teachers) {
        this.teachers = teachers;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public JSONObject getExtend() {
        return extend;
    }

    public void setExtend(JSONObject extend) {
        this.extend = extend;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getOrganizationEduCourseId() {
        return organizationEduCourseId;
    }

    public void setOrganizationEduCourseId(String organizationEduCourseId) {
        this.organizationEduCourseId = organizationEduCourseId;
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

    public List<String> getJoinerList() {
        return joinerList;
    }

    public void setJoinerList(List<String> joinerList) {
        this.joinerList = joinerList;
    }


}
