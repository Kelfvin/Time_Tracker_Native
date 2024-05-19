package com.example.timetracker.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.activity.activity_execute.ActivityExecuteActivity;
import com.example.timetracker.data.model.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {
    private List<Activity> activities = new ArrayList<>();

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder从布局中加载
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.activity_item, parent, false);

        ActivityViewHolder viewHolder = new ActivityViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        // 绑定数据到ViewHolder
        // position是当前item的位置,从0开始
        Activity activity = activities.get(position);
        holder.activityName.setText(String.format("%s",activity.getName()));
        holder.activityTime.setText("00:00");
        // 设置颜色
        // 获取颜色
        String colorCode = activity.getColor();
        if (colorCode == null || colorCode.equals("")) {
            colorCode = "#FFFFFF";
        }
        holder.activityCardView.setCardBackgroundColor(Color.parseColor(colorCode));
        // 创建只会执行一次，所以在这里设置监听器
        holder.activityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击事件
                // Toast.makeText(v.getContext(), "点击了"+viewHolder.activityName.getText(), Toast.LENGTH_SHORT).show();
                // 传递id
                Bundle bundle = new Bundle();
                bundle.putInt("activityId", activity.getId());
                Intent intent = new Intent(v.getContext(), ActivityExecuteActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //返回数据的数量
        return activities.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        private final CardView activityCardView;
        private final TextView activityName;
        private final TextView activityTime;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            activityCardView = itemView.findViewById(R.id.runningRecordCardView);
            activityName = itemView.findViewById(R.id.activityName);
            activityTime = itemView.findViewById(R.id.activityTime);
        }

    }

}
