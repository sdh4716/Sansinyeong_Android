package com.example.sansinyeong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.BaseActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.adapter.NoticeAdapter;
import com.example.sansinyeong.model.FibaseID;
import com.example.sansinyeong.model.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Notice_Activity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "Notice_Activity";
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView NoticeRecyclerView;

    private String admin;

    private FirebaseUser mUser;

    private NoticeAdapter mAdapter;
    private List<Notice> mdatas;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        NoticeRecyclerView=findViewById(R.id.notice_recyclerview);

        mdatas=new ArrayList<>();


        //리사이클러뷰 레이아웃매니저 필수!
        mLayoutManager = new LinearLayoutManager(this);
        NoticeRecyclerView.setLayoutManager(mLayoutManager);

        sidebar_open();
        menu_select();
        backBtn_action();




        //users의 role값 불러오기
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            }
                            admin=document.getData().get("role").toString();
                        Log.e(TAG,"admin:"+admin);
                        if(admin.equals("1")){
                            findViewById(R.id.notice_edit).setVisibility(View.VISIBLE);

                        }else{
                            findViewById(R.id.notice_edit).setVisibility(View.INVISIBLE);

                        }
                        return;


                        }
                    }
                }
        });






        findViewById(R.id.notice_edit).setOnClickListener(this);




    }
    @Override
    protected void onStart() {
        super.onStart();
        mdatas=new ArrayList<>();
        mStore.collection("notice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentSnapshot snap : task.getResult()){
                                    Map<String,Object> shot=snap.getData();
                                    String documentId=String.valueOf(shot.get(FibaseID.documentId));
                                    String title=String.valueOf(shot.get(FibaseID.ntitle));
                                    String contents=String.valueOf(shot.get(FibaseID.ncontents));
                                    Notice data=new Notice(documentId,title,contents);
                                    mdatas.add(data);

                                }
                                mAdapter=new NoticeAdapter(mdatas);
                                NoticeRecyclerView.setAdapter(mAdapter);
                            }

                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,Noticepost_Activity.class));

    }
}