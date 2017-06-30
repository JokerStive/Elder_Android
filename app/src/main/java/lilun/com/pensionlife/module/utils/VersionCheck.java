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

        if (appVersionArray.length > 0 && retVersionArray.length > 0) {
            if (Integer.valueOf(retVersionArray[0]) > Integer.valueOf(appVersionArray[0]))
                return true;
        }
        if (appVersionArray.length > 1 && retVersionArray.length > 1)
            if (Integer.valueOf(retVersionArray[1]) > Integer.valueOf(appVersionArray[1]))
                return true;
        if (appVersionArray.length > 2 && retVersionArray.length > 2)
            if (Integer.valueOf(retVersionArray[2]) > Integer.valueOf(appVersionArray[2]))
                return true;
        if (appVersionArray.length > 3 && retVersionArray.length > 3)
            if (Integer.valueOf(retVersionArray[3]) > Integer.valueOf(appVersionArray[3]))
                return true;

        return checkResult;
    }
}
