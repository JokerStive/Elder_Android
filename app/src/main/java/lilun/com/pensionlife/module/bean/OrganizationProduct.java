package lilun.com.pensionlife.module.bean;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 从产品模型
 *
 * @author yk
 *         create at 2017/2/21 10:06
 *         email : yk_developer@163.com
 */
public class OrganizationProduct extends BaseBean {

    private String id;
    private String name;
    private String title;
    private String subTitle;
    private String context;
    private String contextType;
    private Double price;
    private String unit;
    private int stock;
    private int sold;
    private int rank;
    private String tag;

    public int getRank() {
        return rank;
    }

    public OrganizationProduct setRank(int rank) {
        this.rank = rank;
        return this;
    }

    private String extend;
    private String startTime;
    private String endTIme;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;

    public String getMobile() {
        return mobile;
    }

    public OrganizationProduct setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    private String organizationId;
    private String categoryId;
    private List<String> phone;
    private List<String> orderType;
    private List<IconModule> image;
    private List<String> areaIds;
    private String mobile;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(String endTIme) {
        this.endTIme = endTIme;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public List<String> getOrderType() {
        return orderType;
    }

    public void setOrderType(List<String> orderType) {
        this.orderType = orderType;
    }

    public List<IconModule> getImage() {
        return image;
    }

    public void setImage(List<IconModule> images) {
        this.image = images;
    }

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }
}
