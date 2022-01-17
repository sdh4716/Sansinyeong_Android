package com.example.sansinyeong;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class Hiking_plan extends BaseActivity {

    private EditText edt_date_selector;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_plan);

        sidebar_open();
        menu_select();
        backBtn_action();
        //----- 날짜선택 Start -----

        edt_date_selector = findViewById(R.id.edt_date);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {

                        // Date Picker에서 선택한 날짜를 TextView에 설정

                        edt_date_selector.setText(String.format("%d-%d-%d", yy,mm+1,dd));

                    }

        };

        edt_date_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.

                Calendar cal = Calendar.getInstance();

                new DatePickerDialog( Hiking_plan.this , mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
            }
        });

        //----- 날짜선택 End -----
    }
}