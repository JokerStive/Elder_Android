package lilun.com.pensionlife.module.bean;

/**
 * Created by Admin on 2017/11/6.
 */
public class QINiuToken {

    /**
     * token : 4nxVf4DvcocUOJl29Cig61rVmtDMeUPxAIxjuVl_:eAH-FfEWr4Npo8ELTQp3dQHgog8=:eyJtaW1lTGltaXQiOiJpbWFnZS8qIiwiY2FsbGJhY2tCb2R5VHlwZSI6ImFwcGxpY2F0aW9uL2pzb24iLCJjYWxsYmFja0JvZHkiOiJ7XCJrZXlcIjpcIiQoa2V5KVwiLFwiYnVja2V0XCI6XCIkKGJ1Y2tldClcIn0iLCJjYWxsYmFja1VybCI6Imh0dHA6Ly8xODMuMjMwLjE3NC40NTozMDExL2FwaS9Pcmdhbml6YXRpb25BaWRzLzYwY2Y1YWMwLWM1MTAtMTFlNy04MTVjLTI5MDc1N2EwYzlhMi9JbWFnZXFpbml1Q2FsbGJhY2siLCJzYXZlS2V5IjoiT3JnYW5pemF0aW9uQWlkL2ltYWdlLzYwY2Y1YWMwLWM1MTAtMTFlNy04MTVjLTI5MDc1N2EwYzlhMi8kKGZuYW1lKSIsImlzUHJlZml4YWxTY29wZSI6MSwic2NvcGUiOiJ2My1wdWJsaWM6T3JnYW5pemF0aW9uQWlkL2ltYWdlLzYwY2Y1YWMwLWM1MTAtMTFlNy04MTVjLTI5MDc1N2EwYzlhMi8iLCJkZWFkbGluZSI6MTUxMDIwNjUyMn0=
     * ttl : 300
     */

    private String token;
    private int ttl;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
