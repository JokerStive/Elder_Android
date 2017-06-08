package lilun.com.pension.module.utils.mqtt;

import lilun.com.pension.app.User;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 订阅的主题
 *
 * @author yk
 *         create at 2017/6/7 15:21
 *         email : yk_developer@163.com
 */
public class MqttTopic {

    //普通求助
    public String normal_help = "OrganizationAid/.added";

    //紧急求助
    public String urgent_help = StringUtils.encodeURL(User.getBelongToDistrict() + "/#aid/.help").replace("%2F", "/");

    //公告
    public String normal_announce = "OrganizationInformation/.added";


    //登陆
    public String login = "user/" + User.getUserName() + "/.login";


}
