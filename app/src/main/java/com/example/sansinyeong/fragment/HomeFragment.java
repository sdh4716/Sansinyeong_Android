package com.example.sansinyeong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.After_Login;
import com.example.sansinyeong.MountainDetailActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.Retrofit2_Client;
import com.example.sansinyeong.WeatherResponse;
import com.example.sansinyeong.adapter.MountainRecommendAdapter;
import com.example.sansinyeong.adapter.MountainSearchAdapter;
import com.example.sansinyeong.adapter.PlanListAdapter;
import com.example.sansinyeong.model.Mountains;
import com.example.sansinyeong.model.Plan;
import com.example.sansinyeong.service.Weather_Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    String city, temp, description, icon, weatherId;
    TextView weatherCity, weatherTemp, weatherDesc, notice_title, notice_content;
    ImageView weatherIcon;

    private ArrayList<Mountains> mountains_List;
    Mountains mountain;

    private RecyclerView recyclerView;
    private MountainRecommendAdapter mountainRecommendAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Mountains> mountains;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference ,databaseReference_notice;

    public HomeFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data= getArguments();
        city= data.getString("city");
        temp= data.getString("temp");
        description= data.getString("description");
        icon= data.getString("icon");
        weatherId= data.getString("weatherId");
        Log.d("fragment", "onCreate: "+city+","+temp+","+description+","+icon+","+weatherId);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherCity= view.findViewById(R.id.weatherCity); //지역
        weatherTemp= view.findViewById(R.id.weatherTemp); //온도
        weatherIcon= view.findViewById(R.id.weatherIcon); //상태 아이콘이미지
        weatherDesc= view.findViewById(R.id.weatherDescription); //상태

        notice_title = view.findViewById(R.id.home_notice_title);
        notice_content = view.findViewById(R.id.home_notice_content);

        String cityKr= EnglishToKorea(city);
        weatherCity.setText(cityKr);
        weatherTemp.setText(temp);
        String weatherKr= EnglishToKoreaWeather(weatherId);
        weatherDesc.setText(weatherKr);

        String ImageUrl= "https://openweathermap.org/img/w/"+icon+".png";
        Glide.with(HomeFragment.this).load(ImageUrl).into(weatherIcon);

        recyclerView = view.findViewById(R.id.rv_recommend);
        recyclerView.setHasFixedSize(true);
        // RecyclerView를 가로로 표시
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mountains = new ArrayList<>(); // Plan 객체를 담을 ArrayList (어댑터 쪽으로)
        mountainRecommendAdapter = new MountainRecommendAdapter(mountains);
        recyclerView.setAdapter(mountainRecommendAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("mountain_recommend");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mountains.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            Mountains mountain = ds.getValue(Mountains.class);
                            mountains.add(mountain);
                        }
                        mountainRecommendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // 디비를 가져오던중 에러 발생 시
                        Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
                    }
                });

            }
        },400);

        mountainRecommendAdapter.setOnItemClickListener(new MountainRecommendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mountain = mountains.get(position);
                Intent intent = new Intent(v.getContext(), MountainDetailActivity.class);
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



//        databaseReference_notice = firebaseDatabase.getReference("notice_main");
//        databaseReference_notice.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String title = snapshot.
//                notice_title.setText(title);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    public String EnglishToKorea(String city){ //도시 영어-> 한글변환(서울,6개의 광역시)
        if(city.equals("Seoul")) return "서울";
        if(city.equals("Busan")) return "부산";
        if(city.equals("Daegu")) return "대구";
        if(city.equals("Ulsan")) return "울산";
        if(city.equals("Gwangju")) return "광주";
        if(city.equals("Daejeon")) return "대전";
        if(city.equals("Incheon")) return "인천";

        return city;
    }

    public String EnglishToKoreaWeather(String weatherId){ //날시 상세정보 영어-> 한글변환
        String weatherKr= "";
        int[] w_id_arr = {201,200,202,210,211,212,221,230,231,232,
                300,301,302,310,311,312,313,314,321,500,
                501,502,503,504,511,520,521,522,531,600,
                601,602,611,612,615,616,620,621,622,701,
                711,721,731,741,751,761,762,771,781,800,
                801,802,803,804,900,901,902,903,904,905,
                906,951,952,953,954,955,956,957,958,959,
                960,961,962};
        String[] w_kor_arr = {"가벼운 비를 동반한 천둥구름","비를 동반한 천둥구름","폭우를 동반한 천둥구름","약한 천둥구름",
                "천둥구름","강한 천둥구름","불규칙적 천둥구름","약한 연무를 동반한 천둥구름","연무를 동반한 천둥구름",
                "강한 안개비를 동반한 천둥구름","가벼운 안개비","안개비","강한 안개비","가벼운 적은비","적은비",
                "강한 적은비","소나기와 안개비","강한 소나기와 안개비","소나기","악한 비","중간 비","강한 비",
                "매우 강한 비","극심한 비","우박","약한 소나기 비","소나기 비","강한 소나기 비","불규칙적 소나기 비",
                "가벼운 눈","눈","강한 눈","진눈깨비","소나기 진눈깨비","약한 비와 눈","비와 눈","약한 소나기 눈",
                "소나기 눈","강한 소나기 눈","박무","연기","연무","모래 먼지","안개","모래","먼지","화산재","돌풍",
                "토네이도","구름 한 점 없는 맑은 하늘","약간의 구름이 낀 하늘","드문드문 구름이 낀 하늘","구름이 거의 없는 하늘",
                "구름으로 뒤덮인 흐린 하늘","토네이도","태풍","허리케인","한랭","고온","바람부는","우박","바람이 거의 없는",
                "약한 바람","부드러운 바람","중간 세기 바람","신선한 바람","센 바람","돌풍에 가까운 센 바람","돌풍",
                "심각한 돌풍","폭풍","강한 폭풍","허리케인"};

        for(int i=0; i<w_id_arr.length; i++) {
            if(w_id_arr[i]==Integer.parseInt(weatherId)) {
                weatherKr= w_kor_arr[i];
                return weatherKr;
            }
        }
        return "none";
    }
}