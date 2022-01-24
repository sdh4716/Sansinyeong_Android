package com.example.sansinyeong;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.fragment.HomeFragment;
import com.example.sansinyeong.fragment.PlanFragment;
import com.example.sansinyeong.model.WeatherXml;
import com.example.sansinyeong.service.Weather_Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class After_Login extends BaseActivity {
    private static final String TAG = "After_Login";

    //날씨
    Weather_Service weather_service;
    private final static String appKey = "ea144e7c98e106ef4f26fb9f731be176";
    TextView weatherCity, weatherTemp, weatherDesc;
    ImageView weatherIcon;
    WeatherXml data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        weatherCity= findViewById(R.id.weatherCity); //지역
        weatherTemp= findViewById(R.id.weatherTemp); //온도
        weatherIcon= findViewById(R.id.weatherIcon); //상태 아이콘이미지
        weatherDesc= findViewById(R.id.weatherDescription); //상태

        init();
        sidebar_open();
        menu_select();
        backBtn_action();

        //퍼미션 메세지 권한 허용 여부 확인
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        //위치 권한 묻기
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);

        sidebar_info();
        getCurrentWeather();
    }


    
    // After_Login 페이지로 진입했을때 User가 있다면 User Collection의 Uid를 가져온다 + 회원정보가 없으면 MemberInitActivity를 띄워줌
    // User가 없다면 회원가입 액티비티로 이동한다
    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            myStartActivity(SignUpActivity.class);
        } else {
            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(firebaseUser.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MemberInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, homeFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.home:
//                        HomeFragment homeFragment = new HomeFragment();
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, homeFragment)
//                                .commit();
//                        return true;
                    case R.id.nav_plan:
                        PlanFragment planFragment = new PlanFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_main, planFragment)
                                .commit();
                        return true;
//                    case R.id.friends:
//                        UserListFragment userListFragment = new UserListFragment();
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, userListFragment)
//                                .commit();
//                        return true;
                }
                return false;
            }
        });
    }

//        WeatherThread();


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    //날짜 정보 데이터
    public void getCurrentWeather() {
        //openWeatherApi 사용 레트로핏 클라이언트 생성및 호출(인터페이스 Weather_Service)
        weather_service= Retrofit2_Client.getInstance().getItemService();

        Call<WeatherResponse> weather1= weather_service.CurrentWeather(
                String.valueOf(35.098622772659986),
                String.valueOf(129.03688048507343)
                ,appKey,"metric");

        weather1.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                Log.d("Weather", "Success:"+response.body().toString());
                WeatherResponse weatherResponse= response.body();

                weatherCity.setText(weatherResponse.name);
                weatherTemp.setText(String.format("%.0f",weatherResponse.main.getTemp())+"°C");
                weatherDesc.setText(weatherResponse.weather.get(0).getDescription());

                //현재날씨에 대한 이미지 가져오기
                String ImageUrl= "https://openweathermap.org/img/w/"+weatherResponse.weather.get(0).getIcon()+".png";
                Log.d("ImageUrl", "onResponse: "+ImageUrl);
                Glide.with(After_Login.this).load(ImageUrl).into(weatherIcon);
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });


    }

    private void WeatherThread(){

          new Thread(new Runnable() {
            @Override
            public void run() {
                data=getXmlData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherCity.setText("도시이름");
                    }
                });
            }
        }).start();

    }

    private WeatherXml getXmlData(){
        StringBuffer buffer=new StringBuffer();
        WeatherXml weatherXml= new WeatherXml();;

        String queryUrl="https://www.weather.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108";
        try{
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();//xml파싱을 위한
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag="";
            xpp.next();
            int eventType= xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//테그 이름 얻어오기
                        if (xpp.equals("location")) {
                            //객체 생성
                        }
                        break;

                    case XmlPullParser.TEXT:
                        switch(tag) {
                            case "city":{
//                                Log.d(TAG, "getXmlData:"+weatherXm);
                                break;
                            }
                            case "tmEf":{
//                                weatherXmlData.setTmEf(xpp.getText());
                                break;
                            }
                            case "wf":{
//                                weatherXmlData.setWf(xpp.getText());
                                break;
                            }
                            case "tmn":{
//                                weatherXmlData.setTmn(xpp.getText());
                                break;
                            }
                            case "tmx":{
//                                weatherXmlData.setTmx(xpp.getText());
                                break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //테그 이름 얻어오기
                        if(tag.equals("location"))
                            break;// 첫번째 검색결과종료..줄바꿈
                }

                eventType= xpp.next();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return weatherXml;//StringBuffer 문자열 객체 반환
    }
}
