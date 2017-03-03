package lilun.com.pension.module.bean;

import java.io.Serializable;

/**
 * 位置经纬度信息
 * Created by zp on 2017/3/1.
 */

public class LocationBean implements Serializable {
    private float lat;
    private float lng;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
