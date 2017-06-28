package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

/**
 * 版本更新信息
 * Created by zp on 2017/6/28.
 */

public class AppVersion implements Serializable {
    String name;
    String version;
    String url;
    String description;
    boolean forced;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public boolean getForced() {
        return forced;
    }
}
