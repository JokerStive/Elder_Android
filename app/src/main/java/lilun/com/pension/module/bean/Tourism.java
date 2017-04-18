package lilun.com.pension.module.bean;

import java.io.Serializable;
import java.util.List;

import lilun.com.pension.base.BaseBean;

/**
 * 旅游模型
 *
 * @author yk
 *         create at 2017/4/13 16:44
 *         email : yk_developer@163.com
 */
public class Tourism extends BaseBean {


    private String id;
    private String name;
    private String title;
    private String context;
    private String contextType;
    private int price;
    private String unit;
    private int score;
    private String mobile;
    private ExtendBean extend;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    private List<IconModule> images;
    private String categoryId;
    private List<String> areasList;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContextType() {
        return contextType;
    }

    public void setContextType(String contextType) {
        this.contextType = contextType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(String updatorId) {
        this.updatorId = updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }


    public List<IconModule> getImages() {
        return images;
    }

    public void setImages(List<IconModule> images) {
        this.images = images;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getAreasList() {
        return areasList;
    }

    public void setAreasList(List<String> areasList) {
        this.areasList = areasList;
    }

    public static class ExtendBean implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * route : 重庆-海口、万宁、乐东、三亚、澄迈、邻水、 海南
         * traffic : 飞机往返
         * accommodation : 一晚豪华型住宿；4晚高档型住宿
         * attractions : 全程游览8个景点
         * gather : 门边拐拐处
         * feature : 海景酒店+5A景区+超值赠送
         * satisfaction : 100%满意
         * departure : 重庆出发
         * duration : 6天5晚
         * fee : 费用说明
         * distance : 路线特色
         * notice : 预定须知
         * tag : ["热门","国内游"]
         */

        private String route;
        private String traffic;
        private String accommodation;
        private String attractions;
        private String gather;
        private String feature;
        private String satisfaction;
        private String departure;
        private String duration;
        private String fee;
        private String distance;
        private String notice;
        private List<String> tag;
        private List<String> destinations;

        public List<String> getDestinations() {
            return destinations;
        }

        public ExtendBean setDestinations(List<String> destinations) {
            this.destinations = destinations;
            return this;
        }

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getTraffic() {
            return traffic;
        }

        public void setTraffic(String traffic) {
            this.traffic = traffic;
        }

        public String getAccommodation() {
            return accommodation;
        }

        public void setAccommodation(String accommodation) {
            this.accommodation = accommodation;
        }

        public String getAttractions() {
            return attractions;
        }

        public void setAttractions(String attractions) {
            this.attractions = attractions;
        }

        public String getGather() {
            return gather;
        }

        public void setGather(String gather) {
            this.gather = gather;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getSatisfaction() {
            return satisfaction;
        }

        public void setSatisfaction(String satisfaction) {
            this.satisfaction = satisfaction;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public List<String> getTag() {
            return tag;
        }

        public void setTag(List<String> tag) {
            this.tag = tag;
        }
    }


}
