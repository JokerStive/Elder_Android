package lilun.com.pension.module.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lilun.com.pension.app.Config;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.IconModule;

/**
 * Created by yk on 2017/1/6.
 * 字符工具类
 */
public class StringUtils {

    public static String timeFormat(String isoTime1) {
        String[] ss = isoTime1.split("\\.");
        String isoTime = ss[0] + "+08:00";
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dateTime = parser2.parseDateTime(isoTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(dateTime.getMillis() + 28800 * 1000));
    }

    public static String IOS2ToUTC(String isoTime1) {
        String ret = "Error";
        try {
            String[] ss = isoTime1.split("\\.");
            String isoTime = ss[0] + "+08:00";
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
            DateTime dateTime = parser2.parseDateTime(isoTime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ret = format.format(new Date(dateTime.getMillis() + 28800 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * @param isoTime1
     * @param mode     0 - 返回年月日   1- 返回时分  -2 返回所有 -3 返回月/日 时:分  -4 返回月/日
     * @return
     */
    public static String IOS2ToUTC(String isoTime1, int mode) {
        String ret = "Error";
        try {
            String[] ss = isoTime1.split("\\.");
            String isoTime = ss[0] + "+08:00";
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
            DateTime dateTime = parser2.parseDateTime(isoTime);
            SimpleDateFormat format = null;
            if (mode == 0) {
                format = new SimpleDateFormat("yyyy.MM.dd");
            }
            else if (mode == 1) {
                format = new SimpleDateFormat("HH:mm");
            } else if (mode == 3){
                format = new SimpleDateFormat("MM/dd HH:mm");
            }else if (mode == 4){
                format = new SimpleDateFormat("MM.dd");
            }

            else
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ret = format.format(new Date(dateTime.getMillis() + 28800 * 1000));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static DateTime IOS2DateTime(String isoTime1) {
        DateTime dateTime = null;
        try {
            String[] ss = isoTime1.split("\\.");
            String isoTime = ss[0] + "+08:00";
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
            dateTime = parser2.parseDateTime(isoTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }


    public static String checkNotEmpty(String s) {
        if (TextUtils.isEmpty(s)) {
            throw new NullPointerException();
        } else {
            return s;
        }
    }

    public static boolean checkNotEmptyWithMessage(String s, String message) {
        if (TextUtils.isEmpty(s)) {
            ToastHelper.get().showWareShort(message);
            return false;
        } else {
            return true;
        }
    }

    public static String filterNull(String s) {
        if (!TextUtils.isEmpty(s)) {
            return s;
        } else {
            return "无";
        }
    }

    public static List<IconModule> string2IconModule(String imgString) {
        if (imgString != null) {
            Gson gson = new Gson();
            return gson.fromJson(imgString, new TypeToken<List<IconModule>>() {
            }.getType());
        }

        return null;
    }

    /**
     * 获取列表数据时候，一定要添加的头部、0filter信息
     */
    public static String addFilterWithDef(String filter, int skip) {
        String head;
        if (TextUtils.isEmpty(filter)) {
            head = "{\"limit\":\"" + Config.defLoadDatCount + "\",\"skip\":\"" + skip + "\"}";
            filter = head;
        } else {
            head = ",\"limit\":\"" + Config.defLoadDatCount + "\",\"skip\":\"" + skip + "\"}";
            filter = filter.substring(0, filter.lastIndexOf("}")) + head;
        }
//        Logger.d("拼接够的filter--" + filter);
        return filter;
    }


    /**
     * 根据组织id获取名字
     */
    public static String getOrganizationNameFromId(String organizationId) {
        String substring = organizationId.substring(organizationId.lastIndexOf("/") + 1);
        if (TextUtils.isEmpty(substring)) {
            substring = User.defOrganizationId;
        }
        return substring;
    }


    /**
     * 从module的icon字段获取第一张icon的名称
     */
    public static String getFirstIconNameFromIcon(List<IconModule> iconModules) {
        return iconModules.get(0).getFileName();
    }


    /**
     * 根据list数据设置span数
     */
    public static int spanCountByData(List data) {
        int count;
        if (data.size() == 0) {
            count = 1;
        } else {
            count = data.size() >= 3 ? 3 : data.size();
        }
        return count;
    }


    /**
     * 过滤特殊字符后缀  /#staff ,/#department....
     */
    public static String removeSpecialSuffix(String id) {
        String substring = id.substring(0, id.lastIndexOf("/"));
        if (TextUtils.isEmpty(substring)) {
            substring = User.defOrganizationId;
        }
        return substring;
    }


    /**
     * 从字符串中取数字
     */
    public static String get_StringNum(String str) {
        String result = "";
        if (!TextUtils.isEmpty(str)){
            for(int i=0;i<str.length();i++){
                if(str.charAt(i)>=48 && str.charAt(i)<=57){
                    result+=str.charAt(i);
        }}}
        return result;
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("^(010\\d{8})|(0[2-9]\\d{9})|(13\\d{9})|(14[57]\\d{8})|(15[0-35-9]\\d{8})|(18[0-35-9]\\d{8})");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

}
