package lilun.com.pension.module.bean;

import lilun.com.pension.base.BaseBean;

/**
 * Created by zp on 2017/2/23.
 */

public class EdusColleageCourse extends BaseBean {

    /**
     *  "name": "string",
     "content": "string",
     "plan": "string",
     "teacher": "string",
     "startDate": "2017-02-23",
     "endDate": "2017-02-23",
     "maxCount": 0,
     "maxOnlineCount": 0,
     "count": 0,
     "id": "string",
     "contactId": "string",
     "organizationEduId": "string",
     "schoolId": "string"
     */
    private String name;
    private String content;
    private String plan;
    private String teacher;
    private String startDate;
    private String endDate;
    private int maxCount;
    private int maxOnlineCount;
    private int count;
    private String id;
    private String contactId;
    private String organizationEduId;
    private String schoolId;

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
}
