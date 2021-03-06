package lilun.com.pensionlife.module.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lilun.com.pensionlife.app.User;

/**
 * 组织邀请消息
 *
 * @author yk
 *         create at 2018/1/26 11:13
 *         email : yk_developer@163.com
 */
public class Invitation extends DataSupport implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String userId;
    private String from;
    private String organizationId;
    private String time;

    public Invitation() {
        setUserId(User.getUserId());
    }

    public String getUserId() {
        return userId;
    }

    public Invitation setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    private String verb;
    private int status = 0;

    public int getStatus() {
        return status;
    }

    public Invitation setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getId() {
        return id;
    }

    public Invitation setId(int id) {
        this.id = id;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Invitation setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public Invitation setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Invitation setTime(String time) {
        this.time = time;
        return this;
    }

    public String getVerb() {
        return verb;
    }

    public Invitation setVerb(String verb) {
        this.verb = verb;
        return this;
    }
}
