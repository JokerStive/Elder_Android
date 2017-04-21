package lilun.com.pension.ui.help.list;

/**
 * 互助列表的过滤条件
 *
 * @author yk
 *         create at 2017/4/17 13:12
 *         email : yk_developer@163.com
 */
public class HelpFilter {

    public HelpFilter() {
        where = new WhereBean();
    }

    /**
     * where : {"title":{"like":""},"kind":"","priority":"","status":""}
     */


    private WhereBean where;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        /**
         * title : {"like":""}
         * kind :
         * priority :
         * status :
         */

        private TitleBean title;
        private String kind;
        private String priority;
        private String status;

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class TitleBean {
            /**
             * like :
             */

            private String like;

            public String getLike() {
                return like;
            }

            public void setLike(String like) {
                this.like = like;
            }
        }
    }
}
