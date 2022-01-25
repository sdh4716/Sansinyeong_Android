package com.example.sansinyeong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.BaseActivity;
import com.example.sansinyeong.FirebaseHelper;
import com.example.sansinyeong.R;
import com.example.sansinyeong.listener.OnPostListener;
import com.example.sansinyeong.model.CommentInfo;
import com.example.sansinyeong.model.TalkInfo;
import com.example.sansinyeong.model.UserInfo;
import com.example.sansinyeong.view.ReadContentsVIew;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TalkActivity extends BaseActivity {
    private TalkInfo talkInfo;
    private FirebaseHelper firebaseHelper;
    private ReadContentsVIew readContentsVIew;
    private LinearLayout contentsLayout;
    private CommentInfo comment;
    private static final String TAG = "PostActivity";
    public static final String EXTRA_POST_KEY = "post_key";
    private CommentAdapter mAdapter;

    private UserInfo userInfo;

    private DocumentReference mPostReference;
    private String mPostKey;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private EditText commentText;
    private FirebaseUser mUser;
    private Button btn_comment;
    private TextView postwriter;
    private TextView posttitle;
    private RecyclerView mCommentsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        sidebar_open();
        menu_select();
        backBtn_action();


        talkInfo = (TalkInfo) getIntent().getSerializableExtra("postInfo");

        //postUid값 받아옴
        mPostKey= talkInfo.getId();
        Log.e(TAG,"mPostKey: "+mPostKey);
        commentText=findViewById(R.id.field_comment_text);
        btn_comment=findViewById(R.id.button_post_comment);
        Log.e(TAG,"POSTKEY: "+mPostKey);

        //postuid값 받아온걸 레퍼런스에 넣어줌
        mPostReference = FirebaseFirestore.getInstance().collection("posts").document(mPostKey);

        posttitle=findViewById(R.id.POST_TITLE);
        posttitle.setText(talkInfo.getTitle());
        postwriter=findViewById(R.id.nameTextView);

        mCommentsRecycler = findViewById(R.id.recycler_comments);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommentAdapter(mPostReference.collection("post-comments"));
        mCommentsRecycler.setAdapter(mAdapter);

        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        uiUpdate();

        //해당게시물에 맞는 user값 가져와서 넣기
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef=FirebaseFirestore.getInstance().collection("users").document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo user= documentSnapshot.toObject(UserInfo.class);
                postwriter.setText("작성자 :"+user.getName());
            }
        });


//포스트 추가컬렉션 코멘트 작성
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference docRef=FirebaseFirestore.getInstance().collection("users").document(uid);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserInfo user = documentSnapshot.toObject(UserInfo.class);
                            CommentInfo comment=new CommentInfo(uid,commentText.getText().toString(),user.getName());
                            mPostReference.collection("post-comments").document().set(comment);
                            commentText.setText(null);




                    }
                });
            }
        });


    }

    //수정시 어뎁터에서 postInfo값받아와서 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    talkInfo = (TalkInfo)data.getSerializableExtra("postinfo");

                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.post, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.delete:
//                firebaseHelper.storageDelete(postInfo);
//                return true;
//            case R.id.modify:
//                myStartActivity(WritePostActivity.class, postInfo);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(TalkInfo talkInfo) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){
//        setToolbarTitle(talkInfo.getTitle());
        readContentsVIew.setPostInfo(talkInfo);
    }

    private void myStartActivity(Class c, TalkInfo talkInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", talkInfo);
        startActivityForResult(intent, 0);
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView authorView;
        public TextView bodyView;

        CommentViewHolder(View itemView) {
            super(itemView);

            authorView = itemView.findViewById(R.id.comment_author);
            bodyView = itemView.findViewById(R.id.comment_body);
        }
    }

    //private static class FirestoreAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private List<String> mCommentIds = new ArrayList<>();
        private List<CommentInfo> mComments = new ArrayList<>();

        private ListenerRegistration listenerRegistration;

        public CommentAdapter(Query query) {
            // Create child event listener
            // [START child_event_listener_recycler]
            EventListener childEventListener = new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {return;}
                    String commentKey;
                    int commentIndex;
                    CommentInfo comment;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                // A new comment has been added, add it to the displayed list
                                comment = dc.getDocument().toObject(CommentInfo.class);
                                // [START_EXCLUDE]
                                // Update RecyclerView
                                mCommentIds.add(dc.getDocument().getId());
                                mComments.add(comment);
                                notifyItemInserted(mComments.size() - 1);
                                break;
                            case MODIFIED:
                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so displayed the changed comment.
                                comment = dc.getDocument().toObject(CommentInfo.class);
                                commentKey = dc.getDocument().getId();
                                // [START_EXCLUDE]
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    // Replace with the new data
                                    mComments.set(commentIndex, comment);

                                    // Update the RecyclerView
                                    notifyItemChanged(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                                }
                                // [END_EXCLUDE]
                                break;
                            case REMOVED:
                                // A comment has changed, use the key to determine if we are displaying this
                                // comment and if so remove it.
                                commentKey = dc.getDocument().getId();
                                // [START_EXCLUDE]
                                commentIndex = mCommentIds.indexOf(commentKey);
                                if (commentIndex > -1) {
                                    // Remove data from the list
                                    mCommentIds.remove(commentIndex);
                                    mComments.remove(commentIndex);

                                    // Update the RecyclerView
                                    notifyItemRemoved(commentIndex);
                                } else {
                                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                                }
                                // [END_EXCLUDE]
                                break;
                        }
                    }

                }
            };
            // [END child_event_listener_recycler]
            listenerRegistration = query.addSnapshotListener(childEventListener);
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            CommentInfo comment = mComments.get(position);
          holder.authorView.setText(comment.author);
            holder.bodyView.setText(comment.text);
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            listenerRegistration.remove();
        }
    }
}
