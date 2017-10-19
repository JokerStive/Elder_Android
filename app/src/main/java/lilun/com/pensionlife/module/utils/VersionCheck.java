package lilun.com.pensionlife.module.utils;

import java.util.regex.Pattern;

/**
 * Created by zp on 2017/6/28.
 */

public class VersionCheck {
    public static boolean compareVersion(String appVersion, String retVersion) {
        boolean checkResult = false;
        if (!Pattern.matches("^[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}", appVersion)) return false;
        if (!Pattern.matches("^[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}", retVersion)) return false;
        String[] appVersionArray = appVersion.split("\\.");
        String[] retVersionArray = retVersion.split("\\.");
        if (appVersionArray.length != retVersionArray.length) return false;

        for (int i = 0; i < appVersionArray.length; i++) {
            if (Integer.valueOf(retVersionArray[i]) > Integer.valueOf(appVersionArray[i]))
                return true;
            else if(Integer.valueOf(retVersionArray[i]) < Integer.valueOf(appVersionArray[i]))
                return false;
        }
        return checkResult;
    }
}
