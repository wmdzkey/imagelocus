package m.z.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

    /**
     * 显示为自然时间
     * @param date
     * @return
     */
    public static String showNaturalTime(Date date) {
        String timeDesc = null;
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
        Calendar nowCal = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 0);// 当前时间天数减一增加为加+
        calendar.add(Calendar.MINUTE, -1);// 当前时间分钟减一增加为加+
        calendar.getTime();// 获取时间
        int day = nowCal.get(Calendar.DAY_OF_YEAR)
                - calendar.get(Calendar.DAY_OF_YEAR);
        int hour = nowCal.get(Calendar.HOUR_OF_DAY)
                - calendar.get(Calendar.HOUR_OF_DAY);
        int minute = nowCal.get(Calendar.MINUTE)
                - calendar.get(Calendar.MINUTE);
        if (day == 0) {
            timeDesc = (hour == 0 ? (minute == 0 ? "刚刚" : minute + "分钟前") : "今天 " + dfs.format(calendar.getTime()));
        } else if (day == 1) {
            timeDesc = "昨天 " + dfs.format(calendar.getTime());
        } else if (day == 2) {
            timeDesc = "前天 " + dfs.format(calendar.getTime());
        } else {
            dfs = new SimpleDateFormat("MM-dd HH:mm");
            timeDesc = dfs.format(calendar.getTime());
        }
        return timeDesc;
    }

    /**
     * 显示为自然时间
     * @param date
     * @return
     */
    public static String showSimpleTime(Date date) {
        String timeDesc = null;
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        timeDesc = dfs.format(calendar.getTime());
        return timeDesc;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        CalendarUtil.showNaturalTime(c.getTime());
        CalendarUtil.showSimpleTime(c.getTime());
    }
}