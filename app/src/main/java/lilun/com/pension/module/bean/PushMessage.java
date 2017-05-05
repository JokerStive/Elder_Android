package lilun.com.pension.module.bean;

import org.litepal.crud.DataSupport;

/**
 * 极光推送过来的消息
 *
 * @author yk
 *         create at 2017/3/15 15:18
 *         email : yk_developer@163.com
 */
public class PushMessage extends DataSupport {
    private String model;
    private String verb;
    private String data;
    private String from;
    private String to;
    private String time;
    private String activityId;  //活动id
    private String message;


    public String getModel() {
        return model;
    }

    public PushMessage setModel(String model) {
        this.model = model;
        return this;
    }

    public String getVerb() {
        return verb;
    }

    public PushMessage setVerb(String verb) {
        this.verb = verb;
        return this;
    }

    public String getData() {
        return data;
    }

    public PushMessage setData(String data) {
        this.data = data;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public PushMessage setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public PushMessage setTo(String to) {
        this.to = to;
        return this;
    }

    public String getTime() {
        return time;
    }

    public PushMessage setTime(String time) {
        this.time = time;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public PushMessage setActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public PushMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getJsonStr() {
        String ret = "{}";
        if (null != model) {
            ret = ret.substring(0, ret.length() - 1);
            ret += "\"model\":\"" + model + "\"}";
        }
        if (null != verb) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"verb\":\"" + verb + "\"}" : "\"verb\":\"" + verb + "\"}";
        }
        if (null != data) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"data\":\"" + data + "\"}" : "\"data\":\"" + data + "\"}";
        }
        if (null != from) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"from\":" + from + "}" : "\"from\":" + from + "}";
        }
        if (null != to) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"to\":" + to + "}" : "\"to\":" + to + "}";
        }
        if (null != time) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"time\":\"" + time + "\"}" : "\"time\":\"" + time + "\"}";
        }
        if (null != activityId) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"activityId\":\"" + activityId + "\"}" : "\"activityId\":\"" + activityId + "\"}";
        }
        if (null != message) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"message\":\"" + message + "\"}" : "\"message\":\"" + message + "\"}";
        }

        return ret;
    }
}
