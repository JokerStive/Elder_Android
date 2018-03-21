package lilun.com.pensionlife.module.utils.mqtt;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import lilun.com.pensionlife.app.Event;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.base.BaseChatFragment;
import lilun.com.pensionlife.module.bean.ds_bean.ActivityCategoryMsg;
import lilun.com.pensionlife.module.bean.ds_bean.OrganizationActivityDS;
import lilun.com.pensionlife.module.bean.ds_bean.PushBaseMsg;
import lilun.com.pensionlife.module.bean.ds_bean.PushMessage;
import lilun.com.pensionlife.module.utils.ActUitls;


/**
 * Created by zp on 2017/8/15.
 */

public class MqttActivityHelper {
    String topic;
    String messageData;

    public MqttActivityHelper(String topic, String messageData) {
        this.topic = topic;
        this.messageData = messageData;
        dealMessage();
    }

    public void dealMessage() {
        // 不是活动相关的消息，返回
        if (!(topic.contains("%23activity") || topic.contains("%23order"))) return;

        //解析消息对象
        PushBaseMsg pushBaseMsg = new Gson().fromJson(messageData, new TypeToken<PushBaseMsg>() {
        }.getType());


        if (pushBaseMsg == null) return;
        //判断是新增活动 并且是活动的topic
        if ((pushBaseMsg.isNewActivity() || pushBaseMsg.isUpdateActivity())) {
            if (!topic.contains("%23activity")) return;
            //是当前小区可收到的活动,存入数据库
            ActivityCategoryMsg actCatMsg = new Gson().fromJson(messageData.replace("\"id\"", "\"actId\""), new TypeToken<ActivityCategoryMsg>() {
            }.getType());
            OrganizationActivityDS data = actCatMsg.getData();
            if (ActUitls.isParentTopActivity(User.getLocation(), topic.replace("/%23activity/.added", ""))) {
                Logger.d("topic: " + topic);
                //自己创建的活动不保存
                if (data != null && !User.getUserId().equals(data.getCreatorId())) {
                    data.save();
                }
                EventBus.getDefault().post(new Event.ActivityNew(actCatMsg));
            } else if (ActUitls.isParentTopActivity(User.getLocation(), topic.replace("/%23activity/.updated", ""))) {
                // 活动有更新  在update下  status = 101时 是解散活动,删除对应的数据并更新角标
                if (data != null && !User.getUserId().equals(data.getCreatorId()) && "101".equals(data.getStatus())) {
                    EventBus.getDefault().post(new Event.ActivityCancel(actCatMsg));
                }

            }

        } else if (pushBaseMsg.isChatInfo()) {
            PushMessage chatMessage = getPushMessageFromData(messageData);
            if (!(topic.contains("%23activity") || topic.contains("%23order"))) return;


            if (chatMessage != null) {
                chatMessage.setActivityId(getActivityId(topic));
                if (topic.contains("%23activity")) {
                    chatMessage.setChatType(0);
                    //不是自己发送的消息 且是当前小区可接收到的消息 设置未读，
                    String substring = topic.substring(topic.indexOf("/%23activity"));  //获取组织id之后的内容
                    String actOrg = topic.replace(substring, "");//得到组织id
                    try {
                        actOrg = URLDecoder.decode(actOrg, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //不是自己的消息 && 不是当前活动页面的消息  && 是用户当前小区及其上到市级  所发布的活动，设置为未读
                    if (chatMessage.getFrom() != null && !chatMessage.getFrom().contains(User.getUserId())
                            && ActUitls.isParentTopActivity(User.getLocation(), actOrg)
                            && !BaseChatFragment.isCurrentMsg(chatMessage.getActivityId())) {
                        chatMessage.setUnRead(true);
                        chatMessage.save();
                        EventBus.getDefault().post(new Event.NewChatMsg(chatMessage));
                    } else
                        chatMessage.save();

                } else if (topic.contains("%23order")) {
                    chatMessage.setChatType(1);
                    chatMessage.save();
                }
                //处理活动
                dealActivity(topic, chatMessage);
            }
        }
    }


    private PushMessage getPushMessageFromData(String messageData) {
        try {
            PushMessage pushMessage = new PushMessage();
            JSONObject jsonObject = new JSONObject(messageData);
            if (messageData.contains("\"model\"")) {
                String model = (String) jsonObject.get("model");
                pushMessage.setModel(model);
            }
            if (messageData.contains("\"verb\"")) {
                String verb = (String) jsonObject.get("verb");
                pushMessage.setVerb(verb);
            }
            if (messageData.contains("\"data\"")) {
                JSONObject dataJson = (JSONObject) jsonObject.get("data");
                String data = dataJson.toString();
                pushMessage.setData(data);
            }
            if (messageData.contains("\"from\"")) {
                String from = "";
                if (PushMessage.VERB_HELP.equals(pushMessage.getVerb())) {
                    from = (String) jsonObject.get("from");
                } else {
                    JSONObject dataJson = (JSONObject) jsonObject.get("from");
                    from = dataJson.toString();
                }
                pushMessage.setFrom(from);
            }
            if (messageData.contains("\"to\"")) {
                JSONArray dataJson = (JSONArray) jsonObject.get("to");


                String to = dataJson.toString();
                pushMessage.setTo(to);
            }
            if (messageData.contains("\"message\"")) {
                String message = (String) jsonObject.get("message");
                pushMessage.setMessage(message);
            }
            if (messageData.contains("\"time\"")) {
                String time = (String) jsonObject.get("time");
                pushMessage.setTime(time);
            }
            if (messageData.contains("\"title\"")) {
                String title = (String) jsonObject.get("title");
                pushMessage.setTitle(title);
            }
            if (messageData.contains("\"priority\"")) {
                String priority = (String) jsonObject.get("priority");
                pushMessage.setPriority(priority);
            }
            if (messageData.contains("\"mobile\"")) {
                String mobile = (String) jsonObject.get("mobile");
                pushMessage.setMobile(mobile);
            }
            if (messageData.contains("\"address\"")) {
                String address = (String) jsonObject.get("address");
                pushMessage.setAddress(address);
            }
            if (messageData.contains("\"location\"")) {
                JSONObject object = (JSONObject) jsonObject.get("location");
                pushMessage.setLocation(object.toString());
            }

            return pushMessage;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //从topic里获取到活动的id
    public String getActivityId(String topic) {
        String ret = null;
        String[] split = topic.split("/");
        if (split.length > 2)
            ret = split[split.length - 2];
        return ret;
    }

    /**
     * 活动聊天、加入、踢人、退出信息 处理
     *
     * @param topic
     * @param pushMessage
     */
    public void dealActivity(String topic, PushMessage pushMessage) {
        if (PushMessage.VERB_JOIN.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_CHAR.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_KICK.equals(pushMessage.getVerb()) ||
                PushMessage.VERB_QUIT.equals(pushMessage.getVerb())) {

            if (PushMessage.VERB_KICK.equals(pushMessage.getVerb())) {
                //主持人请出
                //发送强制退出聊天
                if (pushMessage.getTo().contains(User.getUserId())) {
                    EventBus.getDefault().post(new Event.ForcedQuitChat("您被主持人请出了本活动"));
                    //发送刷新我的活动
                    EventBus.getDefault().post(new Event.RefreshActivityData());
                    //取消订阅
                    MQTTManager.getInstance().unSubscribe(topic, null, null);
                }
            } else if (PushMessage.VERB_QUIT.equals(pushMessage.getVerb())) {

                //被动强制退出
                if (TextUtils.isEmpty(pushMessage.getFrom())) {
                    EventBus.getDefault().post(new Event.ForcedQuitChat(pushMessage.getMessage()));
                    //发送刷新我的活动
                    EventBus.getDefault().post(new Event.RefreshActivityData());
                } else {
                    //是主动退出活动，
                    //是主动退出是否自己
                    if (pushMessage.getFrom().contains(User.getUserId())) {
                        //发送刷新我的活动
                        EventBus.getDefault().post(new Event.RefreshActivityData());
                        //取消订阅
                        MQTTManager.getInstance().unSubscribe(topic, null, null);
                    }
                    //其他人添加信息
                }

            }
            EventBus.getDefault().post(new Event.RefreshChatAddOne(pushMessage, pushMessage.getActivityId()));
        }
    }
}
