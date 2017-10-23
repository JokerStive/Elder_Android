package lilun.com.pensionlife.ui.education.course_list;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程列表筛选器
 *
 * @author yk
 *         create at 2017/9/11 14:04
 *         email : yk_developer@163.com
 */
public class CourseListFilter {
//    /**
//     * order : createdAt DESC
//     * where : {"orgCategoryId":"","organizationId":"","extend.termId":"","startTime":{"lte":""},"endTime":{"gte":""}}
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
//
//    public static class WhereBean {
//        /**
//         * orgCategoryId :
//         * organizationId :
//         * extend.termId :
//         * startTime : {"lte":""}
//         * endTime : {"gte":""}
//         */
//
//        public void setTime(String currentTime) {
//            StartTimeBean startTimeBean = new StartTimeBean();
//            EndTimeBean endTimeBean = new EndTimeBean();
//            startTimeBean.setLte(currentTime);
//            endTimeBean.setGte(currentTime);
//            setStartTime(startTimeBean);
//            setEndTime(endTimeBean);
//        }
//
//        private String orgCategoryId;
//        private String organizationId;
//        private Boolean isDraft = false;
//        @SerializedName("extend.termId")
//        private String termId; // FIXME check this code
//        private StartTimeBean startTime;
//        private EndTimeBean endTime;
//
//        public String getOrgCategoryId() {
//            return orgCategoryId;
//        }
//
//        public void setOrgCategoryId(String orgCategoryId) {
//            this.orgCategoryId = orgCategoryId;
//        }
//
//        public String getOrganizationId() {
//            return organizationId;
//        }
//
//        public void setOrganizationId(String organizationId) {
//            this.organizationId = organizationId + "/#product";
//        }
//
//        public String getTermId() {
//            return termId;
//        }
//
//        public void setTermId(String termId) {
//            this.termId = termId;
//        }
//
//        public StartTimeBean getStartTime() {
//            return startTime;
//        }
//
//        public void setStartTime(StartTimeBean startTime) {
//            this.startTime = startTime;
//        }
//
//        public EndTimeBean getEndTime() {
//            return endTime;
//        }
//
//        public void setEndTime(EndTimeBean endTime) {
//            this.endTime = endTime;
//        }
//
//
//        public static class StartTimeBean {
//            /**
//             * lte :
//             */
//
//            private String lte;
//
//            public String getLte() {
//                return lte;
//            }
//
//            public void setLte(String lte) {
//                this.lte = lte;
//            }
//        }
//
//        public static class EndTimeBean {
//            /**
//             * gte :
//             */
//
//            private String gte;
//
//            public String getGte() {
//                return gte;
//            }
//
//            public void setGte(String gte) {
//                this.gte = gte;
//            }
//        }
//    }
//
//    //
//    public CourseListFilter(String organizationId) {
//        where = new WhereBean();
//        where.setOrganizationId(organizationId);
//        setOrder("createdAt DESC");
//    }


    /**
     * order : createdAt ASC
     * where : {"and":[{"or":[{"startTime":{"lte":"2017-09-19 06:56:27"}},{"startTime":{"$exists":false}}]},{"or":[{"endTime":{"gte":"2017-09-19 06:56:27"}},{"endTime":{"$exists":false}}]}],"tag.kind":"college","orgCategoryId":"/社会组织/公益/商家/测试商家数据/教育服务/其他教育服务/老年教育服务/美工/油画","organizationId":"/社会组织/公益/商家/测试商家数据/#product","extend.termId":"/社会组织/公益/商家/测试商家数据/秋季开学"}
     */

    public CourseListFilter(String organizationId) {
        where = new WhereBean();
        where.setOrganizationId(organizationId);
        setOrder("createdAt DESC");
    }

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


    public void setTime(String currentTime) {
//        WhereBean.Or1 or1 = new WhereBean.Or1()
//        StartTimeBean startTimeBean = new StartTimeBean();
//        EndTimeBean endTimeBean = new EndTimeBean();
//        startTimeBean.setLte(currentTime);
//        endTimeBean.setGte(currentTime);
//        setStartTime(startTimeBean);
//        setEndTime(endTimeBean);

        //创建or1
        if (where.and.size() > 0) {
            return;
        }
        WhereBean.Or1 or1 = new WhereBean.Or1();
        //创建startTime1和startTime2
        WhereBean.Or1.startTime1 startTime1 = new WhereBean.Or1.startTime1();
        WhereBean.Or1.startTime2 startTime2 = new WhereBean.Or1.startTime2();
        //设置startTime1和startTime2
        startTime1.setStartTime(new WhereBean.Or1.startTime1.StartTimeBean().setLte(currentTime));
        startTime2.setStartTime(new WhereBean.Or1.startTime2.StartTimeBean());
        //把startTime1和startTime2放进创建or1
        or1.or.add(startTime1);
        or1.or.add(startTime2);


        //创建or2
        WhereBean.Or2 or2 = new WhereBean.Or2();
        //创建endTime1和endTime2
        WhereBean.Or2.endTime1 endTime1 = new WhereBean.Or2.endTime1();
        WhereBean.Or2.endTime2 endTime2 = new WhereBean.Or2.endTime2();
        //设置endTime1和endTime2
        endTime1.setEndTime(new WhereBean.Or2.endTime1.EndTimeBean().setGte(currentTime));
        endTime2.setEndTime(new WhereBean.Or2.endTime2.EndTimeBean());
        //把endTime1和endTime2放进创建or2
        or2.or.add(endTime1);
        or2.or.add(endTime2);

        //把or1 or2放进and
        where.and.add(or1);
        where.and.add(or2);


    }

    public static class WhereBean {
        @SerializedName("tag.kind")
        private String kind = "college"; // FIXME check this code
        private String orgCategoryId;
        private String organizationId;
        private boolean isDraft = false;

        public boolean isDraft() {
            return isDraft;
        }

        public WhereBean setDraft(boolean draft) {
            isDraft = draft;
            return this;
        }

        @SerializedName("extend.termId")
        private String termId; // FIXME check this code
        private List<Object> and = new ArrayList<>();

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

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

        public List<Object> getAnd() {
            return and;
        }

        public void setAnd(List<Object> and) {
            this.and = and;
        }

        //====================================
        public static class Or1 {

            private List<Object> or = new ArrayList<>();

            public List<Object> getOr() {
                return or;
            }

            public void setOr(List<Object> or) {
                this.or = or;
            }

            //===============================
            public static class startTime1 {

                private StartTimeBean startTime;

                public StartTimeBean getStartTime() {
                    return startTime;
                }

                public void setStartTime(StartTimeBean startTime) {
                    this.startTime = startTime;
                }

                public static class StartTimeBean {
                    private String lte;

                    public String getLte() {
                        return lte;
                    }

                    public StartTimeBean setLte(String lte) {
                        this.lte = lte;
                        return this;
                    }
                }
            }

            //===============================
            public static class startTime2 {

                /**
                 * startTime : {"$exists":false}
                 */

                private StartTimeBean startTime;

                public StartTimeBean getStartTime() {
                    return startTime;
                }

                public void setStartTime(StartTimeBean startTime) {
                    this.startTime = startTime;
                }

                public static class StartTimeBean {

                    private boolean $exists = false;

                }
            }


        }

//====================================


        //====================================
        public static class Or2 {

            private List<Object> or = new ArrayList<>();

            public List<Object> getOr() {
                return or;
            }

            public void setOr(List<Object> or) {
                this.or = or;
            }

            //===============================
            public static class endTime1 {

                /**
                 * endTime : {"gte":"2017-09-19 06:56:27"}
                 */

                private EndTimeBean endTime;

                public EndTimeBean getEndTime() {
                    return endTime;
                }

                public void setEndTime(EndTimeBean endTime) {
                    this.endTime = endTime;
                }

                public static class EndTimeBean {
                    /**
                     * gte : 2017-09-19 06:56:27
                     */

                    private String gte;

                    public String getGte() {
                        return gte;
                    }

                    public EndTimeBean setGte(String gte) {
                        this.gte = gte;
                        return this;
                    }
                }
            }

            //===============================
            public static class endTime2 {


                private EndTimeBean endTime;

                public EndTimeBean getEndTime() {
                    return endTime;
                }

                public void setEndTime(EndTimeBean endTime) {
                    this.endTime = endTime;
                }

                public static class EndTimeBean {

                    private boolean $exists = false;

                }


            }

//====================================
        }
    }

}
