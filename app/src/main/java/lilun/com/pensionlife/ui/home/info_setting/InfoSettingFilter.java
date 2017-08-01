package lilun.com.pensionlife.ui.home.info_setting;

import lilun.com.pensionlife.app.App;
import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.utils.ACache;

/**
 * @author yk
 *         create at 2017/8/1 14:16
 *         email : yk_developer@163.com
 */
public class InfoSettingFilter {
    public static String announce = "公告";
    public static String help = "一键求助";
    public static String activity = "活动";
    public static String cards = "打牌";
    public static String dance = "跳舞";
    public static String walk = "健步";
    public static String ball = "打球";
    public static String tourism = "旅游";
    public static String other = "其他";


    public static boolean isInfoFilter(String filter) {
        String userId = User.getUserId();
        return ACache.get(App.context, "info_setting_filter").isExit(userId + filter);
    }


    public static void initInfoFilter() {
        String userId = User.getUserId();
        ACache infoFilterCache = ACache.get(App.context, "info_setting_filter");
        infoFilterCache.put(userId + announce, announce);
        infoFilterCache.put(userId + help, help);
        infoFilterCache.put(userId + activity, activity);
        infoFilterCache.put(userId + cards, cards);
        infoFilterCache.put(userId + dance, dance);
        infoFilterCache.put(userId + walk, walk);
        infoFilterCache.put(userId + ball, ball);
        infoFilterCache.put(userId + tourism, tourism);
        infoFilterCache.put(userId + other, other);
    }


    public static void addInfoFilter(String filter) {
        String userId = User.getUserId();
        ACache.get(App.context, "info_setting_filter").put(userId + filter, filter);
    }


    public static void removeInfoFilter(String filter) {
        String userId = User.getUserId();
        ACache.get(App.context, "info_setting_filter").remove(userId + filter);
    }
}
