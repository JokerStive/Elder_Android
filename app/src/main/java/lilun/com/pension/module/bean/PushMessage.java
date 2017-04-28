package lilun.com.pension.module.bean;

import org.litepal.crud.DataSupport;

/**
*极光推送过来的消息
*@author yk
*create at 2017/3/15 15:18
*email : yk_developer@163.com
*/
public class PushMessage  extends DataSupport{
    private String model;
    private String verb;
    private String data;

    public String getModel() {
        return model;
    }

    public PushMessage setModel(String model) {
        this.model = model;
        return this;
    }

    public String getVerb() {
        return verb;
    }

    public PushMessage setVerb(String verb) {
        this.verb = verb;
        return this;
    }

    public String getData() {
        return data;
    }

    public PushMessage setData(String data) {
        this.data = data;
        return this;
    }
}
