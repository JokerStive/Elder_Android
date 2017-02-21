package lilun.com.pension.module.bean;

import java.io.Serializable;

/**
*公告模型
*@author yk
*create at 2017/2/6 15:30
*email : yk_developer@163.com
*/
public class Announcement  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String type;
    private String contentUrl;
    private String imageUrl;

    public String getContentUrl() {
        return contentUrl;
    }

    public Announcement setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public String getType() {
        return type;
    }

    public Announcement setType(String type) {
        this.type = type;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Announcement setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}
