package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

/**
 * Created by yk on 2017/1/3.
 * 用户账户模型
 */
public class OrganizationAccount implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * visibility : 1
     * id : 58ae94640ab7eb74ec89a171
     * accountId : a882a250-f71b-11e6-9bfd-15b36ba89534
     * createdAt : 2017-02-23T07:51:00.460Z
     * updatedAt : 2017-02-23T07:51:00.460Z
     * organizationId : /地球村/中国/重庆市/南岸区/A小区/#staff
     */

    private int visibility;
    private String id;
    private String accountId;
    private String createdAt;
    private String updatedAt;
    private String organizationId;

    public OrganizationAccountProfile getOrganizationAccountProfile() {
        return organizationAccountProfile;
    }

    public OrganizationAccount setOrganizationAccountProfile(OrganizationAccountProfile organizationAccountProfile) {
        this.organizationAccountProfile = organizationAccountProfile;
        return this;
    }

    private String name;
    private OrganizationAccountProfile organizationAccountProfile;


    public String getName() {
        return name;
    }

    public OrganizationAccount setName(String name) {
        this.name = name;
        return this;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
}
