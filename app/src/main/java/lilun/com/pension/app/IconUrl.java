package lilun.com.pension.app;

import java.net.URLEncoder;

/**
 * 各个模块的图标url
 *
 * @author yk
 *         create at 2017/2/15 15:18
 *         email : yk_developer@163.com
 */
public class IconUrl {
    public static String elderModule(String elderModuleId, String iconName) {
        return Config.BASE_URL + "ElderModules/" + encodeURL(elderModuleId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }


    public static String activityCategory(String activityCategoryId, String iconName) {
        return Config.BASE_URL + "OrganizationActivityCategories/" + encodeURL(activityCategoryId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }


    public static String productCategory(String productCategoryId, String iconName) {
        return Config.BASE_URL + "ProductCategories/" + encodeURL(productCategoryId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }

    public static String organizationAid(String organizationAidId) {
        return Config.BASE_URL + "OrganizationAids/" + encodeURL(organizationAidId) + "/downloadPicture?access_token=" + User.getToken();
    }

    public static String organization(String organizationId, String iconName) {
        return Config.BASE_URL + "Organizations/" + encodeURL(organizationId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }
    public static String organizationEdus(String organizationId, String iconName) {
        return Config.BASE_URL + "OrganizationEdus/" + encodeURL(organizationId) + "/picture/" + iconName + "?access_token=" + User.getToken();
    }

    /**
     * url转义
     */
    public static String encodeURL(String target) {
        try {
            String str = new String(target.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception ignored) {

        }
        return "";
    }
}
