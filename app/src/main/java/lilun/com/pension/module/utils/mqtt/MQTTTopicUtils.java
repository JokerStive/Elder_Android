package lilun.com.pension.module.utils.mqtt;

import lilun.com.pension.module.utils.StringUtils;

/**
 * Created by zp on 2017/5/4.
 */

public class MQTTTopicUtils {
    public static String ACTIVITY = "#activity";

    /**
     * 活动topic /orgId/#activity/actId/.chat
     * @param orgId
     * @param activityId
     * @return
     */
    public static String getActivityTopic(String orgId, String activityId) {
          return StringUtils.encodeURL(orgId + "/"  + activityId + "/.chat").replace("%2F", "/");
        //ok return StringUtils.encodeURL("/" + orgId + "/" + activityId + "/.chat").replace("%2F", "/");

    }






}
