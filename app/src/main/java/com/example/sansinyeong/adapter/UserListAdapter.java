package com.example.sansinyeong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Plan;
import com.example.sansinyeong.model.UserInfo;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    ArrayList<UserInfo> userList;

    public UserListAdapter(ArrayList<UserInfo> userList){
        this.userList = userList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username, email;
        ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.list_user_name);
            email = itemView.findViewById(R.id.list_user_email);
            picture = itemView.findViewById(R.id.list_user_picture);

        }
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {

        UserInfo userInfo = userList.get(position);
        holder.username.setText(userInfo.getName());
        holder.email.setText(userInfo.getEmail());
        if (userInfo.getPhotoUrl()!=null){
            Glide.with(holder.itemView.getContext()).load(userInfo.getPhotoUrl()).centerCrop().override(500).into(holder.picture);
        }
    }

    @Override
    public int getItemCount() {
        return userList==null ? 0 : userList.size();
    }
}
