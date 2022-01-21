package com.example.sansinyeong;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

//클러스터 활용 자바 클래스
public class MyCluster implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;

    public MyCluster(double lat, double lng, String title, String snippet) {
        position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
