package com.example.timetracker.fragment.statistic;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.utils.FormatRunTime;

import java.util.ArrayList;
import java.util.List;

public class StatisticListItemAdapter extends RecyclerView.Adapter<StatisticListItemAdapter.ViewHolder>{
    private List<ActivityAndRecords>  activityAndRecordsList = new ArrayList<>();
    @NonNull
    @Override
    public StatisticListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.activity_statistics_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticListItemAdapter.ViewHolder holder, int position) {
        // 绑定数据到ViewHolder
        // position是当前item的位置,从0开始
        ActivityAndRecords activityAndRecords = activityAndRecordsList.get(position);
        holder.activityName.setText(activityAndRecords.getActivity().getName());
        holder.activityTime.setText(FormatRunTime.format(activityAndRecords.getTotalTimeCost()));
        int color = Color.parseColor(activityAndRecords.getActivity().getColor());
        holder.activityColorCardView.setCardBackgroundColor(color);

        long totalCoast = 0;
        for(ActivityAndRecords activityAndRecords1 : activityAndRecordsList){
            totalCoast += activityAndRecords1.getTotalTimeCost();
        }


        if (totalCoast != 0) {
            int percentage = (int) (activityAndRecords.getTotalTimeCost() * 100 / totalCoast);
            holder.activityProgressBar.setProgress(percentage);
        }
        // 设置进度条的颜色
        holder.activityProgressBar.setProgressTintList(ColorStateList.valueOf(color));

    }

    public void setActivityAndRecordsList(List<ActivityAndRecords> activityAndRecordsList) {
        this.activityAndRecordsList = activityAndRecordsList;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return activityAndRecordsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView activityName;
        private final TextView activityTime;
        private final CardView activityColorCardView;
        private final ProgressBar activityProgressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activity_item_name);
            activityTime = itemView.findViewById(R.id.activity_item_time);
            activityColorCardView = itemView.findViewById(R.id.activity_item_color_card_view);
            activityProgressBar = itemView.findViewById(R.id.percentageBar);
        }
    }
}
