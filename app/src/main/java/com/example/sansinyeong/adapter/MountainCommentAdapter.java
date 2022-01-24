package com.example.sansinyeong.adapter;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.MountainDetailActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.Util;
import com.example.sansinyeong.model.MountainComment;
import com.example.sansinyeong.model.Plan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MountainCommentAdapter extends RecyclerView.Adapter<MountainCommentAdapter.ViewHolder>{
    ArrayList<MountainComment> commentList;
    private OnItemLongClickListener mListener = null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public MountainCommentAdapter(ArrayList<MountainComment> commentList){
        this.commentList = commentList;
    }

    // Adapter가 아닌 다른 엑티비티에서 OnItemClickListener를 사용하기 위해 interface 생성
    // 새로운 액티비티를 시작한다면 Intent에 실어보내면 되지만 이 RecyclerView는 보내고자 하는 액티비티 안에 있음!
    // 그래서 아이템 클릭 리스너를 임의로 만들어서 액티비티에서 사용
    public interface OnItemLongClickListener{
        void onItemLongClick(View v, int position);
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.mListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username, content, star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.m_comment_username);
            content = itemView.findViewById(R.id.m_comment_content);
            star = itemView.findViewById(R.id.m_comment_star);


//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position = getAdapterPosition();
//                    if(position != RecyclerView.NO_POSITION){
//                        if(mListener != null){
//                            mListener.onItemLongClick(v,position);
//                        }
//                    }
//                    return true;
//                }
//            });



        }
    }

    @NonNull
    @Override
    public MountainCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mountain_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MountainCommentAdapter.ViewHolder holder, int position) {
        MountainComment comment = commentList.get(position);

        holder.username.setText(comment.getUsername());
        holder.content.setText(comment.getContent());
        holder.star.setText(comment.getStar().toString());

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference_add = firebaseDatabase.getReference();

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("uid_comment",comment.getUid());
                Log.d("uid_user",user.getUid());
                if (comment.getUid().equals(user.getUid()) ){
                    AlertDialog.Builder builder = new AlertDialog.Builder((MountainDetailActivity)MountainDetailActivity.D_context);
                    builder.setTitle("평가 삭제");
                    builder.setMessage("정말 평가를 삭제하시겠습니까?");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference_add.child("mountain_comments").child(MountainDetailActivity.m_name).child(user.getUid()).removeValue();
                        }
                    });
                    builder.setNeutralButton("취소",null);
                    builder.create().show();
                }else{
                    return false;
                }
                return true;
            }
        });





    }

    @Override
    public int getItemCount() {
        return commentList==null ? 0 : commentList.size();
    }

}
