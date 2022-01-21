package com.example.sansinyeong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Mountains;

import java.util.ArrayList;

public class MountainSearchAdapter extends RecyclerView.Adapter<MountainSearchAdapter.MyViewHolder>{



    ArrayList<Mountains> mountainList;
    private OnItemClickListener mListener = null;

    public MountainSearchAdapter(ArrayList<Mountains> mountainList){
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
        TextView list_name, list_address, list_height;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.mountain_list_name);
            list_address = itemView.findViewById(R.id.mountain_list_address);
            list_height = itemView.findViewById(R.id.mountain_list_height);

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
    public MountainSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mountain_list_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MountainSearchAdapter.MyViewHolder holder, int position) {
        Mountains mountain = mountainList.get(position);

        holder.list_name.setText(mountain.getName());
        holder.list_address.setText(mountain.getAddress());
        holder.list_height.setText(mountain.getHeight().toString());

    }

    @Override
    public int getItemCount() {
        return mountainList==null ? 0 : mountainList.size();
    }
}
