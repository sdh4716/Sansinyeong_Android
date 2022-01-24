package com.example.sansinyeong;

import androidx.annotation.NonNull;
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

import com.example.sansinyeong.adapter.MountainCommentAdapter;
import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.model.MountainComment;
import com.example.sansinyeong.model.Mountains;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MountainBookmarkActivity extends BaseActivity {
    RecyclerView recyclerView;
    private ArrayList<Mountains> mountains_List;
    private FirebaseFirestore firebaseFirestore;
    MountainSearchAdapter mountainSearchAdapter;
    private EditText mountain_search;
    Mountains mountain;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_bookmark);

        sidebar_open();
        menu_select();
        backBtn_action();

        recyclerViewInitSetting();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference_get = firebaseDatabase.getReference().child("users").child(user.getUid()).child("mountain_bookmark");
        databaseReference_get.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mountains_List.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Mountains mountains = ds.getValue(Mountains.class);
                        mountains_List.add(mountains);
                    }
                    mountainSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
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



    public void recyclerViewInitSetting(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_mountain_bookmark);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mountains_List = new ArrayList<>(); // Plan 객체를 담을 ArrayList (어댑터 쪽으로)
        mountainSearchAdapter = new MountainSearchAdapter(mountains_List);
        recyclerView.setAdapter(mountainSearchAdapter);

    }

}
