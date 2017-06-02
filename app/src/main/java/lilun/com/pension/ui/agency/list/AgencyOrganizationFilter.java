package lilun.com.pension.ui.agency.list;

import com.google.gson.annotations.SerializedName;

/**
*养老机构 组织过滤器
*@author yk
*create at 2017/4/18 13:02
*email : yk_developer@163.com
*/
public class AgencyOrganizationFilter  {

    public AgencyOrganizationFilter() {
        this.where = new WhereBean();
    }

    /**
     * where : {"min":{"gte":1000},"max":{"lte":9000},"name":{"like":""},"description.ranking":4}
     */

    public String include;

    public void setInclude(String include) {
        this.include = include;
    }

    public WhereBean where;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        /**
         * min : {"gte":1000}
         * max : {"lte":9000}
         * name : {"like":""}
         * description.ranking : 4
         */

        @SerializedName("description.chargingStandard.min")
        private MinBean min;

        @SerializedName("description.chargingStandard.max")
        private MaxBean max;

        private NameBean name;
        @SerializedName("description.ranking")
        private Integer ranking; // FIXME check this code

        public MinBean getMin() {
            return min;
        }

        public void setMin(MinBean min) {
            this.min = min;
        }

        public MaxBean getMax() {
            return max;
        }

        public void setMax(MaxBean max) {
            this.max = max;
        }

        public NameBean getName() {
            return name;
        }

        public void setName(NameBean name) {
            this.name = name;
        }

        public Integer getRanking() {
            return ranking;
        }

        public void setRanking(Integer ranking) {
            this.ranking = ranking;
        }

        public static class MinBean {
            /**
             * gte : 1000
             */

            private Integer gte;

            public Integer getGte() {
                return gte;
            }

            public void setGte(Integer gte) {
                this.gte = gte;
            }
        }

        public static class MaxBean {
            /**
             * lte : 9000
             */

            private Integer lte;

            public Integer getLte() {
                return lte;
            }

            public void setLte(Integer lte) {
                this.lte = lte;
            }
        }

        public static class NameBean {
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
