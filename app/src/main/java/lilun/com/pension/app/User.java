package lilun.com.pension.app;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pension.module.bean.OrganizationAccount;
import lilun.com.pension.module.utils.ACache;
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
    //    public static final String tokenEffectiveDuration = "tokenEffectiveDuration";
    public static final String userId = "userId";
    public static final String userAvatar = "userAvatar";
    public static final String name = "name";
    public static final String username = "username";
    public static final String password = "password";
    public static final String mobile = "mobile";
    //    public static final String has = "mobile";
    public static final String belongToDistrict = "belongToDistrict";
    public static final String isCustomer = "isCustomer";
    public static final String defaultContactId = "defaultContactId ";
    public static final String rootOrganizationAccountId = "rootOrganizationAccountId";
    public static final String belongOrganizationAccountId = "belongOrganizationAccountId";
    public static final String currentOrganizationAccountId = "currentOrganizationAccountId";
    public static final String defOrganizationId = "/";
    public static final String belongsOrganizationId = "belongsOrganizationId";
    public static final String currentOrganizationId = "currentOrganizationId";
    public static final String belongOrganizations = "belongOrganizations";

    public static String getUserAvatar() {
        return PreUtils.getString(userAvatar, null);
    }

    public static void puttUserAvatar(String avatar) {
        PreUtils.putString(User.userAvatar, avatar);
    }

    public static String getUserId() {
        return PreUtils.getString(userId, "");
    }

    public static void putUserId(String userId) {
        PreUtils.putString(User.userId, userId);
    }


    public static String getUserName() {
        return PreUtils.getString(username, "");
    }

    public static void putUserName(String un) {
        PreUtils.putString(username, un);
    }


    public static String getPassword() {
        return PreUtils.getString(password, "");
    }

    public static void putPassword(String pass) {
        PreUtils.putString(password, pass);
    }


    public static String getToken() {
        return PreUtils.getString(token, "");
    }


    public static String getName() {
        return PreUtils.getString(name, "");
    }

    public static void putName(String nam) {
        PreUtils.putString(name, nam);
    }


    public static void putIsCustomer(boolean isCustomer) {
        PreUtils.putBoolean(User.isCustomer, isCustomer);
    }

    public static boolean isCustomer() {
        return PreUtils.getBoolean(User.isCustomer, true);
    }


    public static String getCurrentOrganizationId() {
        return PreUtils.getString(currentOrganizationId, defOrganizationId);
    }

    public static void putCurrentOrganizationId(String id) {
        PreUtils.putString(currentOrganizationId, TextUtils.isEmpty(id) ? User.defOrganizationId : id);
    }


    public static String getBelongsOrganizationId() {
        return PreUtils.getString(belongsOrganizationId, defOrganizationId);
    }

    public static void putBelongsOrganizationId(String id) {
        PreUtils.putString(belongsOrganizationId, TextUtils.isEmpty(id) ? User.defOrganizationId : id);
    }


    public static String getCurrentOrganizationName() {
        return StringUtils.getOrganizationNameFromId(PreUtils.getString(currentOrganizationId, defOrganizationId));
    }


    public static String getBelongsOrganizationName() {
        return StringUtils.getOrganizationNameFromId(PreUtils.getString(belongsOrganizationId, defOrganizationId));
    }


    /**
     *检查token是否过期
     */
//    public static boolean isTokenEffective() {
//        long currentTime = new Date().getTime();
//        long tokenEffectiveDuration = PreUtils.getLong(User.tokenEffectiveDuration, 0);
//        if (tokenEffectiveDuration==0){
//            throw new RuntimeException("数据异常");
//        }
//        return getUserId().equals(creatorId);
//    }


    /**
     * 根据当前组织判断是否可以增删改
     */
    public static boolean canOperate() {
        String currentOrganizationId = getCurrentOrganizationId();
        if (ACache.get().isExit(belongOrganizations)) {
            List<OrganizationAccount> belongOrganizationAccount = (List<OrganizationAccount>) ACache.get().getAsObject(belongOrganizations);
            for (OrganizationAccount organizationAccount : belongOrganizationAccount) {
                String organizationId = StringUtils.removeSpecialSuffix(organizationAccount.getOrganizationId());
                if (organizationId.equals(Constants.organization_root)) {
                    continue;
                }
                if (TextUtils.equals(currentOrganizationId, organizationId)) {
                    return true;
                }
            }

        }
        return false;
    }


    /**
     * 创建者是否是自己
     */
    public static boolean creatorIsOwn(String creatorId) {
        return getUserId().equals(creatorId);
    }


    /**
     * 获取当前组织是否已经切换
     */
    public static boolean currentOrganizationHasChanged() {
        return PreUtils.getBoolean("currentOrganizationHadChanged", false);
    }

    /**
     * 存当前组织是否已经切换
     */
    public static boolean putCurrentOrganizationHasChanged(boolean changed) {
        return PreUtils.putBoolean("currentOrganizationHadChanged", changed);
    }


    /**
     * 获取用户的组织账号
     */
    public static List<OrganizationAccount> getBelongOrganization() {
        if (ACache.get().getAsObject(belongOrganizations) != null) {
            return (List<OrganizationAccount>) ACache.get().getAsObject(belongOrganizations);
        }
        return null;
    }


    //默认资料
    public static void putContactId(String defaultContactId) {
        PreUtils.putString(defaultContactId, defaultContactId);
    }

    public static String getContactId() {
        return PreUtils.getString(defaultContactId, "");
    }


    //地球村OrganizationAccount  的id
    public static void putRootOrganizationAccountId(String rootId) {
        PreUtils.putString(rootOrganizationAccountId, rootId);
    }

    public static String getRootOrganizationAccountId() {
        return PreUtils.getString(rootOrganizationAccountId, "");
    }


    //默认所属组织OrganizationAccount  的id
    public static void putBelongOrganizationAccountId(String rootId) {
        PreUtils.putString(belongOrganizationAccountId, rootId);
    }

    public static String getBelongOrganizationAccountId() {
        return PreUtils.getString(belongOrganizationAccountId, "");
    }


    //当前所属组织OrganizationAccount  的id
    public static void putCurrentOrganizationAccountId(String rootId) {
        PreUtils.putString(currentOrganizationAccountId, rootId);
    }

    public static String getCurrentOrganizationAccountId() {
        return PreUtils.getString(currentOrganizationAccountId, "");
    }


    //电话
    public static void putMobile(String phone) {
        PreUtils.putString(mobile, phone);
    }

    public static String getMobile() {
        return PreUtils.getString(mobile, "");
    }


    //默认小区
    public static void putBelongToDistrict(String id) {
        PreUtils.putString(belongToDistrict, id);
    }

    public static String getBelongToDistrict() {
        return PreUtils.getString(belongToDistrict, "");
    }


    /**
     * 当前组织id到市
     */
    public static ArrayList<String> levelIds() {
        int level = 4;
        ArrayList<String> ids = null;
        String currentOrganizationId = User.getCurrentOrganizationId();
        String[] split = currentOrganizationId.split("/");
        if (split.length >= level) {
            ids = new ArrayList<>();
            for (int i = level; i < split.length; i++) {
                String id = "";
                for (int j = 1; j <= i; j++) {
                    id += "/" + split[j];
                }
                ids.add(id);
            }
        }

        return ids;
    }


    /**
    *只有当前组织在所属组织的市级以下，才可以操作
    */
    public static boolean canOperate(String targetId) {
        boolean result = false;
        ArrayList<String> ids = levelIds();
        if (ids != null) {
            for (String id : ids) {
                if (TextUtils.equals(id, targetId)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
    *根据层级拼接id
    */
    public static  String spliceId(String suffix) {
        String result = "[";
        String currentOrganizationId = User.getCurrentOrganizationId();
        String[] split = currentOrganizationId.split("/");
        if (split.length < 4) return "";
        for (int i = 4; i < split.length; i++) {
            String parentId = "";
            for (int j = 1; j <= i; j++) {
                parentId += "/" + split[j];
            }
            if (i == 4) {
                result += "\"" + parentId + suffix + "\"";
            } else {
                result += "," + "\"" + parentId + suffix + "\"";
            }
        }
        result += "]";
        return result;
    }
}



