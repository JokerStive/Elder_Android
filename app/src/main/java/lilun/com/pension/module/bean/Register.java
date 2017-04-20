package lilun.com.pension.module.bean;

import java.io.Serializable;

import lilun.com.pension.module.bean.Account;

/**
 * 账户模型
 *
 * @author yk
 *         create at 2017/2/6 15:30
 *         email : yk_developer@163.com
 */
public class Register implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String ttl;
    private String created;
    private String userId;
    private Account user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Account getUser() {
        return user;
    }

    public void setUser(Account user) {
        this.user = user;
    }
}