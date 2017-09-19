package lilun.com.pensionlife.ui.home.info_setting;

import android.text.TextUtils;

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
    public static String help = "紧急求助";
    public static String activity = "活动";
    public static String cards = "打牌";
    public static String dance = "唱歌跳舞";
    public static String walk = "健步";
    public static String ball = "打球";
    public static String tourism = "旅游";
    public static String other = "其他";
    public static String cache_key = "info_setting_filter";


    public static boolean isInfoFilter(String filter) {
        String asString = ACache.get(App.context, cache_key).getAsString(composeKeyWithFilter(filter));
        return !TextUtils.isEmpty(asString);
    }


    public static void initInfoFilter() {
        ACache infoFilterCache = ACache.get(App.context, cache_key);
        infoFilterCache.put(composeKeyWithFilter(announce), announce);
        infoFilterCache.put(composeKeyWithFilter(help), help);
        infoFilterCache.put(composeKeyWithFilter(activity), activity);
        infoFilterCache.put(composeKeyWithFilter(cards), cards);
        infoFilterCache.put(composeKeyWithFilter(dance), dance);
        infoFilterCache.put(composeKeyWithFilter(walk), walk);
        infoFilterCache.put(composeKeyWithFilter(ball), ball);
        infoFilterCache.put(composeKeyWithFilter(tourism), tourism);
        infoFilterCache.put(composeKeyWithFilter(other), other);
    }


    public static void addInfoFilter(String filter) {
        ACache.get(App.context, cache_key).put(composeKeyWithFilter(filter), filter);
    }


    public static void removeInfoFilter(String filter) {
        ACache.get(App.context, cache_key).remove(composeKeyWithFilter(filter));
    }

    public static String composeKeyWithFilter(String filter) {
        String userId = User.getUserId();
        return userId + "_" + filter;
    }
}
