package com.example.sansinyeong;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

String ActivityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName= info.get(0).topActivity;
        ActivityName = componentName.getShortClassName().substring(1).trim();


        Log.d("AcName",ActivityName);



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
                        //String의 비교는 .equals!!!!!! ==이랑 헷갈리지 말자!!!
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "MyPage":{
                                Toast.makeText(getApplicationContext(), "현재 페이지입니다", Toast.LENGTH_SHORT).show();
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
                        //String의 비교는 .equals!!!!!! ==이랑 헷갈리지 말자!!!
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), My_Location.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "My_Location":{
                                Toast.makeText(getApplicationContext(), "현재 페이지입니다", Toast.LENGTH_SHORT).show();
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

                    case R.id.item_hikingsearch: {
                        switch (ActivityName){
                            case "After_Login":{
                                Intent intent = new Intent(getApplicationContext(), Hiking_plan.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                            case "Hiking_plan":{
                                Toast.makeText(getApplicationContext(), "현재 페이지입니다", Toast.LENGTH_SHORT).show();
                                break;
                            }

                            default: {
                                Intent intent = new Intent(getApplicationContext(), Hiking_plan.class);
                                startActivity(intent);
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                break;
                            }
                        }
                        break;
                    }
                }
                return false;
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

    //뒤로가기 버튼눌렀을때 네비바 열려있으면 네비바 닫기
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
}
