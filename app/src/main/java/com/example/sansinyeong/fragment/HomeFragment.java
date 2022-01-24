package com.example.sansinyeong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.After_Login;
import com.example.sansinyeong.R;
import com.example.sansinyeong.Retrofit2_Client;
import com.example.sansinyeong.WeatherResponse;
import com.example.sansinyeong.service.Weather_Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    String city;
    String temp;
    String description;
    String icon;
    TextView weatherCity, weatherTemp, weatherDesc;
    ImageView weatherIcon;

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
        Log.d("fragment", "onCreate: "+city+","+temp+","+description+","+icon);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        weatherCity= view.findViewById(R.id.weatherCity); //지역
        weatherTemp= view.findViewById(R.id.weatherTemp); //온도
        weatherIcon= view.findViewById(R.id.weatherIcon); //상태 아이콘이미지
        weatherDesc= view.findViewById(R.id.weatherDescription); //상태

        weatherCity.setText(city);
        weatherTemp.setText(temp);
        weatherDesc.setText(description);

        String ImageUrl= "https://openweathermap.org/img/w/"+icon+".png";
        Glide.with(HomeFragment.this).load(ImageUrl).into(weatherIcon);
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

}