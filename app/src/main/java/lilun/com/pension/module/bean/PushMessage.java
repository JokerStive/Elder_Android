package lilun.com.pension.module.bean;

import org.litepal.crud.DataSupport;

/**
*极光推送过来的消息
*@author yk
*create at 2017/3/15 15:18
*email : yk_developer@163.com
*/
public class PushMessage  extends DataSupport{
    private String king;
    private String content;

    public String getKing() {
        return king;
    }

    public PushMessage setKing(String king) {
        this.king = king;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PushMessage setContent(String content) {
        this.content = content;
        return this;
    }
}
