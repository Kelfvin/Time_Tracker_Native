package com.example.timetracker.fragment.record.timeblock;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.GroupAndActivities;

import java.util.ArrayList;
import java.util.List;

public class GroupButtonAdapter extends RecyclerView.Adapter<GroupButtonAdapter.ViewHolder>{


    private ActivityButtonAdapter.OnActivityClickListener onActivityClickListener;

    List<GroupAndActivities> groupAndActivitiesList = new ArrayList<>();

    @NonNull
    @Override
    public GroupButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.group_button_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupButtonAdapter.ViewHolder holder, int position) {

        // 绑定数据到ViewHolder
        // position是当前item的位置,从0开始
        GroupAndActivities groupAndActivities = groupAndActivitiesList.get(position);
        holder.groupName.setText(groupAndActivities.getGroup().getName());
        holder.groupCardView.setCardBackgroundColor(Color.parseColor(groupAndActivities.getGroup().getColor()));

        holder.activityRecyclerView.setLayoutManager(new LinearLayoutManager(holder.activityRecyclerView.getContext()));
        ActivityButtonAdapter activityButtonAdapter = new ActivityButtonAdapter();
        activityButtonAdapter.setOnActivityClickListener(onActivityClickListener);
        holder.activityRecyclerView.setAdapter(activityButtonAdapter);
        activityButtonAdapter.setActivityList(groupAndActivities.getActivities());


    }

    public void setGroupAndActivitiesList(List<GroupAndActivities> groupAndActivitiesList) {
        this.groupAndActivitiesList = groupAndActivitiesList;
    }

    @Override
    public int getItemCount() {
        return groupAndActivitiesList.size();
    }

    public void setOnActivityClickListener(ActivityButtonAdapter.OnActivityClickListener onActivityClickListener) {
        this.onActivityClickListener = onActivityClickListener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView groupName;
        private final RecyclerView activityRecyclerView;
        private final CardView groupCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupButtonName);
            activityRecyclerView = itemView.findViewById(R.id.groupActivityButtonList);
            groupCardView = itemView.findViewById(R.id.groupButtonCardView);
        }
    }
}
