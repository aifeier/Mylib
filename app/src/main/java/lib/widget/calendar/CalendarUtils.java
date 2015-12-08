package lib.widget.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by n-240 on 2015/12/8.
 *
 * @author cwf
 */
public class CalendarUtils {

    public static String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};




    /*获取上个月的天数*/
    public static int getSizeoflastMonth(Calendar calendar){
        //下面可以设置月份，注：月份设置要减1，所以设置1月就是1-1，设置2月就是2-1，如此类推
//        calendar.set(Calendar.MONTH, calendar.get(calendar.MONTH) - 1);
        //调到上个月
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.MONTH, calendar.get(calendar.MONTH) - 1);
        //得到一个月最最后一天日期(31/30/29/28)
        return cal_1.getActualMaximum(Calendar.DATE);
    }

    /*获取当当前月的天数*/
    public static int getSizeofMonth(Calendar calendar){
        return calendar.getActualMaximum(Calendar.DATE);
    }


    public static Date getFristDayDateofMonth(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return cal_1.getTime();
    }

    public static int getFristDayIntofMonth(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 0);//设置为0号,当前日期既为本月最后一天
        return cal_1.get(Calendar.DAY_OF_MONTH);
    }

    public static int getFristDayIntofWeek(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return cal_1.get(Calendar.DAY_OF_WEEK);
    }

    public static Date getLastDayDateofMonth(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月最后一天
        return cal_1.getTime();
    }

    public static int getLastDayIntofMonth(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 0);//设置为0号,当前日期既为本月最后一天
        return cal_1.get(Calendar.DAY_OF_MONTH);
    }

    public static int getLastDayIntofWeek(Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, 0);//设置为0号,当前日期既为本月最后一天
        return cal_1.get(Calendar.DAY_OF_WEEK);
    }

    public static Date getDayofMonth(int day, Calendar calendar){
        Calendar   cal_1= calendar;//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH, day);//设置为1号,当前日期既为本月第一天
        return cal_1.getTime();
    }

    public static String getWeekofDate(Date date, Calendar calendar){
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }
}
