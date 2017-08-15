package lilun.com.pensionlife.module.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zp on 2017/8/15.
 */

public class ActUitls {


    /**
     * * 判断是否为用户当前小区及其上到市级  所发布的活动
     *
     * @param curCmmty   当前小区
     * @param categoryId 活动类别
     * @return
     */
    public static boolean isParentTopActivity(String curCmmty, String categoryId) {
        if (categoryId == null || categoryId.trim() == "") return false;

        String[] ctgSplit = categoryId.split("/");
        if (ctgSplit.length <= 4) return false;



        if (curCmmty.contains(categoryId)) return true;

        return false;
    }

}
