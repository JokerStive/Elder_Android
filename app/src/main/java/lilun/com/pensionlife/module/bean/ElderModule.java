package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 普通模块（互助、老年教育、养老机构）分类模型
 *
 * @author yk
 *         create at 2017/2/13 9:20
 *         email : yk_developer@163.com
 */
public class ElderModule extends BaseBean {

    private String name;
    private String parent;
    private int orderId;
    private boolean enabled;
    private String service;
    private ServiceConfigBean serviceConfig;
    private int id;
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public ServiceConfigBean getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfigBean serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static class ServiceConfigBean implements Serializable{
        private int kind;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        private String category;

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }
    }
}
