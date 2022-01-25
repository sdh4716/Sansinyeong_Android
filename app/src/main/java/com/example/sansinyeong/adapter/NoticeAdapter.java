package com.example.sansinyeong.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Notice;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<Notice> datas;

    //어댑터 생성자 생성
    public NoticeAdapter(List<Notice> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice data=datas.get(position);
        holder.nttitle.setText(data.getNttitle());
        holder.ntcontent.setText(data.getNtcontent());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder{

        private TextView nttitle;
        private TextView ntcontent;


        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            nttitle=itemView.findViewById(R.id.item_notice_title);
            ntcontent=itemView.findViewById(R.id.item_notice_content);
            itemView.findViewById(R.id.openbtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ntcontent.getVisibility()==View.GONE){
                        ntcontent.setVisibility(View.VISIBLE);

                    }else {
                        ntcontent.setVisibility(View.GONE);
                    }
                    return;
                }
            });




        }
    }
}
