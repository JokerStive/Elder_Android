package lilun.com.pension.ui.agency.list;

import java.util.List;

/**
 * 机构产品的筛选条件
 *
 * @author yk
 *         create at 2017/4/17 14:25
 *         email : yk_developer@163.com
 */
public class ProductFilter {

    public ProductFilter() {
        where = new WhereBean();
    }

    /**
     * where : {"title":{"like":""},"areasList":"","price":{"between":[100,300]},"score":4}
     */


    public WhereBean where;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        /**
         * title : {"like":""}
         * areasList :
         * price : {"between":[100,300]}
         * score : 4
         */

        private TitleBean title;
        private String areasList;
        private PriceBean price;
        private int score;

        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public String getAreasList() {
            return areasList;
        }

        public void setAreasList(String areasList) {
            this.areasList = areasList;
        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
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

        public static class PriceBean {
            private List<Integer> between;

            public List<Integer> getBetween() {
                return between;
            }

            public void setBetween(List<Integer> between) {
                this.between = between;
            }
        }
    }
}
