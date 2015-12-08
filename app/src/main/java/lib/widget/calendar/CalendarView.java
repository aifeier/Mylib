package lib.widget.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import java.util.ArrayList;
import java.util.Calendar;

import lib.utils.TimeUtils;
import lib.widget.GridAdapter;
import lib.widget.NoScrollGridView;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/12/8.
 *
 * @author cwf
 */
public class CalendarView extends LinearLayout{
    public CalendarView(Context context) {
        super(context);
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private TextView calendar_day;
    private NoScrollGridView calendar_grid;
    private Calendar mCalendar;
    private void initView(){
        mCalendar = getmCalendar();
        if(mCalendar == null)
            mCalendar = Calendar.getInstance();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar, this, true);
        calendar_day = (TextView) findViewById(R.id.calendar_day);
        calendar_day.setText(TimeUtils.getSimpleDate2(mCalendar.getTime()));
        calendar_grid = (NoScrollGridView) findViewById(R.id.calendar_grid);
        initGrid();
    }

    private ArrayList<CalendarDate> calendarDateArrayList;
    private void initGrid(){
        calendarDateArrayList = new ArrayList<>();
        /*上月*/
        Calendar calendar = mCalendar;
        calendar.add(Calendar.MONTH, 0);
        int sizeOfLastMonth = CalendarUtils.getSizeoflastMonth(calendar)
                - CalendarUtils.getFristDayIntofWeek(calendar);
        for(int i = 0; i <= CalendarUtils.getFristDayIntofWeek(calendar); i++){
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_PREVIOUS);
            calendarDate.setStringnum(sizeOfLastMonth + "");
            calendarDate.setMdate(CalendarUtils.getDayofMonth(sizeOfLastMonth, calendar));
            sizeOfLastMonth++;
            calendarDateArrayList.add(calendarDate);
        }
        /*本月*/
        calendar = mCalendar;
        calendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 1);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for(int i =1 ; i<= mCalendar.getActualMaximum(Calendar.DATE); i++) {
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_PRESENT);
            calendarDate.setStringnum(i + "");
            if(i== today)
                calendarDate.setType(CalendarDate.TYPE_TODAY);
            calendarDate.setMdate(CalendarUtils.getDayofMonth(i, calendar));
            calendarDateArrayList.add(calendarDate);
        }

        /*下个月*/
        int sizeOfNextMonth = CalendarUtils.getLastDayIntofWeek(mCalendar);
        calendar = mCalendar;
        calendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 2);
        for(int i=1; i<= sizeOfNextMonth; i++){
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_NEXT);
            calendarDate.setStringnum(i + "");
            calendarDate.setMdate(CalendarUtils.getDayofMonth(i, calendar));
            calendarDateArrayList.add(calendarDate);
        }

        calendar_grid.setAdapter(new GridAdapter<CalendarDate>(getContext()
                , R.layout.layout_calendar_item,calendarDateArrayList) {
            @Override
            public void buildView(ViewHolder holder, CalendarDate data) {
                TextView textView = (TextView) holder.findViewById(R.id.calendar_item_day);
                if (data.getType() == CalendarDate.TYPE_DAY) {
                    textView.setBackgroundResource(R.color.white);
                } else if (data.getType() == CalendarDate.TYPE_PREVIOUS) {
                    textView.setBackgroundResource(R.color.silver);
                } else if (data.getType() == CalendarDate.TYPE_PRESENT) {
                    textView.setBackgroundResource(R.color.lightsteelblue);
                } else if (data.getType() == CalendarDate.TYPE_NEXT) {
                    textView.setBackgroundResource(R.color.silver);
                } else if (data.getType() == CalendarDate.TYPE_TODAY) {
                    textView.setBackgroundResource(R.color.blueviolet);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextColor(
                            getContext().getResources().getColor(R.color.black, null));
                }else{
                    textView.setTextColor(Color.parseColor("#ff0000ff"));
                }
                textView.setText(data.getStringnum());
            }

            @Override
            public void buildAddView(ViewHolder holder) {

            }

            @Override
            public boolean canAddItem() {
                return false;
            }

            @Override
            public int getMaxSize() {
                return calendarDateArrayList.size();
            }
        });

        calendar_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarDate calendarDate = (CalendarDate) parent.getAdapter().getItem(position);
                Toast.makeText(getContext(), TimeUtils.getSimpleDate2(calendarDate.getMdate()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Calendar getmCalendar() {
        return mCalendar;
    }

    public void setmCalendar(Calendar calendar) {
        this.mCalendar = calendar;
    }
}
