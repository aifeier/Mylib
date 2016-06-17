package demo.custom.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cwf.app.cwf.R;
import com.cwf.app.cwf.SlidingLayout;
import com.squareup.haha.trove.TObjectHash;

import java.util.Calendar;

import lib.BaseActivity;
import lib.widget1.calendar.cons.DPMode;
import lib.widget1.calendar.views.MonthView;

/**
 * Created by n-240 on 2016/3/3.
 *
 * @author cwf
 */
public class SlidingAcitivity2 extends AppCompatActivity {
    private MonthView monthView;
    private Calendar now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_view);
        now = Calendar.getInstance();
        monthView = (MonthView) findViewById(R.id.month_calendar);
        monthView.setDPMode(DPMode.SINGLE);
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(new MonthView.OnDateChangeListener() {
            @Override
            public void onDateChange(int year, int month) {
                Toast.makeText(SlidingAcitivity2.this, year + "+" + month, Toast.LENGTH_SHORT).show();
            }
        });
        monthView.setOnDatePickedListener(new MonthView.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                Toast.makeText(SlidingAcitivity2.this, date, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
