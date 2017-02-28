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
        return  format.format(new Date(dateTime.getMillis()+28800*1000));
    }

    public static String IOS2ToUTC(String isoTime1) {
        String[] ss = isoTime1.split("\\.");
        String isoTime = ss[0] + "+08:00";
        DateTimeFormatter parser2 = ISODateTimeFormat.dateTimeNoMillis();
        DateTime dateTime = parser2.parseDateTime(isoTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  format.format(new Date(dateTime.getMillis()+28800*1000));
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
            return "";
        }
    }

    public static List<IconModule> string2IconModule(String imgString) {
        if (imgString!=null){
            Gson gson = new Gson();
            return gson.fromJson(imgString, new TypeToken<List<IconModule>>() {}.getType());
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



}
