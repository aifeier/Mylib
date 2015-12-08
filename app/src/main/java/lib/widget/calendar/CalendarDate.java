package lib.widget.calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by n-240 on 2015/12/8.
 *
 * @author cwf
 */
public class CalendarDate implements Serializable {

    public static int TYPE_PREVIOUS = 1;
    public static int TYPE_PRESENT = 2;
    public static int TYPE_NEXT = 3;
    public static int TYPE_TODAY = 4;
    public static int TYPE_DAY = 5;/*上面的日期备注，不能选择*/


    /*日期*/
    private String stringnum;

    /*类型，本月，上月，下个月, 今天*/
    private int type;

    /*当前日期*/
    private Date mdate;

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

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }
}
