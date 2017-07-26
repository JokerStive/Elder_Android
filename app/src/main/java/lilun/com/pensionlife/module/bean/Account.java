package lilun.com.pensionlife.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 账户模型
 *
 * @author yk
 *         create at 2017/2/6 15:30
 *         email : yk_developer@163.com
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private List<IconModule> image;
    private String visibility;
    private String location;
    /**
     * profile : {}
     */

    private ProfileBean profile;

    public String getLocation() {
        return location;
    }

    public Account setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getDefaultContactId() {
        return defaultContactId;
    }

    public Account setDefaultContactId(String defaultContactId) {
        this.defaultContactId = defaultContactId;
        return this;
    }

    private String defaultContactId;

    public boolean isCustomer() {
        return isCustomer;
    }

    public Account setCustomer(boolean customer) {
        isCustomer = customer;
        return this;
    }

    private String password;
    private Boolean isCustomer;
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

    public List<IconModule> getImage() {
        return image;
    }

    public void setImage(List<IconModule> image) {
        this.image = image;
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

    public ProfileBean getProfile() {
        return profile;
    }

    public void setProfile(ProfileBean profile) {
        this.profile = profile;
    }


    public static class ProfileBean implements Serializable {
        private static final long serialVersionUID = 1L;

        private String belongToDistrict;
        private String firstHelperPhone;
        private String address;

        public ProfileBean(String belongToDistrict, String firstHelperPhone, String address) {
            this.belongToDistrict = belongToDistrict;
            this.address = address;
            this.firstHelperPhone = firstHelperPhone;
        }

        public ProfileBean() {
        }

        public String getBelongToDistrict() {
            return belongToDistrict;
        }

        public ProfileBean setBelongToDistrict(String belongToDistrict) {
            this.belongToDistrict = belongToDistrict;
            return this;
        }

        public ProfileBean setFirstHelperPhone(String firstHelperPhone) {
            this.firstHelperPhone = firstHelperPhone;
            return this;
        }

        public String getFirstHelperPhone() {
            return firstHelperPhone == null ? "" : firstHelperPhone;
        }

        public String getAddress() {
            return address;
        }

        public ProfileBean setAddress(String address) {
            this.address = address;
            return this;
        }
    }
}