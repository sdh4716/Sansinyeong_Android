package com.example.sansinyeong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MountainDetailActivity extends BaseActivity {
    ImageView img_1, img_2, img_3 ,img_chosen;
    FirebaseStorage storage;
    StorageReference storageReference;
    String m_name, m_img1, m_img2, m_img3, uri1, uri2, uri3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_detail);

        sidebar_open();
        menu_select();
        backBtn_action();


        Intent m_intent = getIntent();
        m_name = m_intent.getStringExtra("m_name");
        final String m_addr = m_intent.getStringExtra("m_addr");
        final String m_featrue = m_intent.getStringExtra("m_feature");
        final Long m_height = m_intent.getLongExtra("m_height",0L);
        m_img1 = m_intent.getStringExtra("m_img1");
        m_img2 = m_intent.getStringExtra("m_img2");
        m_img3 = m_intent.getStringExtra("m_img3");


        TextView detail_name = findViewById(R.id.mountain_detail_name);
        TextView detail_address = findViewById(R.id.mountain_detail_address);
        TextView detail_height = findViewById(R.id.mountain_detail_height);
        TextView detail_feature = findViewById(R.id.mountain_detail_feature);
        img_chosen = findViewById(R.id.mountain_detail_img_chosen);
        img_1 = findViewById(R.id.mountain_detail_img1);
        img_2 = findViewById(R.id.mountain_detail_img2);
        img_3 = findViewById(R.id.mountain_detail_img3);
        detail_name.setText(m_name);
        detail_height.setText(m_height.toString());
        detail_address.setText(m_addr);
        detail_feature.setText(m_featrue);

        uri1 = "";
        uri2 = "";
        uri3 = "";
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        load_img1();
        load_img2();
        load_img3();


        img_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MountainDetailActivity.this).load(uri1).into(img_chosen);
            }
        });

        img_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MountainDetailActivity.this).load(uri2).into(img_chosen);
            }
        });

        img_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MountainDetailActivity.this).load(uri3).into(img_chosen);
            }
        });

    }


    public void load_img1(){
        StorageReference pathReference = storageReference.child("m_img").child(m_name);
        if(pathReference == null){
            Toast.makeText(MountainDetailActivity.this,"저장소에 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference m_img = storageReference.child("m_img").child(m_name+"/"+m_img1);
            m_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MountainDetailActivity.this).load(uri).into(img_chosen);
                    Glide.with(MountainDetailActivity.this).load(uri).into(img_1);
                    uri1 = uri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public void load_img2(){
        StorageReference pathReference = storageReference.child("m_img").child(m_name);
        if(pathReference == null){
            Toast.makeText(MountainDetailActivity.this,"저장소에 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference m_img = storageReference.child("m_img").child(m_name+"/"+m_img2);
            m_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MountainDetailActivity.this).load(uri).into(img_2);
                    uri2 = uri.toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    public void load_img3(){
        StorageReference pathReference = storageReference.child("m_img").child(m_name);
        if(pathReference == null){
            Toast.makeText(MountainDetailActivity.this,"저장소에 사진이 없습니다.",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference m_img = storageReference.child("m_img").child(m_name+"/"+m_img3);
            m_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(MountainDetailActivity.this).load(uri).into(img_3);
                    uri3 = uri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}