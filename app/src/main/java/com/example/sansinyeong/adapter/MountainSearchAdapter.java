package com.example.sansinyeong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Mountains;

import java.util.List;

public class MountainSearchAdapter extends RecyclerView.Adapter<MountainSearchAdapter.MyViewHolder>{
    private List<Mountains> mountainList;

    public MountainSearchAdapter(List<Mountains> mountainList){
        this.mountainList = mountainList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView list_name, list_address, list_height;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.mountain_list_name);
            list_address = itemView.findViewById(R.id.mountain_list_address);
            list_height = itemView.findViewById(R.id.mountain_list_height);
        }
    }


    @NonNull
    @Override
    public MountainSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mountain_list,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MountainSearchAdapter.MyViewHolder holder, int position) {
        Mountains mountain = mountainList.get(position);
        String name = mountain.getName();
        String address = mountain.getAddress();
        String height = mountain.getHeight();

        holder.list_name.setText(name);
        holder.list_address.setText(address);
        holder.list_height.setText(height);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
