package lilun.com.pension.module.bean;

import lilun.com.pension.base.BaseBean;

/**
*评价模型
*@author yk
*create at 2017/3/1 10:03
*email : yk_developer@163.com
*/
public class Rank extends BaseBean{

    /**
     * id : string
     * ranking : 0
     * description : string
     * whatModel : string
     * whatId : string
     */

    private String id;
    private int ranking;
    private String description;
    private String whatModel;
    private String whatId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWhatModel() {
        return whatModel;
    }

    public void setWhatModel(String whatModel) {
        this.whatModel = whatModel;
    }

    public String getWhatId() {
        return whatId;
    }

    public void setWhatId(String whatId) {
        this.whatId = whatId;
    }
}
