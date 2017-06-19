package lilun.com.pensionlife.module.bean;

/**
 * Created by youke on 2017/1/3.
 * 登录返回数据模型
 */
public class TokenInfo {

    /**
     * id : fTkcgv0OpmaZozgV68voVYxDcVSCJW1mKT8FRUOW9xeOIHgHV1ABW9aM3ajl39OG
     * ttl : 1209600
     * created : 2017-01-03T08:17:38.316Z
     * userId : 203308f0-bdb3-11e6-ad6e-95b9d95e3980
     */

    private String id;
    private int ttl;
    private String created;
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
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
}
