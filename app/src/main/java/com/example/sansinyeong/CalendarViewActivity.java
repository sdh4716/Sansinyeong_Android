package com.example.sansinyeong;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.List;


public class CalendarViewActivity extends BaseActivity {
    boolean Multi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar_view);

        sidebar_open();
        menu_select();
        backBtn_action();

        TextView start = (TextView) findViewById(R.id.start_date);
        TextView middle = (TextView) findViewById(R.id.middle_date);
        TextView end = (TextView) findViewById(R.id.end_date);




//        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.cv_calendar);
//
//
//        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                String  Date_only = date.toString();
//                start.setText(Date_only.replace("CalendarDay","").replace("{","").replace("}",""));
//                start.setVisibility(View.VISIBLE);
//                end.setVisibility(View.GONE);
//                middle.setVisibility(View.GONE);
//
//
//            }
//        });
//
//        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
//            @Override
//            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
//                String startDate, endDate;
//                startDate = dates.get(0).toString();
//                endDate = dates.get(dates.size()-1).toString();
//                start.setText(startDate.replace("CalendarDay","").replace("{","").replace("}",""));
//                end.setText(endDate.replace("CalendarDay","").replace("{","").replace("}",""));
//                start.setVisibility(View.VISIBLE);
//                end.setVisibility(View.VISIBLE);
//                middle.setVisibility(View.VISIBLE);
//            }
//        });



    }



    public void showToast(Activity activity, String msg){
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}