package com.example.sansinyeong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sansinyeong.BaseActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.model.FibaseID;
import com.example.sansinyeong.model.Mountains;
import com.example.sansinyeong.model.Notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Noticepost_Activity extends BaseActivity implements View.OnClickListener {

    final String TAG="Noticepost_Activity";
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private EditText nTitle,nContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticepost);

        nTitle=findViewById(R.id.notice_title_edit);
        nContents=findViewById(R.id.notice_content_edit);

        findViewById(R.id.notice_save).setOnClickListener(this);
        sidebar_open();
        menu_select();
        backBtn_action();
    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser()!=null){
            String noticeId=mStore.collection(FibaseID.notice).document().getId();
            Map<String,Object> data=new HashMap<>();
            data.put(FibaseID.documentId,mAuth.getCurrentUser().getUid());
            data.put(FibaseID.ntitle,nTitle.getText().toString());
            data.put(FibaseID.ncontents,nContents.getText().toString());
            mStore.collection("notice").document(noticeId).set(data,SetOptions.merge());

            add_notice_main(mAuth.getCurrentUser().getUid(),nTitle.getText().toString(),nContents.getText().toString());

            finish();
        }
    }

    public void add_notice_main(String documentId, String nttitle, String ntcontent){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Notice notice = new Notice(documentId,nttitle,ntcontent);
        databaseReference.child("notice_main").child("notice").setValue(notice);
    }
}