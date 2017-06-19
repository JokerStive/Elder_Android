package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

import lilun.com.pensionlife.base.BaseBean;

/**
 * 健康服务-  大学模型
 * Created by zp on 2017/2/22.
 */

public class HealtheaProduct extends BaseBean implements Serializable{

    private String name;
    private String address;
    private String mobile;

    private String description;
    private int id;
    private String createdAt;
    private String updatedAt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
