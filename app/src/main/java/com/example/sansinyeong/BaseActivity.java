package com.example.sansinyeong;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.activity.Notice_Activity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

String ActivityName = "";
private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    NavigationView NavView;
    private FirebaseUser user;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName= info.get(0).topActivity;
        ActivityName = componentName.getShortClassName().substring(1).trim();
        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sidebar_info();
            }
        },400);



    }

    public void sidebar_info(){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(firebaseAuth.getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d("sidebar_user_get", "DocumentSnapshot data: " + document.getData());
                            if(document.getData().get("photoUrl") != null){
                                final ImageView header_profile_img = findViewById(R.id.header_profile_img);
                                Glide.with(getApplicationContext()).load(document.getData().get("photoUrl")).centerCrop().override(500).into(header_profile_img);
                            }
                            final TextView header_name = findViewById(R.id.header_name);
                            header_name.setText(document.getData().get("name").toString());
                        } else {
                            Log.d("sidebar_user_get", "No such document");
                        }
                    }
                } else {
                    Log.d("sidebar_user_get", "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        LinearLayout activityContainer = (LinearLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);

    }

    public void sidebar_open(){
        ImageView ivMenu=findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawerLayout=findViewById(R.id.drawer);
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

    }

    public void menu_select() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView NavView = findViewById(R.id.nav_view);
        NavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {

                    case R.id.item_mypage: {
                        //String??? ????????? .equals!!!!!! ==?????? ???????????? ??????!!!
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "UserInfoActivity":{
                                Toast.makeText(getApplicationContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            default: {
                                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                        }

                        break;
                    }

                    case R.id.item_mylocation: {
                        //String??? ????????? .equals!!!!!! ==?????? ???????????? ??????!!!
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), My_Location.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "My_Location":{
                                Toast.makeText(getApplicationContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            default: {
                                Intent intent = new Intent(getApplicationContext(), My_Location.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                        }

                        break;
                    }

                    case R.id.item_dangerInsert: {
                        if (firebaseAuth.getCurrentUser().getUid().equals("y7w9lcQ27ASPADsbcG1egJQnlZz1")){
                            //String??? ????????? .equals!!!!!! ==?????? ???????????? ??????!!!
                            switch (ActivityName){
                                case "After_Login":{
                                    Intent intent = new Intent(getApplicationContext(), DangerZoneInert.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(Gravity.RIGHT);
                                    break;
                                }
                                case "DangerZoneInert":{
                                    Toast.makeText(getApplicationContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                                default: {
                                    Intent intent = new Intent(getApplicationContext(), DangerZoneInert.class);
                                    startActivity(intent);
                                    drawerLayout.closeDrawer(Gravity.RIGHT);
                                    break;
                                }
                            }
                            break;
                        }else{
                            Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
                            break;
                        }


                    }

                    case R.id.item_hikingsearch: {
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), MountainListActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "HikingPlanActivity":{
                                Toast.makeText(getApplicationContext(), "?????? ??????????????????", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            default: {
                                Intent intent = new Intent(getApplicationContext(), MountainListActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                        }
                        break;
                    }

                    case R.id.item_logout: {
                        firebaseAuth.signOut();


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.item_noticemenu: {
                        Intent intent=new Intent(getApplicationContext(), Notice_Activity.class);
                        startActivity(intent);
                        break;


                    }
                }
                return false;
            }
        });
    }

    //????????????
    private void Google_signOut() {
        // Firebase sign out
        firebaseAuth.signOut();
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "???????????? ???????????????", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }


    public void backBtn_action(){
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //???????????? ?????????????????? ????????? ??????????????? ????????? ??????
    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        if(drawerLayout.isDrawerOpen(Gravity.END)){
            drawerLayout.closeDrawer(Gravity.END);
        }else{
            super.onBackPressed();
        }
    }

    public void getPlanCount(){
        DatabaseReference databaseReference_getPlanCount = firebaseDatabase.getReference().child("users").child(user.getUid()).child("plans");
        databaseReference_getPlanCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TextView sidebarPlanCount = findViewById(R.id.sidebar_plan_count);
                sidebarPlanCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
