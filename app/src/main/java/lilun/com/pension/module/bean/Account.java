package lilun.com.pension.module.bean;

import java.io.Serializable;
import java.util.List;

/**
*账户模型
*@author yk
*create at 2017/2/6 15:30
*email : yk_developer@163.com
*/
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id : string
     * mobile : string
     * mobileVerified : true
     * name : string
     * username : string
     * email : string
     * defaultOrganizationId : 0
     * isAdmin : true
     * realm : string
     * credentials : {}
     * challenges : {}
     * emailVerified : true
     * status : string
     * picture : string
     * roles : [{}]
     * _perms : [{}]
     */

    private String id;
    private String mobile;
    private Boolean mobileVerified;
    private String name;
    private String username;
    private String email;
    private String defaultOrganizationId;
    private String realm;
    private Boolean emailVerified;
    private String status;
    private List<IconModule> picture;
    private String visibility;

    public boolean isCustomer() {
        return isCustomer;
    }

    public Account setCustomer(boolean customer) {
        isCustomer = customer;
        return this;
    }

    private String password;
    private boolean isCustomer=true;
    private List<String> roles;
    private List<String> _perms;

    public String getVisibility() {
        return visibility;
    }

    public Account setVisibility(String visibility) {
        this.visibility = visibility;
        return this;
    }

    public OrganizationAccount getOa() {
        return defaultOrganization;
    }

    public Account setOa(OrganizationAccount oa) {
        this.defaultOrganization = oa;
        return this;
    }

    private OrganizationAccount defaultOrganization;


    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDefaultOrganizationId() {
        return defaultOrganizationId;
    }

    public void setDefaultOrganizationId(String defaultOrganizationId) {
        this.defaultOrganizationId = defaultOrganizationId;
    }



    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public Boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<IconModule>  getPicture() {
        return picture;
    }

    public void setPicture(List<IconModule>  picture) {
        this.picture = picture;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> get_perms() {
        return _perms;
    }

    public void set_perms(List<String> _perms) {
        this._perms = _perms;
    }




}