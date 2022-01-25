package com.example.sansinyeong;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.model.Mountains;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MountainListActivity extends BaseActivity {
    RecyclerView recyclerView;
    private ArrayList<Mountains> mountains_List;
    Mountains mountain;
    private FirebaseFirestore firebaseFirestore;
    MountainSearchAdapter mountainSearchAdapter;
    private EditText mountain_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_list);

        sidebar_open();
        menu_select();
        backBtn_action();
        getPlanCount();

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.activity_mountainList_recyclerView);
        EventChangeListener();

        mountain_search = findViewById(R.id.activity_mountainList_sv_mountain_search);
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
                    recyclerView.setLayoutManager(new LinearLayoutManager(MountainListActivity.this));
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
                Intent intent = new Intent(v.getContext(),MountainDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("m_name",mountain.getName());
                intent.putExtra("m_addr",mountain.getAddress());
                intent.putExtra("m_height",mountain.getHeight());
                intent.putExtra("m_feature",mountain.getFeature());
                intent.putExtra("m_img1",mountain.getImg1());
                intent.putExtra("m_img2",mountain.getImg2());
                intent.putExtra("m_img3",mountain.getImg3());
                v.getContext().startActivity(intent);

            }
        });
    }



    public void EventChangeListener() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        mountains_List = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mountainSearchAdapter = new MountainSearchAdapter(mountains_List);
        recyclerView.setAdapter(mountainSearchAdapter);
        firebaseFirestore.collection("mountains")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
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
}