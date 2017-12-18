package lilun.com.pensionlife.module.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式工具
 * Created by zp on 2017/12/18.
 */

public class DateTimeUtils {
    /**
     * 今天 19:30
     * 明天 18:20
     * 今年显示月日时分
     * 其他显示年月日时分
     */
    public static String dateToSmartTime(String self) {
        String smartTime = "";
        Date date = StringUtils.IOS2ToUTCDate(self);
        Date now = new Date();
        if (date == null) return smartTime;

        if (now.getYear() != date.getYear()) {
            //其他显示年月日时分
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            smartTime = simpleDateFormat.format(date);
        } else {
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(now);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //今天
            if (nowCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("今天 HH:mm");
                smartTime = simpleDateFormat.format(calendar.getTime());
            } else if (nowCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("昨天 HH:mm");
                smartTime = simpleDateFormat.format(calendar.getTime());
            } else if (nowCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == -1) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("明天 HH:mm");
                smartTime = simpleDateFormat.format(calendar.getTime());
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                smartTime = simpleDateFormat.format(calendar.getTime());
            }
        }

        return smartTime;
    }
}
