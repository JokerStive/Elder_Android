package lilun.com.pension.module.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lilun.com.pension.R;
import lilun.com.pension.app.App;
import lilun.com.pension.app.Config;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.IconModule;

/**
 * Created by yk on 2017/1/6.
 * 字符工具类
 */
public class StringUtils {

    /**
     * 转换为东八区时间
     *
     * @param isoTime1
     * @return
     */
    public static String timeFormat(String isoTime1) {
        String[] ss = isoTime1.split("\\.");
        String isoTime = ss[0] + "+08:00";
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dateTime = parser2.parseDateTime(isoTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(dateTime.getMillis() + 28800 * 1000));
    }

    /**
     * 转换为东八区时间
     *
     * @param isoTime1
     * @return
     */
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


    public static String objectToJsonObjectString(Object object) {
        String result = null;
        Object json = null;
        JSONObject jsonObject;
        if (object != null) {
            if (object instanceof String || object instanceof Integer || object instanceof Float || object instanceof Double) {
                result = object.toString();
            } else {
                try {
                    json = new JSONTokener(object.toString()).nextValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json instanceof JSONObject) {
                    jsonObject = (JSONObject) json;
                    result = jsonObject.toString();
                }
            }
        }
//        try {
//            json = new JSONTokener(object.toString()).nextValue();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (json instanceof JSONObject) {
//            jsonObject = (JSONObject) json;
//        } else if (json!=null){
//            result = json.toString();
//        }
//        if (jsonObject != null) {
//            result = jsonObject.toString();
//        }
        return result;
    }


    /**
     * 转换为东八区时间
     *
     * @param isoTime1
     * @return
     */
    public static Date IOS2ToUTCDate(String isoTime1) {
        try {
            String[] ss = isoTime1.split("\\.");
            String isoTime = ss[0] + "+08:00";
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
            DateTime dateTime = parser2.parseDateTime(isoTime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new Date(dateTime.getMillis() + 28800 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换为东八区时间
     *
     * @param isoTime1
     * @param mode     0 - 返回年月日   1- 返回时分  -2 返回所有 -3 返回月/日 时:分  -4 返回月/日
     * @return
     */
    public static String IOS2ToUTC(String isoTime1, int mode) {
        String ret = "";
        if (!TextUtils.isEmpty(isoTime1)) {
            try {
                String[] ss = isoTime1.split("\\.");

                String isoTime = ss[0] + "+08:00";
                DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
                DateTime dateTime = parser2.parseDateTime(isoTime);
                SimpleDateFormat format = null;
                if (mode == 0) {
                    format = new SimpleDateFormat("yyyy.MM.dd");
                } else if (mode == 1) {
                    format = new SimpleDateFormat("HH:mm");
                } else if (mode == 3) {
                    format = new SimpleDateFormat("MM/dd HH:mm");
                } else if (mode == 4) {
                    format = new SimpleDateFormat("MM.dd");
                } else
                    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ret = format.format(new Date(dateTime.getMillis() + 28800 * 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    /***
     * 东八区时间 转换为 0区时间  转mongoDB 时间
     *
     * @param str 2015-03-23 12:12:12
     * @return
     */
    public static String localToGTM(String str) {
        String ret = "";

        SimpleDateFormat format;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date result_date;
        long result_time = 0;

        try {
            format.setTimeZone(TimeZone.getDefault());
            result_date = format.parse(str);
            result_time = result_date.getTime();
            format.setTimeZone(TimeZone.getTimeZone("GMT08:00"));
            ret = format.format(result_time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 将 0区时间 转为东八区时间 本地时间，
     *
     * @param isoTime1 0区时间
     * @return 返回 Date
     */
    public static Date IOS2DateTime(String isoTime1) {
        Date dateTime = null;
        try {
            String localTime = IOS2ToUTC(isoTime1, 5);

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateTime = parser.parse(localTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static Date string2Date(String strTime) {
        Date dateTime = null;
        try {

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateTime = parser.parse(strTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
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
        if (TextUtils.isEmpty(filter) || filter.equals("{}")) {
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
     * 获取列表数据时候，一定要添加的头部、0filter信息
     */
    public static String addFilterWithDefVisible(String filter, int skip) {
        String head;
        if (TextUtils.isEmpty(filter) || filter.equals("{}")) {
            head = "{\"limit\":\"" + Config.defLoadDatCount + "\",\"skip\":\"" + skip + "\",\"visible\":\"0\"}";
            filter = head;
        } else {
            head = ",\"limit\":\"" + Config.defLoadDatCount + "\",\"skip\":\"" + skip + "\",\"visible\":\"0\"}";
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
        if (iconModules != null && iconModules.size() != 0) {
            return iconModules.get(0).getFileName();
        } else {
            return null;
        }
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
        String substring = "";
        if (!TextUtils.isEmpty(id)) {
            substring = id.substring(0, id.lastIndexOf("/"));
            if (TextUtils.isEmpty(substring)) {
                substring = User.defOrganizationId;
            }
        }
        return substring;
    }


    /**
     * 从字符串中取数字
     */
    public static String get_StringNum(String str) {
        String result = "";
        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    result += str.charAt(i);
                }
            }
        }
        return result;
    }

    /**
     * 验证手机号码+座机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNo(String mobiles) {
        Pattern p = Pattern.compile("^(010\\d{8})|(0[2-9]\\d{9})|(13\\d{9})|(14[57]\\d{8})|(15[0-35-9]\\d{8})|(17[0-35-9]\\d{8})|(18[0-35-9]\\d{8})");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^(13\\d{9})|(14[57]\\d{8})|(15[0-35-9]\\d{8})|(17[0-35-9]\\d{8})|(17[0-35-9]\\d{8})|(18[0-35-9]\\d{8})");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    /**
     * 根据订单状态得到现实的值
     */
    public static String getOrderStatusValue(String status) {
        String[] orderStatues = App.context.getResources().getStringArray(R.array.merchant_order_condition_value);
        String[] orderStatusValue = App.context.getResources().getStringArray(R.array.merchant_order_condition);
        for (int i = 0; i < orderStatues.length; i++) {
            if (status.equals(orderStatues[i])) {
                return orderStatusValue[i];
            }
        }
        return null;
    }

    /**
     * 计算发布时间
     *
     * @param strTime
     * @return
     */
    public static String up2thisTime(String strTime) {
        Date time = IOS2ToUTCDate(strTime);
        if (time == null) return "";
        long ms = new Date().getTime() - time.getTime();
        long days, hours, mins;
        days = ms / (1000 * 60 * 60 * 24);
        hours = ms / (1000 * 60 * 60);
        mins = ms / (1000 * 60);
        if (days != 0) return days + "天前";
        if (hours != 0) return hours + "小时前";
        if (mins >= 5) return mins + "分钟前";
        return "刚刚";
    }

    /**
     * 计算时间
     *
     * @param strTime
     * @return
     */
    public static String thatTime(String strTime) {
        Date time = string2Date(strTime);
        if (time == null) return "";
        long ms = new Date().getTime() - time.getTime();
        long days, hours, mins;
        days = ms / (1000 * 60 * 60 * 24);
        hours = ms / (1000 * 60 * 60);
        mins = ms / (1000 * 60);
        if (days == 0) return new SimpleDateFormat("HH:mm").format(string2Date(strTime));
        if (days == 1) return "昨天" + new SimpleDateFormat("HH:mm").format(string2Date(strTime));

        return new SimpleDateFormat("MM-dd HH:mm").format(string2Date(strTime));
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

    /**
     * 对组织id进行转义
     *
     * @return
     */
    public static String encodeURLOfOrg(String orgId) {

        return encodeURL(orgId).replace("%2F", "/");
    }

    /**
     * 判断是否为用户注册小区及其上级小区
     * 注册地区必须是有到社区或小区，否则返回false
     * @return
     */
    public static boolean isResisterTopCommunity(String current, String register) {
        String[] district = register.split("/");
        String[] curDistrict = current.split("/");
        if (district.length < 2) return false;
        if (curDistrict.length == 0) return false;
        if (district.length == 9) {
            if (curDistrict[curDistrict.length - 1].equals(district[district.length - 1])
                    || curDistrict[curDistrict.length - 1].equals(district[district.length - 2]))
            return true;
        } else if (district.length == 8) {
            if (curDistrict[curDistrict.length - 1].equals( district[district.length - 1]))
                return true;
        }
        return false;
    }
}
