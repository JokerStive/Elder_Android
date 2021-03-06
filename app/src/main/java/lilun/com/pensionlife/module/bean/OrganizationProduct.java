package lilun.com.pensionlife.module.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private String metaServiceContactId;


    private Double price;
    private Boolean isDraft;
    private String unit;
    private String licenseGgreement;
    private int stock;
    private int sold;
    private int rank;
    private Map<String, String> tag;
    private Map<String, Object> extend;
    private List<District> areas;
    private String startTime;
    private String endTime;
    private String createdAt;
    private String updatedAt;
    private String creatorId;
    private String creatorName;
    private String updatorId;
    private String updatorName;
    private String organizationId;
    //    private String categoryId;
    private String phone;
    private String orderType;
    private List<String> image;
    private List<String> areaIds;
    private String mobile;
    private String orgCategoryId;
    private OrganizationProductCategory orgCategory;
    private Organization serviceOrganization; //服务组织


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


    public Map<String, String> getTag() {
        return tag;
    }

    public void setTag(Map<String, String> tag) {
        this.tag = tag;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> images) {
        this.image = images;
    }

    public List<String> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<String> areaIds) {
        this.areaIds = areaIds;
    }

    public String getMobile() {
        return mobile;
    }

    public OrganizationProduct setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getOrgCategoryId() {
        return orgCategoryId;
    }

    public OrganizationProduct setOrgCategoryId(String orgCategoryId) {
        this.orgCategoryId = orgCategoryId;
        return this;
    }

    public int getRank() {
        return rank;
    }

    public OrganizationProduct setRank(int rank) {
        this.rank = rank;
        return this;
    }


    public Boolean getDraft() {
        return isDraft;
    }

    public OrganizationProduct setDraft(Boolean draft) {
        isDraft = draft;
        return this;
    }

    public String getLicenseGgreement() {
        return licenseGgreement;
    }

    public OrganizationProduct setLicenseGgreement(String licenseGgreement) {
        this.licenseGgreement = licenseGgreement;
        return this;
    }

    public OrganizationProductCategory getOrgCategory() {
        return orgCategory;
    }

    public OrganizationProduct setOrgCategory(OrganizationProductCategory orgCategory) {
        this.orgCategory = orgCategory;
        return this;
    }

    public String getMetaServiceContactId() {
        return metaServiceContactId;
    }

    public OrganizationProduct setMetaServiceContactId(String metaServiceContactId) {
        this.metaServiceContactId = metaServiceContactId;
        return this;
    }

    public Organization getServiceOrganization() {
        return serviceOrganization;
    }

    public void setServiceOrganization(Organization serviceOrganization) {
        this.serviceOrganization = serviceOrganization;
    }
//===============私有方法

    /**
     * 获取产品服务区域名
     *
     * @return
     */
    public List<String> getProductArea() {
        ArrayList<String> arealist = new ArrayList<>();
        if (areas == null) return arealist;
        for (District d : areas) {
            arealist.add(d.getId());
        }
        return arealist;
    }
}
