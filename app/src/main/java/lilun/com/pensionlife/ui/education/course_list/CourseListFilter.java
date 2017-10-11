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
    /**
     * order : createdAt DESC
     * where : {"orgCategoryId":"","organizationId":"","extend.termId":"","startTime":{"lte":""},"endTime":{"gte":""}}
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
         * orgCategoryId :
         * organizationId :
         * extend.termId :
         * startTime : {"lte":""}
         * endTime : {"gte":""}
         */

        public void setTime(String currentTime) {
            StartTimeBean startTimeBean = new StartTimeBean();
            EndTimeBean endTimeBean = new EndTimeBean();
            startTimeBean.setLte(currentTime);
            endTimeBean.setGte(currentTime);
            setStartTime(startTimeBean);
            setEndTime(endTimeBean);
        }

        private String orgCategoryId;
        private String organizationId;
        private Boolean isDraft = false;
        @SerializedName("extend.termId")
        private String termId; // FIXME check this code
        private StartTimeBean startTime;
        private EndTimeBean endTime;

        public String getOrgCategoryId() {
            return orgCategoryId;
        }

        public void setOrgCategoryId(String orgCategoryId) {
            this.orgCategoryId = orgCategoryId;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId + "/#product";
        }

        public String getTermId() {
            return termId;
        }

        public void setTermId(String termId) {
            this.termId = termId;
        }

        public StartTimeBean getStartTime() {
            return startTime;
        }

        public void setStartTime(StartTimeBean startTime) {
            this.startTime = startTime;
        }

        public EndTimeBean getEndTime() {
            return endTime;
        }

        public void setEndTime(EndTimeBean endTime) {
            this.endTime = endTime;
        }


        public static class StartTimeBean {
            /**
             * lte :
             */

            private String lte;

            public String getLte() {
                return lte;
            }

            public void setLte(String lte) {
                this.lte = lte;
            }
        }

        public static class EndTimeBean {
            /**
             * gte :
             */

            private String gte;

            public String getGte() {
                return gte;
            }

            public void setGte(String gte) {
                this.gte = gte;
            }
        }
    }

    //
    public CourseListFilter(String organizationId) {
        where = new WhereBean();
        where.setOrganizationId(organizationId);
        setOrder("createdAt DESC");
    }
//
//    /**
//     * order :
//     * where : {"organizationId":"","orgCategoryId":""}
//     */
//
//    private String order;
//    private WhereBean where;
//
//    public String getOrder() {
//        return order;
//    }
//
//    public void setOrder(String order) {
//        this.order = order;
//    }
//
//    public WhereBean getWhere() {
//        return where;
//    }
//
//    public void setWhere(WhereBean where) {
//        this.where = where;
//    }
//
//    public static class WhereBean {
//        /**
//         * organizationId :
//         * orgCategoryId :
//         */
//
//        private String organizationId;
//        private String orgCategoryId;
//        @SerializedName("extend.termId")
//        private String termId;
//
//        public String getTermId() {
//            return termId;
//        }
//
//        public WhereBean setTermId(String termId) {
//            this.termId = termId;
//            return this;
//        }
//
//        public String getOrganizationId() {
//            return organizationId;
//        }
//
//        public void setOrganizationId(String organizationId) {
//            this = organizationId + "/#product";
//        }
//
//        public String getOrgCategoryId() {
//            return orgCategoryId;
//        }
//
//        public void setOrgCategoryId(String orgCategoryId) {
//            this.orgCategoryId = orgCategoryId;
//        }
//    }
}
