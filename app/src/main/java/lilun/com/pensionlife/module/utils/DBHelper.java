package lilun.com.pensionlife.module.utils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import lilun.com.pensionlife.app.User;
import lilun.com.pensionlife.module.bean.OrganizationActivityDS;

/**
 * Created by zp on 2017/8/15.
 */

public class DBHelper {

    /**
     * MQTT消息已读
     * 删除活动类别数据
     */
    public static void deleterCategory(String id) {
        String substring = id.substring(id.indexOf("/#activity-category"));
        String orgId = id.replace(substring, "");
        ArrayList<String> orgIds = User.levelIds(orgId);
        for (int i = 0; i < orgIds.size(); i++) {
            DataSupport.deleteAll(OrganizationActivityDS.class, "categoryId = ?", orgIds.get(i) + substring);
        }

    }
}
