package lilun.com.pensionlife.module.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Admin on 2017/4/10.
 */
public class Setting implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * key : healthyDescription
     * type : string
     * title : 健康状态
     * enum : []
     */

    private String key;
    private String type;
    private String title;
    private List<String> enumX;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getEnumX() {
        return enumX;
    }

    public void setEnumX(List<String> enumX) {
        this.enumX = enumX;
    }
}
