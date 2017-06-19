package lilun.com.pensionlife.module.bean;

import java.util.List;

/**
 * Created by Admin on 2017/4/7.
 */
public class ProductFilter {


    /**
     * where : {"or":[{"price":{"gt":"3000"}},{"price":{"lt":"300"}}]}
     */

    private WhereBean where;

    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public static class WhereBean {
        private List<OrBean> or;

        public List<OrBean> getOr() {
            return or;
        }

        public void setOr(List<OrBean> or) {
            this.or = or;
        }

        public static class OrBean {
            /**
             * price : {"gt":"3000"}
             */

            private PriceBean price;

            public PriceBean getPrice() {
                return price;
            }

            public void setPrice(PriceBean price) {
                this.price = price;
            }

            public static class PriceBean {
                /**
                 * gt : 3000
                 */

                private String gt;

                public String getGt() {
                    return gt;
                }

                public void setGt(String gt) {
                    this.gt = gt;
                }
            }
        }
    }
}
