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
        weatherCity= view.findViewById(R.id.weatherCity); //??????
        weatherTemp= view.findViewById(R.id.weatherTemp); //??????
        weatherIcon= view.findViewById(R.id.weatherIcon); //?????? ??????????????????
        weatherDesc= view.findViewById(R.id.weatherDescription); //??????

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
        // RecyclerView??? ????????? ??????
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        mountains = new ArrayList<>(); // Plan ????????? ?????? ArrayList (????????? ?????????)
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
                        // ????????? ??????????????? ?????? ?????? ???
                        Log.e("Fraglike", String.valueOf(error.toException())); // ????????? ??????
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

    public String EnglishToKorea(String city){ //?????? ??????-> ????????????(??????,6?????? ?????????)
        if(city.equals("Seoul")) return "??????";
        if(city.equals("Busan")) return "??????";
        if(city.equals("Daegu")) return "??????";
        if(city.equals("Ulsan")) return "??????";
        if(city.equals("Gwangju")) return "??????";
        if(city.equals("Daejeon")) return "??????";
        if(city.equals("Incheon")) return "??????";

        return city;
    }

    public String EnglishToKoreaWeather(String weatherId){ //?????? ???????????? ??????-> ????????????
        String weatherKr= "";
        int[] w_id_arr = {201,200,202,210,211,212,221,230,231,232,
                300,301,302,310,311,312,313,314,321,500,
                501,502,503,504,511,520,521,522,531,600,
                601,602,611,612,615,616,620,621,622,701,
                711,721,731,741,751,761,762,771,781,800,
                801,802,803,804,900,901,902,903,904,905,
                906,951,952,953,954,955,956,957,958,959,
                960,961,962};
        String[] w_kor_arr = {"????????? ?????? ????????? ????????????","?????? ????????? ????????????","????????? ????????? ????????????","?????? ????????????",
                "????????????","?????? ????????????","???????????? ????????????","?????? ????????? ????????? ????????????","????????? ????????? ????????????",
                "?????? ???????????? ????????? ????????????","????????? ?????????","?????????","?????? ?????????","????????? ?????????","?????????",
                "?????? ?????????","???????????? ?????????","?????? ???????????? ?????????","?????????","?????? ???","?????? ???","?????? ???",
                "?????? ?????? ???","????????? ???","??????","?????? ????????? ???","????????? ???","?????? ????????? ???","???????????? ????????? ???",
                "????????? ???","???","?????? ???","????????????","????????? ????????????","?????? ?????? ???","?????? ???","?????? ????????? ???",
                "????????? ???","?????? ????????? ???","??????","??????","??????","?????? ??????","??????","??????","??????","?????????","??????",
                "????????????","?????? ??? ??? ?????? ?????? ??????","????????? ????????? ??? ??????","???????????? ????????? ??? ??????","????????? ?????? ?????? ??????",
                "???????????? ????????? ?????? ??????","????????????","??????","????????????","??????","??????","????????????","??????","????????? ?????? ??????",
                "?????? ??????","???????????? ??????","?????? ?????? ??????","????????? ??????","??? ??????","????????? ????????? ??? ??????","??????",
                "????????? ??????","??????","?????? ??????","????????????"};

        for(int i=0; i<w_id_arr.length; i++) {
            if(w_id_arr[i]==Integer.parseInt(weatherId)) {
                weatherKr= w_kor_arr[i];
                return weatherKr;
            }
        }
        return "none";
    }
}