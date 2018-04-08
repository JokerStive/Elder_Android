package lilun.com.pensionlife.ui.education.colleage_list;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lilun.com.pensionlife.app.User;

/**
 * Created by Admin on 2017/10/11.
 */
public class aggregateFilter {

    public aggregateFilter() {
        AggregateBean aggregate = new AggregateBean();
        AggregateBean.GroupBean groupBean = new AggregateBean.GroupBean();
        groupBean.setId("$organizationId");
        aggregate.setGroup(groupBean);
        setAggregate(aggregate);

        WhereBean whereBean = new WhereBean();
        WhereBean.AreaIdsBean areaIdsBean = new WhereBean.AreaIdsBean();
        areaIdsBean.setInq(User.levelIds(true));
        whereBean.setAreaIds(areaIdsBean);
        setWhere(whereBean);
    }

    /**
     * visible : 0
     * where : {"tag.kind":"college","areaIds":{"inq":["1","2"]}}
     * aggregate : {"group":{"id":"$organizationId"}}
     */

    private int visible = 0;

    public int getVisible() {
        return visible;
    }

    private WhereBean where;
    private AggregateBean aggregate;


    public WhereBean getWhere() {
        return where;
    }

    public void setWhere(WhereBean where) {
        this.where = where;
    }

    public AggregateBean getAggregate() {
        return aggregate;
    }

    public void setAggregate(AggregateBean aggregate) {
        this.aggregate = aggregate;
    }

    public static class WhereBean {
        @SerializedName("tag.kind")
        private String kind = "college";
        private AreaIdsBean areaIds;

        public String getKind() {
            return kind;
        }

        public AreaIdsBean getAreaIds() {
            return areaIds;
        }

        public void setAreaIds(AreaIdsBean areaIds) {
            this.areaIds = areaIds;
        }

        public static class AreaIdsBean {
            private List<String> inq;

            public List<String> getInq() {
                return inq;
            }

            public void setInq(List<String> inq) {
                this.inq = inq;
            }
        }
    }

    public static class AggregateBean {
        /**
         * group : {"id":"$organizationId"}
         */

        private GroupBean group;

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public static class GroupBean {
            /**
             * id : $organizationId
             */

            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
