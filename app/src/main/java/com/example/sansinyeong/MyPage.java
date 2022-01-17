package com.example.sansinyeong;

import android.os.Bundle;

public class MyPage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        sidebar_open();
        menu_select();
        backBtn_action();
    }


}