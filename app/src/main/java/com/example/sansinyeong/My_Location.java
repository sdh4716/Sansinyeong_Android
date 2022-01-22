package com.example.sansinyeong;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class My_Location extends BaseActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    /*
    GPS 를 설정하지않을 경우 미국의 구글본사로 현재위치가 표시된다-> 자신의 위치를 Location 의 setLocation 으로 설정할 경우 변경됨을 확인가능
    GPS 기능을 사용해서 다른 버튼기능을 실행할때 null 으로 설정되어 에러가 발생한다-> 코드 수정필요
*/
    private GoogleMap mMap;
    private EditText address_edit;
    //private Button search;
    private Button sosBtn;

    //지오코딩-> 주소를 지도에 마커를 배치하거나 위치를 지정하는 데 사용할 수 있는 지리적 좌표로 변환하는 프로세스
    private Geocoder geocoder;

    /* */
    private static final String TAG= "googleMap";
    private static final int GPS_ENABLE_REQUEST_CODE= 2001;
    private static int UPDATE_INTERVAL_MS= 1000; //1초
    private static int FASTEST_UPDATE_INTERVAL_MS= 5000; //5초
    private Marker currentMarker= null; //현재위치표시 마커
    //onRequestPermissionsResult 에서 수신된결과에 ActivityCompat.requestPermissions 를 사용한 퍼미션요청을 구별하기 위해 사용
    private static final int PERMISSIONS_REQUEST_CODE= 100;
    boolean needRequest= false;

    //앱을 실행하기위해 필요한 퍼미션 정의

    String[] REQUIRED_PERMISSIONS= {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    //
    Location mCurrentLocation;
    LatLng currentPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private View mLayout; //Snackbar 사용하기 위한 View
    private String myLocation= ""; //위도 경도 저장할 변수

    /* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        sidebar_open();
        menu_select();
        backBtn_action();

        //위치 권한 묻기
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);

        address_edit= (EditText) findViewById(R.id.address_Edit); //검색어
//        search= (Button) findViewById(R.id.address_Search); //검색버튼
        sosBtn= findViewById(R.id.Btn_sos); //도움버튼

        /* */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mLayout= findViewById(R.id.layout_board);

//        locationRequest= new LocationRequest()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL_MS)
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        locationRequest= new LocationRequest();

        LocationSettingsRequest.Builder builder= new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        /* */


        SupportMapFragment mapFragment= (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),address_edit.getText().toString(), Toast.LENGTH_LONG).show();

                //문자 전송
                String phoneNo= "1234";
                String sms= "주소값: "+address_edit.getText().toString()+"\n";
                sms+= ","+ myLocation.toString();
                try {
                    //전송
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        });
    }

    //뒤로가기 버튼에 대한 메서드 처리
    public boolean onSupportNavigateUp(){
        onBackPressed(); // 뒤로가기 버튼이 눌렸을시
        return super.onSupportNavigateUp(); // 뒤로가기 버튼
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //맵 초기화 설정

        mMap= googleMap;
        geocoder= new Geocoder(this);

        //지도 초기위치
        setDefultLocatin();


        /* */
        //런타임 퍼미션 처리

        //위치 퍼미션 체크
        int hasFineLocationPermission= ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarsseLocationPermission= ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED&&
                hasCoarsseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            //퍼미션 있으면
            startLocationUpdates(); // 위치 업데이트시작
        }else{ //퍼미션 요청을 허용한적이 없다면 퍼미션 요청 필요
            //퍼미션 거부를 한적이 있는경우
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[0])){
                //퍼미션 필요한 이유 설명
                Snackbar.make(mLayout, "현재위치를 확인하기위해 접근권한 필요", Snackbar.LENGTH_INDEFINITE)
                        .setAction("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //퍼미션 요청-> 결과 onRequestPermissionResult 에 수신
                                ActivityCompat.requestPermissions(My_Location.this, REQUIRED_PERMISSIONS,
                                        PERMISSIONS_REQUEST_CODE);
                            }
                        }).show();
            }else{
                //사용자가 퍼미션 거부를 한적이 없는 경우 퍼미션요청
                //퍼미션 요청-> 결과 onRequestPermissionResult 에 수신
                ActivityCompat.requestPermissions(My_Location.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);

            }

        }
//        mMap.setMinZoomPreference(10.0f); //줌 최소
//        mMap.setMaxZoomPreference(20.0f); //줌 최대
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick: ");
            }
        });
        /* */

        //위험지역 표시
        firebaseList();
    }


    DatabaseReference databaseReference;
    private void firebaseList(){
        ArrayList<Dangers> dangers= new ArrayList<>();

        //파이어 베이스 값 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dangerData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //파이어베이스 데이터 ArrayList<Dangers> dangers 에 추가
                Long abc= snapshot.getChildrenCount();
                Log.d("abc", "onDataChange: "+abc);
                for(int i=0;i<abc;i++){
                    Dangers d1 = snapshot.child(String.valueOf(i)).getValue(Dangers.class);
                    dangers.add(d1);
                }
                Log.d("DangerL", "onDataChange:"+dangers);

                //커스텀 마커
                BitmapDrawable bitmapDrawable= (BitmapDrawable)getResources().getDrawable(R.drawable.fire1);
                Bitmap b= bitmapDrawable.getBitmap();
                Bitmap fireMarker= Bitmap.createScaledBitmap(b,100,100,false);

                for (int i = 0; i < dangers.size(); i++) {
                    LatLng fire = new LatLng(dangers.get(i).getLatitude(), dangers.get(i).getLongitude());
                    Log.d("Fire", "onMapReady: " + fire);
                    MarkerOptions makerFire = new MarkerOptions();
                    makerFire // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                            .position(fire)
                            .title(dangers.get(i).getLocationName())
                            .snippet("위험지역!!!"); // 타이틀.
                    makerFire.icon(BitmapDescriptorFactory.fromBitmap(fireMarker));
                    mMap.addMarker(makerFire);
//                    MyCluster myCluster = new MyCluster(dangers.get(i).getLatitude(), dangers.get(i).getLongitude(),
//                            dangers.get(i).getLocationName(), String.valueOf(dangers.get(i).getNumber()));
//                    clusterManager.setAnimation(false);
//                    clusterManager.addItem(myCluster);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /* */
    LocationCallback locationCallback= new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList= locationResult.getLocations();

            if(locationList.size()>0){
                location= locationList.get(locationList.size()-1);
                currentPosition= new LatLng(location.getLatitude(),location.getLongitude());

                String markerTitle= getCurrentAddress(currentPosition);
                String markerSnippet= "위도 :"+String.valueOf(location.getLatitude())+",경도 :"+String.valueOf(location.getLongitude());
                Log.d(TAG, "onLocationResult: "+markerSnippet); //위도 경도값 확인

                //현재위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
                mCurrentLocation= location;
            }
        }
    };

    private void startLocationUpdates(){
        if(!checkLocationServicesStatus()){
            Log.d(TAG, "startLocationUpdates: call showDialogForLocationServiceSetting()");
            showDialogForLocationServiceSetting();
        }else{
            int hasFineLocationPermission= ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission= ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);

            if(hasFineLocationPermission!= PackageManager.PERMISSION_GRANTED||
                    hasCoarseLocationPermission!= PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "startLocationUpdates: 퍼미션 없음");
                return;
            }

            Log.d(TAG, "startLocationUpdates: call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
                    Looper.myLooper());

            if(checkPermission()) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(checkPermission()){
            Log.d(TAG, "onStart: ");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            if(mMap!=null) mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mFusedLocationClient!=null){
            Log.d(TAG, "onStop: ");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    //현재 위도와 경도값의 주소값을 확인하는 메서드 지오코더 사용
    public String getCurrentAddress(LatLng latLng){
        geocoder= new Geocoder(this, Locale.getDefault());

        List<Address> addr;

        try{
            addr= geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1);
        }catch (IOException ioException){
            Toast.makeText(this,"서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            Toast.makeText(this,"서비스 사용불가", Toast.LENGTH_LONG).show();
            return "잘못된 GPS";
        }

        if(addr==null||addr.size()==0){
            Toast.makeText(this,"발견된 주소없음", Toast.LENGTH_LONG).show();
            return "발견된 주소없음";
        }else{
            Address address= addr.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet){
        if(currentMarker!= null) currentMarker.remove();

        LatLng currentLatLng= new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        address_edit.setText(markerTitle);
        myLocation= "위도: "+location.getLatitude()+", 경도: "+location.getLongitude();

        currentMarker= mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(currentLatLng,17);
        mMap.moveCamera(cameraUpdate);
    }

    //초기위치(부산)
    public void setDefultLocatin(){
        LatLng DEFAULT_LOCATION= new LatLng(35.15, 129.05);
        String markerTitle= "위치정보 없음";
        String markerSnippet= "퍼미션과 GPS 활성여부 확인 필요";

        if(currentMarker!= null) currentMarker.remove();

        //마커옵션
        MarkerOptions markerOptions= new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker= mMap.addMarker(markerOptions);


        //맵설정(위치, 줌)
        CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION,15);
        mMap.moveCamera(cameraUpdate);
    }

    //런타임 퍼미션처리 메소드
    private boolean checkPermission(){
        int hasFineLocationPermission= ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission= ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission== PackageManager.PERMISSION_GRANTED&&
                hasCoarseLocationPermission== PackageManager.PERMISSION_GRANTED){
            return true;
        }

        return false;
    }

    //ActivityCompat.requestPermissions 사용한 퍼미션 요청결과 리턴 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode== PERMISSIONS_REQUEST_CODE&&grantResults.length==
                REQUIRED_PERMISSIONS.length){
            //요청코드-> PERMISSIONS_REQUEST_CODE, 요청한 퍼미션 개수만큼 수신되었으면
            boolean check_result= true;

            //퍼미션 체크
            for(int result:grantResults){
                if(result!= PackageManager.PERMISSION_GRANTED){
                    check_result= false;
                    break;
                }
            }

            if(check_result){
                //퍼미션 허용했다면 위치업데이트 시작
                startLocationUpdates();
            }else{
                //거부한 퍼미션 있다면 앱 사용할수없는 이유 설명후 앱 종료
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1] )){
                    //사용자 거부만 선택한 경우 앱 다시 실행하여 허용
                    Snackbar.make(mLayout,"퍼미션이 거부되었습니다, 앱을 다시 실행하여 퍼미션을 허용하세요",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }else{
                    //"다시 묻지않음"을 사용자가 체크하고 거부를 선택한 경우 설정에서 퍼미션을 허용해야 앱 사용가능
                    Snackbar.make(mLayout,"퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야합니다",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    //GPS 활성화 메소드
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder= new AlertDialog.Builder(My_Location.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치서비스가 필요합니다.\n"+
                "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent=
                        new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성시켰는지 검사
                if(checkLocationServicesStatus()){
                    if(checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult: GPS가 활성화 되어있습니다");
                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

    /* */

}