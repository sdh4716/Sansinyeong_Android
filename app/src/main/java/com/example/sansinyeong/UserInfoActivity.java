package com.example.sansinyeong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.model.MountainComment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoFragment";
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private TextView bookmarkCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

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
        firebaseDatabase = FirebaseDatabase.getInstance();


        ImageView profileImageView = findViewById(R.id.profileImageView);
        final TextView nameTextView = findViewById(R.id.nameTextView);
        final TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        final TextView birthDayTextView = findViewById(R.id.birthDayTextView);
        final TextView emailTextView = findViewById(R.id.emailTextView);
        final RelativeLayout bookmark_go = findViewById(R.id.rl_user_info_bookmark);
        final Button profile_edit = findViewById(R.id.btn_profile_edit);
        bookmarkCount = findViewById(R.id.user_info_bookmark_gun);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.getData().get("photoUrl") != null){
                                Glide.with(getApplicationContext()).load(document.getData().get("photoUrl")).centerCrop().override(500).into(profileImageView);
                            }
                            nameTextView.setText(document.getData().get("name").toString());
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            birthDayTextView.setText(document.getData().get("birthDay").toString());
//                            emailTextView.setText(document.getData().get("email").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // 댓글 변화를 감지해 리사이클러뷰를 갱신
        DocumentReference documentReference_update = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference_update.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Glide.with(getApplicationContext()).load(value.getData().get("photoUrl")).centerCrop().override(500).into(profileImageView);
                nameTextView.setText(value.getData().get("name").toString());
                phoneNumberTextView.setText(value.getData().get("phoneNumber").toString());
                birthDayTextView.setText(value.getData().get("birthDay").toString());
            }
        });

        bookmark_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(MountainBookmarkActivity.class);
            }
        });

        getBookmarkCount();

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(MemberInitActivity.class);
            }
        });
    }



    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void getBookmarkCount(){
        DatabaseReference databaseReference_getBookmarkCount = firebaseDatabase.getReference().child("users").child(user.getUid()).child("mountain_bookmark");
        databaseReference_getBookmarkCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookmarkCount.setText(String.valueOf(snapshot.getChildrenCount())+"건");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}