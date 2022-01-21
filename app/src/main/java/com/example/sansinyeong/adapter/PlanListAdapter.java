package com.example.sansinyeong.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sansinyeong.R;
import com.example.sansinyeong.model.Plan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {

    ArrayList<Plan> planList;
    Long dday;

    public PlanListAdapter(ArrayList<Plan> planList){
        this.planList = planList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView dDay, mountain, dayStart, dayEnd, bak, members, D;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dDay = itemView.findViewById(R.id.list_item_Dday);
            mountain = itemView.findViewById(R.id.list_item_mountain);
            dayStart = itemView.findViewById(R.id.list_item_day_start);
            dayEnd = itemView.findViewById(R.id.list_item_day_end);
            bak = itemView.findViewById(R.id.list_item_bak);
            members = itemView.findViewById(R.id.list_item_members);
            D = itemView.findViewById(R.id.list_item_D);
            dday = 0L;

        }
    }

    @NonNull
    @Override
    public PlanListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiking_plan_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanListAdapter.ViewHolder holder, int position) {
        Plan plan = planList.get(position);
        try{
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        Date getTime_date = sdf.parse(getTime);
        String startDate = plan.getStartdate();
        Date startDate_date = sdf.parse(startDate);
        long calDate = startDate_date.getTime() - getTime_date.getTime();
        Long dday = calDate / (24*60*60*1000);
            if (dday < 0){
                holder.dDay.setText("day가 지났습니다");
            }else if (dday == 0){
                holder.dDay.setText("day");
                holder.dDay.setTextColor(0xAAef484a);
                holder.D.setTextColor(0xAAef484a);
            }else{
                holder.dDay.setText(dday.toString());
            }
        }catch (ParseException e){

        }

        holder.mountain.setText(plan.getMountain());
        holder.dayStart.setText(plan.getStartdate());
        holder.dayEnd.setText(plan.getEnddate());
        holder.bak.setText(plan.getBak());
        holder.members.setText(plan.getMembers());
    }

    @Override
    public int getItemCount() {
        return planList==null ? 0 : planList.size();
    }
}
