package com.example.sansinyeong;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.model.Mountains;
import com.example.sansinyeong.model.Plan;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import static com.example.sansinyeong.Util.showToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HikingPlanActivity extends BaseActivity {

    private ImageView scroll_down, scroll_down2, plan_iv_plus, plan_iv_minus;
    private MaterialCalendarView calendar;
    private String flag, flag2;
    String search_name;
    private LinearLayout ll_calendar, ll_mountain_search;
    private TextView plan_start, plan_middle, plan_end, plan_bak, plan_members, plan_mountain_choose;
    private int members = 0, bak = 0;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<Mountains> mountains_List;
    private Button btn_add;
    MountainSearchAdapter mountainSearchAdapter;
    EditText mountain_search;
    Mountains mountain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_plan);

        sidebar_open();
        menu_select();
        backBtn_action();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPlanCount();
            }
        },400);


        user = FirebaseAuth.getInstance().getCurrentUser();
        btn_add = findViewById(R.id.btn_add_plan);
        scroll_down = findViewById(R.id.plan_scroll_down);
        scroll_down2 = findViewById(R.id.plan_scroll_down2);
        calendar = (MaterialCalendarView) findViewById(R.id.cv_calendar);
        ll_calendar = findViewById(R.id.ll_calendar);
        ll_mountain_search = findViewById(R.id.ll_mountain_search);
        plan_start = findViewById(R.id.plan_start_date);
        plan_middle = findViewById(R.id.plan_middle_date);
        plan_end = findViewById(R.id.plan_end_date);
        plan_bak = findViewById(R.id.plan_bak);
        plan_members = findViewById(R.id.plan_tv_members);
        plan_iv_plus = findViewById(R.id.plan_iv_plus);
        plan_iv_minus = findViewById(R.id.plan_iv_minus);
        plan_mountain_choose = findViewById(R.id.plan_mountain_choose);
        recyclerView = findViewById(R.id.plan_recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        // 파이어베이스 데이터베이스 연동
        firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference는 데이터베이스의 특정 위치로 연결하는 거라고 생각하면 된다.
        //현재 연결은 데이터베이스에만 딱 연결해놓고
        //키값(테이블 또는 속성)의 위치 까지는 들어가지는 않은 모습이다.
        databaseReference = firebaseDatabase.getReference();

        flag = "gone";
        flag2 = "gone";

        String init_date = CalendarDay.today().toString().replace("CalendarDay","").replace("{","").replace("}","");
        plan_start.setText(init_date);

        calendar.setDateSelected(CalendarDay.today(), true);
//        EventChangeListener();
        
        // 액티비티 시작 시 Firestore의 mountains 컬렉션에 있는 모든 데이터를 가져온다
        EventChangeListener();



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

        scroll_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2 == "gone"){
                    ll_mountain_search.setVisibility(View.VISIBLE);
                    scroll_down2.setImageResource(R.drawable.ic_scroll_up);
                    flag2 = "visible";
                }else{
                    ll_mountain_search.setVisibility(View.GONE);
                    scroll_down2.setImageResource(R.drawable.ic_scroll_down);
                    flag2 = "gone";
                }
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


        mountain_search = findViewById(R.id.sv_mountain_search);
        mountain_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("search","Searchbox has changed to: " + s.toString());
                //EditText에
                if(mountain_search.getText().toString().equals("")){
                    getDataFromFireStore();
                }else{
                    mountains_List.clear();
                    firebaseFirestore = FirebaseFirestore.getInstance();
//                    mountains_List = new ArrayList<>();
//                    mountainSearchAdapter = new MountainSearchAdapter(mountains_List);
//                    recyclerView = findViewById(R.id.plan_recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HikingPlanActivity.this));
                    recyclerView.setAdapter(mountainSearchAdapter);
                    firebaseFirestore.collection("mountains").whereEqualTo("name",s.toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mountains_List.clear();

                            for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                                mountains_List.add(ds.toObject(Mountains.class));
                            }
                            mountainSearchAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        });

        mountainSearchAdapter.setOnItemClickListener(new MountainSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mountain = mountains_List.get(position);
                Log.d("mList",mountain.getName().toString());
                plan_mountain_choose.setText(mountain.getName());
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plan_mountain_choose.getText().toString().equals("산을 선택하세요")){
                    showToast(HikingPlanActivity.this,"산을 선택하세요");
                }else if(plan_members.getText().toString().equals("0")){
                    showToast(HikingPlanActivity.this,"인원을 선택하세요");
                }else{
                    add_plan(plan_start.getText().toString(),plan_end.getText().toString(),
                            plan_mountain_choose.getText().toString(), plan_members.getText().toString(),
                            plan_bak.getText().toString());
                    showToast(HikingPlanActivity.this,"일정이 등록되었습니다");

                    finish();
                }
            }
        });

    }

//    getDataFromFireStore()이랑 출력 결과 같음.. 어쩌다보니 2개를 만들게 됐어요..
    public void EventChangeListener(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        mountains_List = new ArrayList<>();
//        recyclerView = findViewById(R.id.plan_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mountainSearchAdapter = new MountainSearchAdapter(mountains_List);
        recyclerView.setAdapter(mountainSearchAdapter);
        firebaseFirestore.collection("mountains")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                mountains_List.add(dc.getDocument().toObject(Mountains.class));

                            }
                            mountainSearchAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void getDataFromFireStore(){ 
                /////////!!중요!!////////
                // 아래 주석 처리된 두 녀석은 제일 처음 이 Activity를 OnCreate 할때 한번 생성된것들임
                // 이유는 모르겠으나 이것들을 다시 생성하면 처음에 먹혔던 OnItemSelectListener가 작동하지 않음
                // 내 생각엔 새로 ArrayList를 만드는 과정에서 뭔가 달라져서 OnItemSelectListener에 걸리지 않는듯
                // 아래 두 코드가 없어야 선택한 Item의 이름을 가져와서 EditText에 setText할 수 있음!! 하지만 있어도 리스트는 잘 가져옴
//            mountains_List = new ArrayList<>();
//            mountainSearchAdapter = new MountainSearchAdapter(mountains_List);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mountainSearchAdapter);
            firebaseFirestore.collection("mountains").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    mountains_List.clear();

                    for(QueryDocumentSnapshot ds : queryDocumentSnapshots){
                        mountains_List.add(ds.toObject(Mountains.class));
                    }
                    mountainSearchAdapter.notifyDataSetChanged();
                }
            });

    }
    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void add_plan(String start_date, String end_date, String mountain, String members, String bak){

        //Plan.java에서 선언했던 함수
        Plan plan = new Plan(start_date,end_date,mountain,members,bak);

        // .child로 타고 타고 내려감
        // .push()는 상위 키값을 랜덤으로 설정해 주는 함수 (블로그 왈 채팅기능 만들때 사용하면 좋답니다)
        // .setValue로 Plan 클래스에서 지정한 값을 자동으로 넣어줌
        databaseReference.child("users").child(user.getUid()).child("plans").push().setValue(plan);
    }
}