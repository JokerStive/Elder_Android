package lilun.com.pension.module.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import java.util.List;

import lilun.com.pension.app.Config;
import lilun.com.pension.app.User;
import lilun.com.pension.module.bean.IconModule;

/**
 * Created by yk on 2017/1/6.
 * 字符工具类
 */
public class StringUtils {
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

    /**
     * 获取列表数据时候，一定要添加的头部、0filter信息
     */
    public static String addFilterWithDef(String filter, int skip) {
        String head;
        if (TextUtils.isEmpty(filter)) {
            head = "{\"limit\":\"" + Config.DEF_LOAD_DATA_COUNT + "\",\"skip\":\"" + skip + "\"}";
            filter = head;
        } else {
            head = ",\"limit\":\"" + Config.DEF_LOAD_DATA_COUNT + "\",\"skip\":\"" + skip + "\"}";
            filter = filter.substring(0, filter.lastIndexOf("}")) + head;
        }
        Logger.d("拼接够的filter--" + filter);
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

    public static List<IconModule> string2IconModule(String icon) {
        if (!TextUtils.isEmpty(icon)) {
            Gson gson = new Gson();
            return gson.fromJson(icon, new TypeToken<List<IconModule>>() {
            }.getType());
        }

        return null;
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


}
