package lilun.com.pensionlife.module.bean;

import java.util.List;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 行政区域组织
 * Created by zp on 2018/3/27.
 */

public class District extends BaseBean {
    private String id;           // 行政区域组织ID就是组织ID ,
    private List<String> providerIds;
    private List<String> pendingProviderIds;
    private String createdAt;// 创建时间 ,
    private String updatedAt; //更新时间 ,
    private String creatorId;// 创建者 ,
    private String creatorName; // 创建用户名 ,
    private String updatorId;//更新者 ,
    private String updatorName;// 更新者用户名 ,
    private String organizationId;

    public String getId() {
        return id;
    }

    public List<String> getProviderIds() {
        return providerIds;
    }

    public List<String> getPendingProviderIds() {
        return pendingProviderIds;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getUpdatorId() {
        return updatorId;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public String getOrganizationId() {
        return organizationId;
    }
}
