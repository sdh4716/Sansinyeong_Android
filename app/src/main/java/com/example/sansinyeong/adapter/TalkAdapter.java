package com.example.sansinyeong.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.FirebaseHelper;
import com.example.sansinyeong.R;
import com.example.sansinyeong.activity.TalkActivity;
import com.example.sansinyeong.activity.WriteTalkActivity;
import com.example.sansinyeong.listener.OnPostListener;
import com.example.sansinyeong.model.TalkInfo;
import com.example.sansinyeong.view.ReadContentsVIew;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.MainViewHolder> {
    private ArrayList<TalkInfo> mDataset;
    private Activity activity;
    private FirebaseHelper firebaseHelper;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    static FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();
    private FirebaseFirestore db;




    static class MainViewHolder extends RecyclerView.ViewHolder {
        private final String TAG="MainViewHolder";
        CardView cardView;
        private String writer;
        MainViewHolder(CardView v) {
            super(v);
            cardView = v;

            //레퍼런스불러와서 작성자와 uid 비교->메뉴(수정삭제)버튼 숨기기
            CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("posts");
            collectionReference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                      writer= document.getData().get("publisher").toString();


                                }
                                Log.e(TAG,"writer:"+writer);
                                Log.e(TAG,"UID:"+mAuth.getCurrentUser().getUid());
                                if(writer.equalsIgnoreCase(mAuth.getCurrentUser().getUid())){
                                    v.findViewById(R.id.menu).setVisibility(View.VISIBLE);
                                }else{
                           v.findViewById(R.id.menu).setVisibility(View.GONE);

                        }


                            }
                        }
                    });

        }

        public void bindToPost(TalkInfo post, View.OnClickListener posts) {
        }
    }

    public TalkAdapter(Activity activity, ArrayList<TalkInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);
    }

    public void setOnPostListener(OnPostListener onPostListener){
        firebaseHelper.setOnPostListener(onPostListener);
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talk, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TalkActivity.class);
                intent.putExtra("postInfo", mDataset.get(mainViewHolder.getAdapterPosition()));
                intent.putExtra(TalkActivity.EXTRA_POST_KEY,mDataset.get(mainViewHolder.getAdapterPosition()));

                activity.startActivity(intent);

            }
        });




        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, mainViewHolder.getAdapterPosition());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder,int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);

        TalkInfo talkInfo = mDataset.get(position);
        titleTextView.setText(talkInfo.getTitle());

        ReadContentsVIew readContentsVIew = cardView.findViewById(R.id.readContentsView);
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);

       //
//        DocumentSnapshot documentSnapshot = getSnapshot(position);
//        PostInfo post=documentSnapshot.toObject(PostInfo.class);
//
//        final String postKey = documentSnapshot.getId();
//holder.itemView.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//       Intent intent=new Intent(activity,PostActivity.class);
//        intent.putExtra(PostActivity.EXTRA_POST_KEY,postKey);
//        activity.startActivity(intent);
//    }
//});


//holder.bindToPost(post, new View.OnClickListener() {
//    @Override
//    public void onClick(View starView) {
//        db.collection("posts").document(postKey).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        PostInfo post = documentSnapshot.toObject(PostInfo.class);
//
//
//                        documentSnapshot.getReference().set(post);
//                    }
//                });
//    }
//});

        if (contentsLayout.getTag() == null || !contentsLayout.getTag().equals(talkInfo)) {
            contentsLayout.setTag(talkInfo);
            contentsLayout.removeAllViews();

            readContentsVIew.setMoreIndex(MORE_INDEX);
            readContentsVIew.setPostInfo(talkInfo);

            ArrayList<SimpleExoPlayer> playerArrayList = readContentsVIew.getPlayerArrayList();
            if(playerArrayList != null){
                playerArrayListArrayList.add(playerArrayList);
            }
        }
    }
//
//    protected DocumentSnapshot getSnapshot(int index) {
//        return mSnapshots.get(index);
//    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        myStartActivity(WriteTalkActivity.class, mDataset.get(position));
                        return true;
                    case R.id.delete:
                        firebaseHelper.storageDelete(mDataset.get(position));
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.talk, popup.getMenu());
        popup.show();
    }

    private void myStartActivity(Class c, TalkInfo talkInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", talkInfo);
        activity.startActivity(intent);
    }

    public void playerStop(){
        for(int i = 0; i < playerArrayListArrayList.size(); i++){
            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
            for(int ii = 0; ii < playerArrayList.size(); ii++){
                SimpleExoPlayer player = playerArrayList.get(ii);
                if(player.getPlayWhenReady()){
                    player.setPlayWhenReady(false);
                }
            }
        }
    }


}