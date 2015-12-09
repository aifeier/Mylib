package lib.widget.calendar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by n-240 on 2015/12/8.
 *
 * @author cwf
 */
public class CalendarDate implements Serializable {

    public static int TYPE_PREVIOUS = 1;/*上月*/
    public static int TYPE_PRESENT = 2;/*本月*/
    public static int TYPE_NEXT = 3;/*下个月*/
    public static int TYPE_TODAY = 4;/*今天*/


    /*日期*/
    private String stringnum;

    /*类型，本月，上月，下个月, 今天*/
    private int type;

    /*当前日期*/
    private Calendar calendar;

    private boolean selected;

    public String getStringnum() {
        return stringnum;
    }

    public void setStringnum(String stringnum) {
        this.stringnum = stringnum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
