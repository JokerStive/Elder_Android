package lilun.com.pensionlife.app;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import lilun.com.pensionlife.module.bean.OrganizationAccount;
import lilun.com.pensionlife.module.utils.ACache;
import lilun.com.pensionlife.module.utils.PreUtils;
import lilun.com.pensionlife.module.utils.StringUtils;

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
    public static final String location = "location";
    public static final String isCustomer = "isCustomer";
    public static final String defaultContactId = "defaultContactId ";
    public static final String rootOrganizationAccountId = "rootOrganizationAccountId";
    public static final String belongOrganizationAccountId = "belongOrganizationAccountId";
    public static final String currentOrganizationAccountId = "currentOrganizationAccountId";
    public static final String defOrganizationId = "";
    public static final String belongsOrganizationId = "belongsOrganizationId";
    public static final String currentOrganizationId = "currentOrganizationId";
    public static final String belongOrganizations = "belongOrganizations";
    public static final String businessId = "businessId";
    public static final String firstHelperPhone = "firstHelperPhone";
    public static final String loginTime = "loginTime";
    public static final String address = "address";
    public static final String certificateLicense = "certificateLicense";
    public static final String prizedraw = "prizedraw"; //兑奖信息


    public static ArrayList<String> getCertificateLicense() {
        return (ArrayList<String>) ACache.get().getAsObject(certificateLicense);
    }

    public static void putCertificateLicense(ArrayList<String> list) {
        ACache.get().put(certificateLicense, list);
    }

    public static void addCertificateLicense(String productId) {
        ArrayList<String> certificateLicense = getCertificateLicense();
        if (certificateLicense == null) {
            certificateLicense = new ArrayList<>();
        }
        certificateLicense.add(productId);
        putCertificateLicense(certificateLicense);
    }


    public static String getLoginTime() {
        return PreUtils.getString(loginTime, null);
    }

    public static void putLoginTime(String time) {
        PreUtils.putString(User.loginTime, time);
    }

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

    public static boolean isMerchant() {
        return PreUtils.getBoolean("merchant", false);
    }


    public static void putisMerchant(boolean isCustomer) {
        PreUtils.putBoolean("merchant", isCustomer);
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
    public static void putLocation(String id) {
        PreUtils.putString(location, id);
    }

    public static String getLocation() {
        return PreUtils.getString(location, "");
    }

    public static void putHelpPhone(String phone) {
        PreUtils.putString(firstHelperPhone, phone);
    }

    public static String getFirstHelperPhone() {
        return PreUtils.getString(firstHelperPhone, "");
    }


    //跑单人员id
    public static void putBusinessId(String id) {
        PreUtils.putString(businessId, id);
    }

    public static String getBusinessId() {
        return PreUtils.getString(businessId, "");
    }

    //跑单人员id
    public static void putAddress(String addr) {
        PreUtils.putString(address, addr);
    }

    public static String getAddress() {
        return PreUtils.getString(address, "");
    }

    public static void putPrizedraw(String prize) {
        PreUtils.putString(prizedraw, prize);
    }

    public static String getPrizedraw() {
        return PreUtils.getString(prizedraw, "");
    }



    /**
     * 当前组织id到市
     */
    public static ArrayList<String> levelIds(boolean isCurrentOrganizationId) {
        String currentOrganizationId = isCurrentOrganizationId ? User.getCurrentOrganizationId() : User.getBelongsOrganizationId();
        return levelIds(currentOrganizationId);
    }

    public static ArrayList<String> levelIds(String curOrgId) {
        int level = 4;
        ArrayList<String> ids = null;
        String[] split = curOrgId.split("/");
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
     * 只有当前组织在所属组织的市级以下，才可以操作
     */
    public static boolean canOperate(String targetId) {
        boolean result = false;
        ArrayList<String> ids = levelIds(false);
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
     * 判断当前状态是否正确，在切换组织界面，杀进程，就会导致状态错乱
     */
    public static boolean isCurrentStatusCorrect() {
        boolean result = true;
        boolean currentOrganizationHadChanged = PreUtils.getBoolean("currentOrganizationHadChanged", false);
        if (!currentOrganizationHadChanged && User.getCurrentOrganizationId().equals(Constants.organization_root)) {
            result = false;
        }
        return result;
    }

    /**
     * 根据层级拼接id
     */
    public static String spliceId(String suffix) {
        String result = "[";
        String currentOrganizationId = isCurrentStatusCorrect() ? getCurrentOrganizationId() : getBelongsOrganizationId();
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



