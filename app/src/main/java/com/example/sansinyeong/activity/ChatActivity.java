package com.example.sansinyeong.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.BaseActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.model.ChatData;
import com.example.sansinyeong.model.FibaseID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatActivity extends BaseActivity {

    private RecyclerView listView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;
    private Button sendButton;
//    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference databaseReference = firebaseDatabase.getReference();
private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private String userName;
//    private ArrayAdapter adapter;
    private List<ChatData> mData;
    private ChatAdapter mAdapter;
    private Date timestamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listView = (RecyclerView) findViewById(R.id.listview1);
        editText = (EditText) findViewById(R.id.chat_editText);
        sendButton = (Button) findViewById(R.id.chat_button);

        //리사이클러뷰 레이아웃매니저 필수
        mLayoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);

        userName = "user" + new Random().nextInt(10000);

        mData=new ArrayList<>();
        listView.setAdapter(mAdapter);
        sidebar_open();
        menu_select();
        backBtn_action();

        //리얼타임데이터베이스 값넣기
//        sendButton.setOnClickListener((view) -> {
//            ChatData chatData = new ChatData(userName, editText.getText().toString());  // 유저 이름과 메세지로 chatData 만들기
//            databaseReference.child("message").push().setValue(chatData);  // 기본 database 하위 message라는 child에 chatData를 list로 만들기
//            editText.setText("");
//
//
//        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long now=System.currentTimeMillis();
                timestamp=new Date(now);
                SimpleDateFormat sdf=new SimpleDateFormat("MM-dd--HH:mm:ss");
                String getTime=sdf.format(timestamp);
                Map<String,Object> data=new HashMap<>();
                data.put(FibaseID.chatname,userName);
                data.put(FibaseID.chatbody,editText.getText().toString());
                data.put(FibaseID.timestamp,getTime);
                mStore.collection("chat").document().set(data);
                editText.setText(null);
                onStart();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mData=new ArrayList<>();
        mStore.collection("chat").orderBy("timestamp", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           if(task.getResult()!=null){
                               for(DocumentSnapshot snap:task.getResult()){
                                   Map<String,Object> shot=snap.getData();
                                   String username=String.valueOf(shot.get(FibaseID.chatname));
                                   String message=String.valueOf(shot.get(FibaseID.chatbody));


                                   ChatData data=new ChatData(username,message,timestamp);
                                   mData.add(data);
                               }
                               mAdapter=new ChatAdapter(mData);

                               listView.setAdapter(mAdapter);


                           }
                       }
                    }
                });
    }

//리얼타임 데이터베이스 값가져오기
//        databaseReference.child("message").addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                ChatData chatData = dataSnapshot.getValue(ChatData.class);  // chatData를 가져오고
//                mAdapter.add(chatData.getUserName() + ": " + chatData.getMessage());  // adapter에 추가합니다.
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) { }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//
//        });
//
//
//
//
//


//    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }


    private static class ChatViewHolder extends RecyclerView.ViewHolder{
    public TextView chat_nameView;
    public TextView chat_bodyView;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        chat_nameView=itemView.findViewById(R.id.chat_name);
        chat_bodyView=itemView.findViewById(R.id.chat_body);
    }
}


    private static class ChatAdapter extends  RecyclerView.Adapter<ChatViewHolder>{


        private List<ChatData> mChats;
        private ListenerRegistration listenerRegistration;

        public ChatAdapter(List<ChatData> mChats){this.mChats=mChats;}



        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

            ChatData data=mChats.get(position);
            holder.chat_nameView.setText(data.getUserName());
            holder.chat_bodyView.setText(data.getMessage());
        }

        @Override
        public int getItemCount() {
            return mChats.size();
        }
    }

}