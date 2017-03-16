package lilun.com.pension.module.bean;

import java.util.ArrayList;

import lilun.com.pension.base.BaseBean;

/**
 * Created by zp on 2017/2/23.
 */

public class EdusColleageCourse extends BaseBean {
    public static int TYPE = 2;
    private String name;
    private String content;
    private String plan;
    private String teacher;
    private String startDate;
    private String endDate;
    private String startCourseTime;// 上课时间 ,
    private String endCourseTime;// 下课时间 ,


    private String startSingnDate;// 开课时间 ,
    private String endSingnDate;// 开课时间 ,
    private int maxCount;
    private int maxOnlineCount;
    private int count;
    private String id;
    private String contactId;
    private String organizationEduId;
    private String schoolId;
    private ArrayList<String> joinerList;
    private ArrayList<IconModule> picture;
    private Account contact;
    private ElderEdusColleage school;


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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setEndCourseTime(String endCourseTime) {
        this.endCourseTime = endCourseTime;
    }

    public void setEndSingnDate(String endSingnDate) {
        this.endSingnDate = endSingnDate;
    }

    public void setStartCourseTime(String startCourseTime) {
        this.startCourseTime = startCourseTime;
    }

    public String getEndCourseTime() {
        return endCourseTime;
    }

    public String getStartSingnDate() {
        return startSingnDate;
    }

    public void setStartSingnDate(String startSingnDate) {
        this.startSingnDate = startSingnDate;
    }

    public String getEndSingnDate() {
        return endSingnDate;
    }

    public String getStartCourseTime() {
        return startCourseTime;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxOnlineCount() {
        return maxOnlineCount;
    }

    public void setMaxOnlineCount(int maxOnlineCount) {
        this.maxOnlineCount = maxOnlineCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getOrganizationEduId() {
        return organizationEduId;
    }

    public void setOrganizationEduId(String organizationEduId) {
        this.organizationEduId = organizationEduId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public ArrayList<IconModule> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<IconModule> picture) {
        this.picture = picture;
    }

    public ArrayList<String> getJoinerList() {
        return joinerList;
    }

    public void setJoinerList(ArrayList<String> accountIds) {
        this.joinerList = accountIds;
    }

    public Account getContact() {
        return contact;
    }

    public void setContact(Account contact) {
        this.contact = contact;
    }

    public ElderEdusColleage getSchool() {
        return school;
    }

    public void setSchool(ElderEdusColleage school) {
        this.school = school;
    }

    public ElderEdus toElderEdus() {
        ElderEdus edus = new ElderEdus();
        edus.setType(TYPE);
        edus.setTitle(name);
        edus.setId(id);
        edus.setPicture(picture);
        edus.setVisible(visible);
        edus.setSelected(isSelected);
        edus.setContact(contact);
        return edus;
    }
}
