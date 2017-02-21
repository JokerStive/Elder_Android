package lilun.com.pension.app;

import android.text.TextUtils;

import lilun.com.pension.module.utils.PreUtils;
import lilun.com.pension.module.utils.StringUtils;

/**
 * 关于用户的常用操作
 *
 * @author yk
 *         create at 2017/2/13 13:32
 *         email : yk_developer@163.com
 */
public class User {
    public static final String token = "token";
    public static final String userId = "userId";
    public static final String defOrganizationId = "/";
    public static final String belongsOrganizationId = "belongsOrganizationId";
    public static final String currentOrganizationId = "currentOrganizationId";

    public static String getUserId() {
        return PreUtils.getString(userId, "");

    }
    public static String getToken() {
        return PreUtils.getString(token, "");
    }



    public static void putUserId(String userId) {
         PreUtils.putString(User.userId, userId);
    }

    public static String getCurrentOrganizationId() {
       return PreUtils.getString(currentOrganizationId, defOrganizationId);
    }

    public static void puttCurrentOrganizationId(String currentOrganizationId) {
        PreUtils.putString(currentOrganizationId, TextUtils.isEmpty(currentOrganizationId)?User.defOrganizationId:currentOrganizationId);
    }

    public static String getBelongsOrganizationId() {
       return PreUtils.getString(belongsOrganizationId, defOrganizationId);
    }

    public static void putBelongsOrganizationId(String belongsOrganizationId) {
        PreUtils.putString(belongsOrganizationId,  TextUtils.isEmpty(belongsOrganizationId)?User.defOrganizationId:belongsOrganizationId);
    }


    public static String getCurrentOrganizationName() {
       return StringUtils.getOrganizationNameFromId(PreUtils.getString(currentOrganizationId, defOrganizationId));
    }


    public static String getBelongsOrganizationName() {
       return StringUtils.getOrganizationNameFromId(PreUtils.getString(belongsOrganizationId, defOrganizationId));
    }


    public static boolean creatorIsOwn(String creatorId){
        return getUserId().equals(creatorId);
    }

}



