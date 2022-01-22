package com.example.sansinyeong;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sansinyeong.model.Dangers;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangerZoneInert extends BaseActivity {
    EditText dangerName, dangerLatitude,dangerLongitude,dangerNumber;
    Button btn_danger;
    DatabaseReference databaseReference;
    Long number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zone_inert);

        sidebar_open();
        menu_select();
        backBtn_action();

        dangerName= findViewById(R.id.dangerName);
        dangerLatitude= findViewById(R.id.dangerLatitude);
        dangerLongitude= findViewById(R.id.dangerLongitude);
        dangerNumber= findViewById(R.id.dangerNumber);

        btn_danger= findViewById(R.id.btn_danger);
        number= 0l;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dangerData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                number+= snapshot.getChildrenCount(); //파베 전체개수 확인
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //추가버튼
        btn_danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("number", "onClick: "+number);
                if(dangerName.getText().toString().isEmpty()&&dangerLatitude.getText().toString().isEmpty()&&
                        dangerLongitude.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"값을 입력하세요", Toast.LENGTH_LONG).show();
                }else{
                    dangerInsert(number);
                }
            }
        });
    }

    private void dangerInsert(Long keyNumber){ //전체개수값 데이터 키값으로 사용하기 위해 매개변수로 전달
        //앱에 입력받은데이터 객체에 저장
        String name= dangerName.getText().toString();
        String latitude= dangerLatitude.getText().toString();
        String longitude= dangerLongitude.getText().toString();
        Integer number= Integer.valueOf(dangerNumber.getText().toString());
        Dangers dangers= new Dangers(number ,name,Double.valueOf(latitude),Double.valueOf(longitude));
        //위험정보 데이터 리얼타임데이터베이스에 추가
        databaseReference.child("dangerData").child(String.valueOf(keyNumber)).setValue(dangers);
        //  databaseReference.child("fireData").child(String.valueOf(keyNumber)).setValue(dangers); //테스트
        Toast.makeText(getApplicationContext(),"위험지역 보고가 완료되었습니다", Toast.LENGTH_LONG).show();

        Intent intent= new Intent(getApplicationContext(),My_Location.class);
        startActivity(intent);
        finish();
    }
}