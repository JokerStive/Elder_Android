package lilun.com.pensionlife.app;

import java.net.URLEncoder;

/**
 * 各个模块的图标url
 *
 * @author yk
 *         create at 2017/2/15 15:18
 *         email : yk_developer@163.com
 */
public class IconUrl {

    public static String ElderModules = "ElderModules";
    public static String OrganizationActivityCategories = "OrganizationActivityCategories";
    public static String OrganizationProductCategories = "OrganizationProductCategories";
    public static String OrganizationAids = "OrganizationAids";
    public static String OrganizationActivities = "OrganizationActivities";
    public static String OrganizationProducts = "OrganizationProducts";
    public static String Organizations = "Organizations";
    public static String OrganizationEdus = "OrganizationEdus";
    public static String Accounts = "Accounts";
    public static String OrganizationInformations = "OrganizationInformations";

    public static String moduleIconUrl(String module, String moduleId, String iconName) {
        return ConfigUri.BASE_URL + module + "/" + encodeURL(moduleId) + "/download/image/" + iconName + "?access_token=" + User.getToken();
    }

    public static String moduleIconUrlOfActivity(String module, String moduleId, String iconName) {
        return ConfigUri.BASE_URL + module + "/" + encodeURL(moduleId) + "/download/icon/" + iconName + "?access_token=" + User.getToken();
    }

    public static String moduleIconUrl(String module, String moduleId, String iconName, String Icon) {
        return ConfigUri.BASE_URL + module + "/" + encodeURL(moduleId) + "/download/icon/" + iconName + "?access_token=" + User.getToken();
    }

    public static String account(String accountId) {
        return ConfigUri.BASE_URL + Accounts + "/" + encodeURL(accountId) + "/download/image?access_token=" + User.getToken();
    }

    public static String organizationEdus(String organizationId, String iconName) {
        return ConfigUri.BASE_URL + "OrganizationEdus/" + encodeURL(organizationId) + "/download/image/" + iconName + "?access_token=" + User.getToken();
    }

    public static String eduCourses(String organizationId, String iconName) {
        return ConfigUri.BASE_URL + "EduCourses/" + encodeURL(organizationId) + "/picture/" + iconName + "?access_token=" + User.getToken();
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
