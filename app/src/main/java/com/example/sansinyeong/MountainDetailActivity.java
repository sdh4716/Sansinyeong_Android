package com.example.sansinyeong;

import static com.example.sansinyeong.Util.showToast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.adapter.MountainCommentAdapter;
import com.example.sansinyeong.adapter.PlanListAdapter;
import com.example.sansinyeong.model.MountainComment;
import com.example.sansinyeong.model.Mountains;
import com.example.sansinyeong.model.Plan;
import com.example.sansinyeong.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MountainDetailActivity extends BaseActivity {
    ImageView img_1, img_2, img_3 ,img_chosen, bookmark, btn_recommend;
    FirebaseStorage storage;
    StorageReference storageReference;
    String m_img1, m_img2, m_img3, uri1, uri2, uri3, current_user_name, flag, m_addr, m_featrue;
    Long m_height;
    public static String m_name;
    TextView tv_rating;
    RatingBar ratingbar;
    Button btn_add;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference_add, databaseReference_bookmark;
    private FirebaseUser user;
    private Long counting_star;
    private EditText edt_comment;
    private RecyclerView recyclerView;
    private MountainCommentAdapter mountainCommentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MountainComment> mountainComments;
    public static Context D_context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_detail);

        sidebar_open();
        menu_select();
        backBtn_action();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPlanCount();
            }
        },400);
        D_context = this;


        Intent m_intent = getIntent();
        m_name = m_intent.getStringExtra("m_name");
        m_addr = m_intent.getStringExtra("m_addr");
        m_featrue = m_intent.getStringExtra("m_feature");
        m_height = m_intent.getLongExtra("m_height",0L);
        m_img1 = m_intent.getStringExtra("m_img1");
        m_img2 = m_intent.getStringExtra("m_img2");
        m_img3 = m_intent.getStringExtra("m_img3");


        btn_add = findViewById(R.id.mountain_detail_addComment);
        TextView detail_name = findViewById(R.id.mountain_detail_name);
        TextView detail_address = findViewById(R.id.mountain_detail_address);
        TextView detail_height = findViewById(R.id.mountain_detail_height);
        TextView detail_feature = findViewById(R.id.mountain_detail_feature);
        TextView tv_rating = findViewById(R.id.mountain_detail_rating);
        bookmark = findViewById(R.id.iv_mountain_bookmark);

        edt_comment = findViewById(R.id.mountain_detail_comment);
        img_chosen = findViewById(R.id.mountain_detail_img_chosen);
        img_1 = findViewById(R.id.mountain_detail_img1);
        img_2 = findViewById(R.id.mountain_detail_img2);
        img_3 = findViewById(R.id.mountain_detail_img3);
        btn_recommend = findViewById(R.id.btn_recommend_add);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference_add = firebaseDatabase.getReference();
        databaseReference_bookmark = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        current_user_name = "";
        counting_star = 0L;
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    current_user_name = document.getData().get("name").toString();

                } else {
                    Log.d("MountainDetailActivity", "get failed with ", task.getException());
                }
            }
        });

        bookmark_check();
        adminChk();



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

        ratingbar = findViewById(R.id.mountain_detail_ratingbar);


        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv_rating.setText(rating + "점");
                counting_star = (long)rating;
//                Log.d("rating",counting_star.toString());
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting_star == 0){
                    showToast(MountainDetailActivity.this,"별점을 선택하세요");
                }else if(edt_comment.getText().toString().equals("")){
                    showToast(MountainDetailActivity.this,"댓글을 입력하세요");
                }else{
                    add_comment(current_user_name,edt_comment.getText().toString(),counting_star,user.getUid());
                    showToast(MountainDetailActivity.this,"평가가 등록되었습니다.");
                    edt_comment.setText("");
                    ratingbar.setRating(0);
                    counting_star = 0L;
                }
            }
        });

        recyclerViewInitSetting();
        Log.d("m_name",m_name);



        DatabaseReference databaseReference_get = firebaseDatabase.getReference().child("mountain_comments").child(m_name);
        databaseReference_get.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mountainComments.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        MountainComment comment = ds.getValue(MountainComment.class);
                        mountainComments.add(comment);
                    }
                    mountainCommentAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
            }
        });

        Log.d("mountainComments",mountainComments.toString());

        // 댓글 변화를 감지해 리사이클러뷰를 갱신
        DatabaseReference databaseReference_update = firebaseDatabase.getReference().child("mountain_comments").child(m_name);
        databaseReference_update.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mountainComments.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    MountainComment comment = ds.getValue(MountainComment.class);
                    mountainComments.add(comment);
                }
                mountainCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == "off"){
                    add_bookmark(m_name,m_addr,m_height,m_featrue,m_img1,m_img2,m_img3);
                    bookmark.setImageResource(R.drawable.ic_community_good);
                    flag = "on";
                    showToast(MountainDetailActivity.this,"북마크에 추가되었습니다.");
                }else{
                    remove_bookmark();
                    bookmark.setImageResource(R.drawable.ic_community_good_off);
                    flag = "off";
                    showToast(MountainDetailActivity.this,"북마크에서 제거되었습니다.");
                }

            }
        });

        btn_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_recommend(m_name,m_addr,m_height,m_featrue,m_img1,m_img2,m_img3);
            }
        });

    }

    //----------------------OnCreate() 끝-----------------------------------

    // 억지로 구현한 이미지 로딩... 비효율적인 코드입니다..

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

    public void add_bookmark(String name, String address, Long height, String feature, String img1, String img2, String img3){

        Mountains mountains = new Mountains(name,address,height,feature,img1,img2,img3);
        databaseReference_bookmark.child("users").child(user.getUid()).child("mountain_bookmark").child(m_name).setValue(mountains);
    }

    public void remove_bookmark(){
        databaseReference_bookmark.child("users").child(user.getUid()).child("mountain_bookmark").child(m_name).removeValue();
    }

    // 북마크의 존재여부 확인!!
    public void bookmark_check(){
        databaseReference_bookmark.child("users").child(user.getUid()).child("mountain_bookmark").child(m_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    // 데이터가 있을 경우 북마크 버튼이 눌러진 상태로 액티비티 시작
                    flag = "on";
                    bookmark.setImageResource(R.drawable.ic_community_good);
                }else{
                    // 데이터가 없을 경우 flag를 off로 놔둬 버튼이 눌러질 수 있게끔 함
                    flag = "off";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //값을 파이어베이스 Realtime database로 넘기는 함수
    public void add_comment(String username, String content, Long star, String uid){

        //Plan.java에서 선언했던 함수
        MountainComment m_comment = new MountainComment(username,content,star,uid);
        databaseReference_add.child("mountain_comments").child(m_name).child(user.getUid()).setValue(m_comment);

        // .child로 타고 타고 내려감
        // .push()는 상위 키값을 랜덤으로 설정해 주는 함수 (블로그 왈 채팅기능 만들때 사용하면 좋답니다)
        // .setValue로 Plan 클래스에서 지정한 값을 자동으로 넣어줌

    }

    public void recyclerViewInitSetting(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_mountain_detail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mountainComments = new ArrayList<>(); // Plan 객체를 담을 ArrayList (어댑터 쪽으로)
        mountainCommentAdapter = new MountainCommentAdapter(mountainComments);
        recyclerView.setAdapter(mountainCommentAdapter);

    }

    public void adminChk(){
        if (firebaseAuth.getCurrentUser().getUid().equals("y7w9lcQ27ASPADsbcG1egJQnlZz1")){
    btn_recommend.setVisibility(View.VISIBLE);
        }
    }

    public void add_recommend(String name, String address, Long height, String feature, String img1, String img2, String img3){

        Mountains mountains = new Mountains(name,address,height,feature,img1,img2,img3);
        databaseReference_bookmark.child("mountain_recommend").child(m_name).setValue(mountains);
    }
}