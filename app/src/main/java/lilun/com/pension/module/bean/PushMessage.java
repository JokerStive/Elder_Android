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
    public static final String VERB_HELP = "help";
    public static final String VERB_CHAR = "chat";
    public static final String VERB_KICK = "kick";
    public static final String VERB_QUIT = "quit";
    public static final String VERB_JOIN = "join";
    private String model;
    private String verb;
    private String data;
    private String from;
    private String to;
    private String time;
    private String activityId;  //活动id
    private String message;
    private String priority;
    private String location;
    private String title;
    private String mobile;
    private String address;

    public String getPriority() {
        return priority;
    }

    public PushMessage setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public PushMessage setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PushMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PushMessage setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public PushMessage setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

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
            ret += ret.length() != 1 ? ",\"from\":\"" + from + "\"}" : "\"from\":\"" + from + "\"}";
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
        if (null != priority) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"priority\":\"" + priority + "\"}" : "\"priority\":\"" + priority + "\"}";
        }
        if (null != location) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"location\":" + location + "}" : "\"location\":" + location + "}";
        }
        if (null != title) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"title\":\"" + title + "\"}" : "\"title\":\"" + title + "\"}";
        }
        if (null != mobile) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"mobile\":\"" + mobile + "\"}" : "\"mobile\":\"" + mobile + "\"}";
        }
        if (null != address) {
            ret = ret.substring(0, ret.length() - 1);
            ret += ret.length() != 1 ? ",\"address\":\"" + address + "\"}" : "\"address\":\"" + address + "\"}";
        }
        return ret;
    }
    public String getAllField(){
        return "model,verb,data,from,to,time,activityId,message,priority,location,title,mobile,address" ;
    }
}
