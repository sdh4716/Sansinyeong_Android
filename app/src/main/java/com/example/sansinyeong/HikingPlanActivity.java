package com.example.sansinyeong;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.model.Mountains;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import static com.example.sansinyeong.Util.showToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HikingPlanActivity extends BaseActivity {

    private ImageView scroll_down, plan_iv_plus, plan_iv_minus;
    private MaterialCalendarView calendar;
    private String flag;
    private LinearLayout ll_calendar;
    private TextView plan_start, plan_middle, plan_end, plan_bak, plan_members;
    private int members = 0, bak = 0;
    MountainSearchAdapter mountainSearchAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_plan);

        sidebar_open();
        menu_select();
        backBtn_action();

        scroll_down = findViewById(R.id.plan_scroll_down);
        calendar = (MaterialCalendarView) findViewById(R.id.cv_calendar);
        ll_calendar = findViewById(R.id.ll_calendar);
        plan_start = findViewById(R.id.plan_start_date);
        plan_middle = findViewById(R.id.plan_middle_date);
        plan_end = findViewById(R.id.plan_end_date);
        plan_bak = findViewById(R.id.plan_bak);
        plan_members = findViewById(R.id.plan_tv_members);
        plan_iv_plus = findViewById(R.id.plan_iv_plus);
        plan_iv_minus = findViewById(R.id.plan_iv_minus);
        flag = "gone";

        String init_date = CalendarDay.today().toString().replace("CalendarDay","").replace("{","").replace("}","");
        plan_start.setText(init_date);

        calendar.setDateSelected(CalendarDay.today(), true);



        // 캘린더의 visibility를 flag로 구분하여 열고 닫는다
        scroll_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag=="gone"){
                    ll_calendar.setVisibility(View.VISIBLE);
                    scroll_down.setImageResource(R.drawable.ic_scroll_up);
                    flag = "visible";

                }else{
                    ll_calendar.setVisibility(View.GONE);
                    scroll_down.setImageResource(R.drawable.ic_scroll_down);
                    flag = "gone";
                }



            }
        });

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String  Date_only = date.toString();
                plan_start.setText(Date_only.replace("CalendarDay","").replace("{","").replace("}",""));
                plan_start.setVisibility(View.VISIBLE);
                plan_end.setVisibility(View.GONE);
                plan_middle.setVisibility(View.GONE);
                plan_bak.setText("0");
            }
        });

        calendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                String startDate, endDate;
                startDate = dates.get(0).toString();
                endDate = dates.get(dates.size()-1).toString();
                plan_start.setText(startDate.replace("CalendarDay","").replace("{","").replace("}",""));
                plan_end.setText(endDate.replace("CalendarDay","").replace("{","").replace("}",""));
                plan_start.setVisibility(View.VISIBLE);
                plan_end.setVisibility(View.VISIBLE);
                plan_middle.setVisibility(View.VISIBLE);
                bak = dates.size()-1;
                plan_bak.setText(Integer.toString(bak));
            }
        });

        plan_iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (members == 0){
                    showToast(HikingPlanActivity.this, "더 이상 인원을 감소시킬 수 없습니다");
                }else{
                    members -= 1;
                    plan_members.setText(Integer.toString(members));
                }

            }
        });

        plan_iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                members += 1;
                plan_members.setText(Integer.toString(members));
            }
        });



        recyclerView = findViewById(R.id.plan_recyclerView);

        //레이아웃 설정
        manager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

//        mountainSearchAdapter;

        //파이어스토어에 접근하기 위한 객체를 생성한다.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //CollectionReference 는 파이어스토어의 컬렉션을 참조하는 객체다.
        CollectionReference productRef = db.collection("mountains");
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //document.getData() or document.getId() 등등 여러 방법으로
                        //데이터를 가져올 수 있다.
//                        List<Mountains> mountains = document.getData(List)
                    }
                }else{
                    showToast(HikingPlanActivity.this,"failed");
                }
            }
        });
    }
}