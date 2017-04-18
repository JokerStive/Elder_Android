package lilun.com.pension.ui.tourism.list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
*旅游的筛选条件
*@author yk
*create at 2017/4/14 11:42
*email : yk_developer@163.com
*/
public class TourismFilter {

    public TourismFilter() {
        where = new WhereBean();

    }

    /**
     * where : {"title":{"like":"来看看"},"price":{"between":[100,200]},"departure":"来看看","extand.destinations":""}
     */



    public WhereBean where ;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        @SerializedName("extend.tag")
        private String tag; // FIXME check this code

        public WhereBean() {

        }

        /**
         * title : {"like":"来看看"}
         * price : {"between":[100,200]}
         * departure : 来看看
         * extand.destinations :
         *
         *
         */



        public TitleBean title;
        public PriceBean price;

        public String getCategoryId() {
            return categoryId;
        }

        public WhereBean setCategoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        @SerializedName("extend.departure")
        public String departure;
        public String categoryId;

        @SerializedName("extend.destinations")
        private String destination;



        public TitleBean getTitle() {
            return title;
        }

        public void setTitle(TitleBean title) {
            this.title = title;
        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public static class TitleBean {
            /**
             * like : 来看看
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
