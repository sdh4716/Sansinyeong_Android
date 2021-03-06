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
        // ?????????????????? ?????????????????? ??????
        firebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference??? ????????????????????? ?????? ????????? ???????????? ????????? ???????????? ??????.
        //?????? ????????? ???????????????????????? ??? ???????????????
        //??????(????????? ?????? ??????)??? ?????? ????????? ??????????????? ?????? ????????????.
        databaseReference = firebaseDatabase.getReference();

        flag = "gone";
        flag2 = "gone";

        String init_date = CalendarDay.today().toString().replace("CalendarDay","").replace("{","").replace("}","");
        plan_start.setText(init_date);

        calendar.setDateSelected(CalendarDay.today(), true);
//        EventChangeListener();
        
        // ???????????? ?????? ??? Firestore??? mountains ???????????? ?????? ?????? ???????????? ????????????
        EventChangeListener();



        // ???????????? visibility??? flag??? ???????????? ?????? ?????????
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
                    showToast(HikingPlanActivity.this, "??? ?????? ????????? ???????????? ??? ????????????");
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
                //EditText???
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
                if (plan_mountain_choose.getText().toString().equals("?????? ???????????????")){
                    showToast(HikingPlanActivity.this,"?????? ???????????????");
                }else if(plan_members.getText().toString().equals("0")){
                    showToast(HikingPlanActivity.this,"????????? ???????????????");
                }else{
                    add_plan(plan_start.getText().toString(),plan_end.getText().toString(),
                            plan_mountain_choose.getText().toString(), plan_members.getText().toString(),
                            plan_bak.getText().toString());
                    showToast(HikingPlanActivity.this,"????????? ?????????????????????");

                    finish();
                }
            }
        });

    }

//    getDataFromFireStore()?????? ?????? ?????? ??????.. ??????????????? 2?????? ????????? ?????????..
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
                /////////!!??????!!////////
                // ?????? ?????? ????????? ??? ????????? ?????? ?????? ??? Activity??? OnCreate ?????? ?????? ??????????????????
                // ????????? ??????????????? ???????????? ?????? ???????????? ????????? ????????? OnItemSelectListener??? ???????????? ??????
                // ??? ????????? ?????? ArrayList??? ????????? ???????????? ?????? ???????????? OnItemSelectListener??? ????????? ?????????
                // ?????? ??? ????????? ????????? ????????? Item??? ????????? ???????????? EditText??? setText??? ??? ??????!! ????????? ????????? ???????????? ??? ?????????
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
    //?????? ?????????????????? Realtime database??? ????????? ??????
    public void add_plan(String start_date, String end_date, String mountain, String members, String bak){

        //Plan.java?????? ???????????? ??????
        Plan plan = new Plan(start_date,end_date,mountain,members,bak);

        // .child??? ?????? ?????? ?????????
        // .push()??? ?????? ????????? ???????????? ????????? ?????? ?????? (????????? ??? ???????????? ????????? ???????????? ????????????)
        // .setValue??? Plan ??????????????? ????????? ?????? ???????????? ?????????
        databaseReference.child("users").child(user.getUid()).child("plans").push().setValue(plan);
    }
}