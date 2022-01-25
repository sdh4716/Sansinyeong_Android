package com.example.sansinyeong.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.MountainDetailActivity;
import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Mountains;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MountainRecommendAdapter extends RecyclerView.Adapter<MountainRecommendAdapter.MyViewHolder>{
    ArrayList<Mountains> mountainList;
    private OnItemClickListener mListener = null;
    StorageReference storageReference;
    FirebaseStorage storage;

    public MountainRecommendAdapter(ArrayList<Mountains> mountainList){
        this.mountainList = mountainList;
    }

    // Adapter가 아닌 다른 엑티비티에서 OnItemClickListener를 사용하기 위해 interface 생성
    // 새로운 액티비티를 시작한다면 Intent에 실어보내면 되지만 이 RecyclerView는 보내고자 하는 액티비티 안에 있음!
    // 그래서 아이템 클릭 리스너를 임의로 만들어서 액티비티에서 사용
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView list_name, list_height, list_feature;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.recommend_name);
            list_feature = itemView.findViewById(R.id.recommend_feature);
            list_height = itemView.findViewById(R.id.recommend_height);
            image = itemView.findViewById(R.id.recommend_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(v,position);
                        }
                    }
                }
            });

        }
    }


    @NonNull
    @Override
    public MountainRecommendAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_mountain,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MountainRecommendAdapter.MyViewHolder holder, int position) {
        Mountains mountain = mountainList.get(position);

        holder.list_name.setText(mountain.getName());
        holder.list_feature.setText(mountain.getFeature());
        holder.list_height.setText(mountain.getHeight().toString()+"m");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        StorageReference pathReference = storageReference.child("m_img").child(mountain.getName());

        if(pathReference == null){
            return;
        }else{
            StorageReference m_img = storageReference.child("m_img").child(mountain.getName()+"/"+mountain.getImg1());
            m_img.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView.getContext()).load(uri).into(holder.image);
//                    uri1 = uri.toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mountainList==null ? 0 : mountainList.size();
    }




}
