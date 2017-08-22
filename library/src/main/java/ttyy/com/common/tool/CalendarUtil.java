package ttyy.com.common.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * author: admin
 * date: 2017/08/22
 * version: 0
 * mail: secret
 * desc: CalendarUtil
 */
public class CalendarUtil {
    /*
     * Calendar 的 month 从 0 开始，也就是全年 12 个月由 0 ~ 11 进行表示。
     * Calendar.DAY_OF_WEEK 定义和值如下：
     * Calendar.SUNDAY = 1
     * Calendar.MONDAY = 2
     * Calendar.TUESDAY = 3
     * Calendar.WEDNESDAY = 4
     * Calendar.THURSDAY = 5
     * Calendar.FRIDAY = 6
     * Calendar.SATURDAY = 7
     */
    Calendar mCalendar;
    SimpleDateFormat mSdf;

    protected CalendarUtil() {
        mCalendar = Calendar.getInstance();
        mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public String getDate(SimpleDateFormat sdf){
        return sdf.format(mCalendar.getTime());
    }

    public String getDate(){
        return mSdf.format(mCalendar.getTime());
    }

    public long getDateMillions(){
        return mCalendar.getTimeInMillis();
    }

    public int getYear(){
        return mCalendar.get(Calendar.YEAR);
    }

    public int getMonth(){
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    public int getHour12(){
        return mCalendar.get(Calendar.HOUR);
    }

    public int getHour24(){
        return mCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(){
        return mCalendar.get(Calendar.MINUTE);
    }

    public int getSecond(){
        return mCalendar.get(Calendar.SECOND);
    }

    public int getMilliSecond(){
        return mCalendar.get(Calendar.MILLISECOND);
    }

    public int getDayOfYear(){
        return mCalendar.get(Calendar.DAY_OF_YEAR);
    }

    public int getDayOfMonth(){
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeek(){
        return mCalendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getDays(int year, int month){
        int days;
        if(month == 2){
            if (year % 4 == 0 && !(year % 100 == 0) || year % 400 == 0){
                days = 29;
            }else {
                days = 28;
            }
        }else {
            switch (month){
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    days = 31;
                    break;
                default:
                    days = 30;
            }
        }

        return days;
    }
}
