package lilun.com.pensionlife.module.bean;

/**
*缓存的数据
*@author yk
*create at 2017/4/27 17:33
*email : yk_developer@163.com
*/
public class CacheInfo  {
    private String first;
    private String second;
    private String third;

    public String getFirst() {
        return first;
    }

    public CacheInfo(String first, String second, String third, String fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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

    private String fourth;
}
