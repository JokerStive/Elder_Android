package lilun.com.pensionlife.module.bean;

import java.io.Serializable;

/**
 * 缓存的数据
 *
 * @author yk
 *         create at 2017/4/27 17:33
 *         email : yk_developer@163.com
 */
public class CacheInfo implements Serializable {
    private String first;
    private String second;
    private String third;
    private String fourth;
    private int type;

    public CacheInfo(String first, String second, String third, String fourth, int type) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.type = type;
    }

    public String getFirst() {
        return first;
    }

    public CacheInfo setFirst(String first) {
        this.first = first;
        return this;
    }

    public String getSecond() {
        return second;
    }

    public CacheInfo setSecond(String second) {
        this.second = second;
        return this;
    }

    public String getThird() {
        return third;
    }

    public CacheInfo setThird(String third) {
        this.third = third;
        return this;
    }

    public String getFourth() {
        return fourth;
    }

    public CacheInfo setFourth(String fourth) {
        this.fourth = fourth;
        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
