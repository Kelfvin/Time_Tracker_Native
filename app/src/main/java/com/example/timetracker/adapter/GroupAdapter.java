package com.example.timetracker.adapter;

import android.app.Application;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;
import com.example.timetracker.data.repository.RecordRepository;
import com.example.timetracker.utils.FormatRunTime;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<GroupAndActivitiesAndRecords> groupAndActivitiesRecordsList = new ArrayList<>();

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder从布局中加载
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.group_item, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        // 绑定数据到ViewHolder
        // position是当前item的位置,从0开始
        GroupAndActivitiesAndRecords groupAndActivitiesAndRecords = groupAndActivitiesRecordsList.get(position);
        holder.groupName.setText(groupAndActivitiesAndRecords.getGroup().getName());
        holder.groupTime.setText("00:00");
        holder.groupColorCardView.setCardBackgroundColor(Color.parseColor(groupAndActivitiesAndRecords.getGroup().getColor()));

        // 动态绑定flexbox布局，动态设置活动按钮
        holder.flexboxLayout.removeAllViews();

        if (groupAndActivitiesAndRecords.getActivitiesAndRecords().size() == 0) {
            holder.flexboxLayout.setVisibility(View.GONE);
            holder.noActivityText.setVisibility(View.VISIBLE);
        } else {
            holder.flexboxLayout.setVisibility(View.VISIBLE);
            holder.noActivityText.setVisibility(View.GONE);
        }

        long groupTotalTime = groupAndActivitiesAndRecords.getTotalTimeCost();
        holder.groupTime.setText(FormatRunTime.format(groupTotalTime));


        for (int i = 0; i < groupAndActivitiesAndRecords.getActivitiesAndRecords().size(); i++) {
            ActivityAndRecords activityAndRecords = groupAndActivitiesAndRecords.getActivitiesAndRecords().get(i);
            Activity activity = activityAndRecords.getActivity();
            View ActivityItem = LayoutInflater.from(holder.flexboxLayout.getContext()).inflate(R.layout.activity_item, holder.flexboxLayout, false);
            long activityTotalTime = activityAndRecords.getTotalTimeCost();
            TextView activityTime = ActivityItem.findViewById(R.id.activityTime);
            activityTime.setText(FormatRunTime.format(activityTotalTime));
            ActivityItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 点击事件
                    Application application = (Application) holder.flexboxLayout.getContext().getApplicationContext();
                    RecordRepository recordRepository = new RecordRepository(application);
                    recordRepository.startRecordAsync(activity);
                    Toast.makeText(holder.flexboxLayout.getContext(), "开始记录" + activity.getName(), Toast.LENGTH_SHORT).show();
                }
            });

            CardView cardView = ActivityItem.findViewById(R.id.activityItemCardView);
            cardView.setCardBackgroundColor(Color.parseColor(activity.getColor()));

            TextView activityName = ActivityItem.findViewById(R.id.activityName);
            activityName.setText(activity.getName());
            holder.flexboxLayout.addView(ActivityItem);
        }
    }

    @Override
    public int getItemCount() {
        return groupAndActivitiesRecordsList.size();
    }

    public void setGroupAndActivitiesRecordsList(List<GroupAndActivitiesAndRecords> groupAndActivitiesAndRecordsList) {
        this.groupAndActivitiesRecordsList = groupAndActivitiesAndRecordsList;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private final TextView groupName;
        private final TextView groupTime;

        private final TextView noActivityText;

        private final CardView groupColorCardView;

        private final FlexboxLayout flexboxLayout;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_item_name);
            groupTime = itemView.findViewById(R.id.group_item_time);
            flexboxLayout = itemView.findViewById(R.id.activityFlexboxLayout);
            noActivityText = itemView.findViewById(R.id.no_activity_text_view);
            groupColorCardView = itemView.findViewById(R.id.group_item_color_card_view);
        }


    }
}
