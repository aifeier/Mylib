package lib.widget.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private Button btn_precious, btn_next;
    private void initView(){
        mCalendar = getmCalendar();
        if(mCalendar == null)
            mCalendar = Calendar.getInstance();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar, this, true);
        calendar_day = (TextView) findViewById(R.id.calendar_day);
        calendar_grid = (NoScrollGridView) findViewById(R.id.calendar_grid);
        btn_precious = (Button) findViewById(R.id.calendar_previous);
        btn_next = (Button) findViewById(R.id.calendar_next);
        btn_precious.setOnClickListener(onClickListener);
        btn_next.setOnClickListener(onClickListener);
        mCalendar.add(Calendar.MONTH, 0);
        initDay();
        initGrid();
    }

    /*当前选择日期的年月*/
    private int month;
    private int year;
    private int dayofmonth;

    private void initDay(){
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH);
        dayofmonth = mCalendar.get(Calendar.DAY_OF_MONTH);
    }

    private void resetCalendar(){
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayofmonth);
    }

    private ArrayList<CalendarDate> calendarDateArrayList;
    private void refreshDate(){
        if(calendarDateArrayList==null)
            calendarDateArrayList = new ArrayList<>();
        else
            calendarDateArrayList.clear();
        /*上月*/
        resetCalendar();
        int start = CalendarUtils.getSizeoflastMonth(mCalendar);
        resetCalendar();
        int size = CalendarUtils.getFristDayIntofWeek(mCalendar);
        for(int i = size; i > 1; i--){

            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_PREVIOUS);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month - 1);
            c.set(Calendar.DAY_OF_MONTH, start - i + 2);
            calendarDate.setCalendar(c);
            calendarDate.setStringnum(c.get(Calendar.DAY_OF_MONTH) + "");
            calendarDateArrayList.add(calendarDate);
        }
        /*本月*/
        resetCalendar();
        resetCalendar();
        size = mCalendar.getActualMaximum(Calendar.DATE);
        for(int i =1 ; i<= size; i++) {
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_PRESENT);
            calendarDate.setStringnum(i + "");

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, i);
            calendarDate.setCalendar(c);
            if(year == Calendar.getInstance().get(Calendar.YEAR)
                    && month == Calendar.getInstance().get(Calendar.MONTH)
                    && i == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                calendarDate.setType(CalendarDate.TYPE_TODAY);
            }

            if(i == dayofmonth){
                calendarDate.setSelected(true);
            }
            calendarDateArrayList.add(calendarDate);
        }

        /*下个月*/
        resetCalendar();
        int sizeOfNextMonth = 7- CalendarUtils.getLastDayIntofWeek(mCalendar);
        for(int i=1; i<= sizeOfNextMonth; i++){
            CalendarDate calendarDate = new CalendarDate();
            calendarDate.setType(CalendarDate.TYPE_NEXT);
            calendarDate.setStringnum(i + "");
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month + 1);
            c.set(Calendar.DAY_OF_MONTH, i);
            calendarDate.setCalendar(c);
            calendarDateArrayList.add(calendarDate);
        }

        resetCalendar();
        calendar_day.setText(mCalendar.get(Calendar.YEAR) + "-"
                + (month + 1));
    }
    private void initGrid() {
        initDay();
        refreshDate();

        calendar_grid.setAdapter(new GridAdapter<CalendarDate>(getContext()
                , R.layout.layout_calendar_item,calendarDateArrayList) {
            @Override
            public void buildView(ViewHolder holder, CalendarDate data) {
                TextView textView = (TextView) holder.findViewById(R.id.calendar_item_day);
                if (data.getType() == CalendarDate.TYPE_PREVIOUS) {
//                    textView.setBackgroundResource(R.color.silver);
                } else if (data.getType() == CalendarDate.TYPE_PRESENT) {
//                    textView.setBackgroundResource(R.color.lightsteelblue);
                    textView.setTextColor(Color.parseColor("#ff000000"));
                } else if (data.getType() == CalendarDate.TYPE_NEXT) {
//                    textView.setBackgroundResource(R.color.silver);
                } else if (data.getType() == CalendarDate.TYPE_TODAY) {
//                    textView.setBackgroundResource(R.color.skyblue);
                    textView.setTextColor(Color.parseColor("#ff1111ee"));
                }

                if(data.isSelected()){
                    if(data.getType() == CalendarDate.TYPE_TODAY){
                        textView.setBackgroundResource(R.color.skyblue);
                    }else if(data.getType() == CalendarDate.TYPE_PRESENT){
                        textView.setBackgroundResource(R.color.divide_line);
                    }
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
                Toast.makeText(getContext(), +calendarDate.getCalendar().get(Calendar.YEAR)
                        +"-" +(calendarDate.getCalendar().get(Calendar.MONTH)+1)
                        +"-" + calendarDate.getCalendar().get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                mCalendar = calendarDate.getCalendar();
                initGrid();
            }
        });
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.calendar_previous:
                    mCalendar.set(year, month-1, dayofmonth);
                    initGrid();
                    break;
                case R.id.calendar_next:
                    mCalendar.set(year, month+1, dayofmonth);
                    initGrid();
                    break;
                default:
                    break;
            }
        }
    };

    public Calendar getmCalendar() {
        return mCalendar;
    }

    public void setmCalendar(Calendar calendar) {
        this.mCalendar = calendar;
    }
}
