package lilun.com.pension.app;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

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

    public static String organizationAid(String organizationAidId, String iconName) {
        Logger.d("加载aid图片地址 = " + Config.BASE_URL + "OrganizationAids/" + encodeURL(organizationAidId) + "/downloadDefaultPicture?pictureName=" + iconName + "&access_token=" + User.getToken());
        if (TextUtils.isEmpty(iconName)){
            return Config.BASE_URL + "OrganizationAids/" + encodeURL(organizationAidId) + "/downloadDefaultPicture?access_token=" + User.getToken();
        }else {
            return Config.BASE_URL + "OrganizationAids/" + encodeURL(organizationAidId) + "/downloadDefaultPicture?pictureName=" + iconName + "&access_token=" + User.getToken();

        }
    }

    public static String organizationActivies(String activityId, String iconName) {
        return Config.BASE_URL + "OrganizationActivities/" + encodeURL(activityId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }


    public static String organizationProduct(String productId, String iconName) {
        Logger.d("加载aid图片地址 = " + Config.BASE_URL + "OrganizationProducts/" + encodeURL(productId) + "/downloadDefaultimages/?pictureName=" + iconName + "&access_token=" + User.getToken());
        return Config.BASE_URL + "OrganizationProducts/" + encodeURL(productId) + "/downloadDefaultimages/?pictureName=" + iconName + "&access_token=" + User.getToken();
    }


    public static String organization(String organizationId, String iconName) {
        return Config.BASE_URL + "Organizations/" + encodeURL(organizationId) + "/icon/" + iconName + "?access_token=" + User.getToken();
    }

    public static String account(String accountId, String iconName) {
        Logger.d("加载account图片地址 = "+Config.BASE_URL + "Accounts/" + encodeURL(accountId) + "/picture/" + iconName + "?access_token=" + User.getToken());

        return Config.BASE_URL + "Accounts/" + encodeURL(accountId) + "/picture/" + iconName + "?access_token=" + User.getToken();
    }

    public static String information(String informationId, String iconName) {
        return Config.BASE_URL + "OrganizationInformations/" + encodeURL(informationId) + "/picture/" + iconName + "?access_token=" + User.getToken();
    }


    public static String organizationEdus(String organizationId, String iconName) {
        return Config.BASE_URL + "OrganizationEdus/" + encodeURL(organizationId) + "/picture/" + iconName + "?access_token=" + User.getToken();
    }

    public static String eduCourses(String organizationId, String iconName) {
        return Config.BASE_URL + "EduCourses/" + encodeURL(organizationId) + "/picture/" + iconName + "?access_token=" + User.getToken();
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
