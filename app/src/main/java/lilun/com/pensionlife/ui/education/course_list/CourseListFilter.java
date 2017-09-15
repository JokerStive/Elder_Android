package lilun.com.pensionlife.ui.education.course_list;

import com.google.gson.annotations.SerializedName;

/**
 * 课程列表筛选器
 *
 * @author yk
 *         create at 2017/9/11 14:04
 *         email : yk_developer@163.com
 */
public class CourseListFilter {


    public CourseListFilter(String organizationId) {
        where = new WhereBean();
        where.setOrganizationId(organizationId);
        setOrder("createdAt DESC");
    }

    /**
     * order :
     * where : {"organizationId":"","orgCategoryId":""}
     */

    private String order;
    private WhereBean where;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        /**
         * organizationId :
         * orgCategoryId :
         */

        private String organizationId;
        private String orgCategoryId;
        @SerializedName("extend.termId")
        private String termId;

        public String getTermId() {
            return termId;
        }

        public WhereBean setTermId(String termId) {
            this.termId = termId;
            return this;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId+ "/#product";
        }

        public String getOrgCategoryId() {
            return orgCategoryId;
        }

        public void setOrgCategoryId(String orgCategoryId) {
            this.orgCategoryId = orgCategoryId;
        }
    }
}
