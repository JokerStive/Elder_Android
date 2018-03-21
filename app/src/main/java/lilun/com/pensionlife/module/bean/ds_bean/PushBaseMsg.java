package lilun.com.pensionlife.module.bean.ds_bean;

import org.litepal.crud.DataSupport;

/**
 *  MQTT 推送聊天室消息内容对象体
 *
 * Created by zp on 2017/8/15.
 */

public class PushBaseMsg extends DataSupport{
    public static final String MODEL_ORG_ACT = "OrganizationActivity";
    public static final String VERB_ADDED = "added";
    public static final String VERB_UPDATED = "updated";
    public static final String VERB_HELP = "help";
    public static final String VERB_CHAR = "chat";
    public static final String VERB_KICK = "kick";
    public static final String VERB_QUIT = "quit";
    public static final String VERB_JOIN = "join";


    public String model;
    public String verb;
    private int chatType;    //聊天类别 0-社区活动 1-订单

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }


    //判断是新增活动
    public boolean isNewActivity() {
        boolean bool = false;
        if (VERB_ADDED.equals(getVerb()) && MODEL_ORG_ACT.equals(getModel())) {
            bool = true;
        }
        return bool;
    }//判断是新增活动
    public boolean isUpdateActivity() {
        boolean bool = false;
        if (VERB_UPDATED.equals(getVerb()) && MODEL_ORG_ACT.equals(getModel())) {
            bool = true;
        }
        return bool;
    }

    //判断是聊天、加入、踢人、退出信息
    public boolean isChatInfo() {
        boolean bool = false;
        if (VERB_CHAR.equals(getVerb())) {
            bool = true;
        } else if (VERB_JOIN.equals(getVerb())) {
            bool = true;
        } else if (VERB_KICK.equals(getVerb())) {
            bool = true;
        } else if (VERB_QUIT.equals(getVerb())) {
            bool = true;
        }
        return bool;
    }
}
