package lilun.com.pension.module.bean;

import org.litepal.crud.DataSupport;

import lilun.com.pension.app.User;

/**
*推送过来的信息缓存
*@author yk
*create at 2017/6/9 15:08
*email : yk_developer@163.com
*/
public class CacheMsg extends DataSupport {
    private String userId;
    private int classify;
    private String data;

    public CacheMsg(String data) {
       this.userId = User.getUserId();
       this.data = data;
    }

    public String getData() {
        return data;
    }

    public CacheMsg setClassify(int classify) {
        this.classify = classify;
        return this;

    }

    public int getClassify() {
        return classify;
    }
}
