package com.example.sansinyeong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.HikingPlanActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.UserInfoActivity;
import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.adapter.PlanListAdapter;
import com.example.sansinyeong.model.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.example.sansinyeong.Util.showToast;

import java.util.ArrayList;

public class PlanFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlanListAdapter planListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Plan> plans;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button btn_add;

    public PlanFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hiking_plan_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_hiking_plan_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        plans = new ArrayList<>(); // Plan 객체를 담을 ArrayList (어댑터 쪽으로)
        planListAdapter = new PlanListAdapter(plans);
        recyclerView.setAdapter(planListAdapter);
        btn_add = (Button) view.findViewById(R.id.plan_add_btn);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("plans");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                plans.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Plan plan = ds.getValue(Plan.class);
                    plans.add(plan);
                }
                planListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), HikingPlanActivity.class);
                startActivity(intent);
            }
        });



        return view;



    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

}