package lilun.com.pensionlife.module.utils.mqtt;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.app.User;

/**
 * 订阅的主题
 *
 * @author yk
 *         create at 2017/6/7 15:21
 *         email : yk_developer@163.com
 */
public class MqttTopic {
    //新增活动
    public String topic_activity_suffix = "/%23activity/.added";


    //普通求助
    public String topic_help_suffix = "/%23aid/.added";

    //个人私有
    public String personal_msg = "user/" + User.getUserName() + "/#";

    //公告
    public String topic_information_add = "/%23information/.added";

    //公告
    public String topic_information_edit= "/%23information/.updated";


    //登陆
    public String login = "user/" + User.getUserName() + "/.login";


    //紧急求助
    public String urgent_help = "user/" + User.getUserName() + "/.help/10";

    //活动更新
    public String topic_activity_update = "/%23activity/.updated";


    public String[] getAllTopicWhenInit() {
        List<String> topics = new ArrayList<>();
        topics.add(personal_msg);
        ArrayList<String> levelIds = User.levelIds(false);
        if (levelIds == null) {
            return null;
        }
        for (String levelId : levelIds) {
            String aidTopic = levelId + topic_help_suffix;
            String informationTopicAdd = levelId + topic_information_add;
            String informationTopicEdit = levelId + topic_information_edit;
            String activityTopic = levelId + topic_activity_suffix;
            String activityUpdateTopic = levelId + topic_activity_update;
            topics.add(aidTopic);
            topics.add(informationTopicAdd);
            topics.add(informationTopicEdit);
            topics.add(activityTopic);
            topics.add(activityUpdateTopic);
        }

        return topics.toArray(new String[topics.size()]);
    }

    /**
     * topic包含的组织id是否在个人所在组织列表层级中
     */
    public boolean isTopicBelongCurrentOrganizationLevel(String topic) {
        String substring = topic.substring(0, topic.lastIndexOf("/"));
        String topicOrganizationId = substring.substring(0,substring.lastIndexOf("/"));
        ArrayList<String> levelIds = User.levelIds(false);
        for (String levelId : levelIds) {
            if (TextUtils.equals(levelId, topicOrganizationId)) {
                return true;
            }
        }

        return false;
    }
}
