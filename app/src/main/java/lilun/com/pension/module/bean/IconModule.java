package lilun.com.pension.module.bean;

import java.io.Serializable;

/**
*解析模块icon字段模型 
*@author yk
*create at 2017/2/15 15:43
*email : yk_developer@163.com
*/
public class IconModule implements Serializable{

    /**
     * fileName : 1477285278916.png
     * description : no description
     */

    private String fileName;
    private String description;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "{" +
                "description='" + description + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
