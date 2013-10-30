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

    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, 0, 0, 0);
    }
    public static Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }
    public static Date createTime(int hour, int minute, int second) {
        return createDate(0, 0, 0, hour, minute, second);
    }
    public static Date createTime(int hour, int minute) {
        return createDate(0, 0, 0, hour, minute, 0);
    }

    public static int compareTime(Date date1, Date date2) {
        int compare = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int hour2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int minute1 = calendar1.get(Calendar.MINUTE);
        int minute2 = calendar2.get(Calendar.MINUTE);
        if(hour1 > hour2) {
            compare = 1;
        } else if (hour1 < hour2){
            compare = -1;
        } else if (hour1 == hour2){
            if(minute1 > minute2) {
                compare = 1;
            } else if (minute1 < minute2){
                compare = -1;
            } else if (minute1 == minute2){
                compare = 0;
            }
        }
        return compare;
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        CalendarUtil.showNaturalTime(c.getTime());
        CalendarUtil.showSimpleTime(c.getTime());
    }
}