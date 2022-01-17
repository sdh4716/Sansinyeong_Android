package com.example.sansinyeong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        sidebar_open();
        menu_select();
        backBtn_action();


        final ImageView profileImageView = findViewById(R.id.profileImageView);
        final TextView nameTextView = findViewById(R.id.nameTextView);
        final TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        final TextView birthDayTextView = findViewById(R.id.birthDayTextView);
        final TextView addressTextView = findViewById(R.id.addressTextView);

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
                            addressTextView.setText(document.getData().get("address").toString());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}