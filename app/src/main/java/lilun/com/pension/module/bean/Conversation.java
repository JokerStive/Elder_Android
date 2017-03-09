package lilun.com.pension.module.bean;

/**
*回话模型
*@author yk
*create at 2017/3/7 11:58
*email : yk_developer@163.com
*/
public class Conversation {
    private String time;
    private String creatorId;
    private String content;

    public boolean isOwn() {
        return isOwn;
    }

    public Conversation setOwn(boolean own) {
        isOwn = own;
        return this;
    }

    private boolean isOwn;

    public String getTime() {
        return time;
    }

    public Conversation setTime(String time) {
        this.time = time;
        return this;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public Conversation setCreatorId(String creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Conversation setContent(String content) {
        this.content = content;
        return this;
    }

}
