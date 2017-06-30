package lilun.com.pensionlife.module.bean;

import java.io.Serializable;
import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 组织模型
 *
 * @author yk
 *         create at 2017/2/21 11:34
 *         email : yk_developer@163.com
 */

public class Organization extends BaseBean {


    /**
     * id : string
     * name : string
     * parentId : string
     * description : {"description":"","adress":"","property":"","requirements":"","bedsCount":"","chargingStandard":{"min":"","max":""}}
     * isInherited : true
     * icon : {}
     */

    private String id;
    private String name;
    private String parentId;


    /**
     * description :
     * adress :
     * property :
     * requirements :
     * bedsCount :
     * chargingStandard : {"min":"","max":""}
     */

    private DescriptionBean description;
    private boolean isInherited;
    private List<IconModule> icon;

    private Extension extension;

    public Extension getExtension() {
        return extension;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public DescriptionBean getDescription() {
        return description;
    }

    public void setDescription(DescriptionBean description) {
        this.description = description;
    }

    public boolean isIsInherited() {
        return isInherited;
    }

    public void setIsInherited(boolean isInherited) {
        this.isInherited = isInherited;
    }

    public List<IconModule> getIcon() {
        return icon;
    }

    public void setIcon(List<IconModule> icon) {
        this.icon = icon;
    }

    public static class DescriptionBean implements Serializable {
        private static final long serialVersionUID = 1L;
        private String description;
        private String adress;
        private String property;
        private String requirements;
        private String bedsCount;
        private int ranking;
        private int rankCount;

        public int getRanking() {
            return ranking;
        }

        public DescriptionBean setRanking(int ranking) {
            this.ranking = ranking;
            return this;
        }

        public int getRankCount() {
            return rankCount;
        }

        public DescriptionBean setRankCount(int rankCount) {
            this.rankCount = rankCount;
            return this;
        }

        /**
         * min :
         * max :
         */

        private ChargingStandardBean chargingStandard;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getRequirements() {
            return requirements;
        }

        public void setRequirements(String requirements) {
            this.requirements = requirements;
        }

        public String getBedsCount() {
            return bedsCount;
        }

        public void setBedsCount(String bedsCount) {
            this.bedsCount = bedsCount;
        }

        public ChargingStandardBean getChargingStandard() {
            return chargingStandard;
        }

        public void setChargingStandard(ChargingStandardBean chargingStandard) {
            this.chargingStandard = chargingStandard;
        }

        public static class ChargingStandardBean implements Serializable {
            private static final long serialVersionUID = 1L;
            private String min;
            private String max;

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }
        }
    }


    /**
     * 服务提供商数据
     */
    public static class Extension implements Serializable {
        private String city;//: "中国重庆重庆市",
        private String address;//: "中国重庆重庆市南岸区茶园新区",
        private String phone;//: "15236827810",
        private String id;//: "/社会组织/营利/商家/重庆俏姨妈家政",

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }


}
