package com.example.timetracker.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
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
        List<ActivityAndRecords> activityAndRecordsList = groupAndActivitiesAndRecords.getActivitiesAndRecords();

        holder.flexboxLayout.removeAllViews();
        if (activityAndRecordsList.isEmpty()) {
            holder.flexboxLayout.setVisibility(View.GONE);
            holder.noActivityText.setVisibility(View.VISIBLE);
        } else {
            holder.flexboxLayout.setVisibility(View.VISIBLE);
            holder.noActivityText.setVisibility(View.GONE);
        }

        long groupTotalTime = groupAndActivitiesAndRecords.getTotalTimeCost();
        holder.groupTime.setText(FormatRunTime.format(groupTotalTime));
        for (int i = 0; i < activityAndRecordsList.size(); i++) {
            ActivityAndRecords activityAndRecords = activityAndRecordsList.get(i);
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
            bindLongClickActivityEvent(ActivityItem, activity);
        }


        bindingLongClickGroupEvent(holder.itemView, groupAndActivitiesAndRecords);

    }

    // 长按群组删除群组
    private void bindingLongClickGroupEvent(View itemView, GroupAndActivitiesAndRecords groupAndActivitiesAndRecords) {

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), itemView);
                popupMenu.getMenuInflater().inflate(R.menu.group_card_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.group_delete) {
                        // 删除群组
                        showDeleteGroupDialog(itemView, groupAndActivitiesAndRecords);
                    }
                    return true;
                });

                popupMenu.show();
                return true;
            }
        });


//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // 长按事件
//                // 弹出对话框
//                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
//                builder.setTitle("删除群组");
//                builder.setMessage("确定删除" + groupAndActivitiesAndRecords.getGroup().getName() + "吗？");
//
//
//                builder.setPositiveButton("确定", (dialog, which) -> {
//                    // 删除群组
//                    Application application = (Application) itemView.getContext().getApplicationContext();
//                    GroupRepository groupRepository = new GroupRepository(application);
//                    groupRepository.deleteGroupAsync(groupAndActivitiesAndRecords.getGroup());
////                    recordRepository.deleteGroupAsync(groupAndActivitiesAndRecords.getGroup());
//                    Toast.makeText(itemView.getContext(), "删除" + groupAndActivitiesAndRecords.getGroup().getName(), Toast.LENGTH_SHORT).show();
//
//                });
//
//                // 取消按钮
//                builder.setNegativeButton("取消", (dialog, which) -> {
//                    // do nothing
//                });
//
//                builder.show();
//                return true;
//            }
//        });



    }

    // 显示确定删除对话框
    private void showDeleteGroupDialog(View itemView, GroupAndActivitiesAndRecords groupAndActivitiesAndRecords) {
        AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
        builder.setTitle("删除群组");
        builder.setMessage("确定删除" + groupAndActivitiesAndRecords.getGroup().getName() + "吗？");

        builder.setPositiveButton("确定", (dialog, which) -> {
            // 删除群组
            Application application = (Application) itemView.getContext().getApplicationContext();
            GroupRepository groupRepository = new GroupRepository(application);
            groupRepository.deleteGroupAsync(groupAndActivitiesAndRecords.getGroup());
            Toast.makeText(itemView.getContext(), "删除" + groupAndActivitiesAndRecords.getGroup().getName(), Toast.LENGTH_SHORT).show();
        });

        // 取消按钮
        builder.setNegativeButton("取消", (dialog, which) -> {
            // do nothing
        });

        builder.show();
    }

    // 删除活动的对话框
    private void showDeleteActivityDialog(View ActivityItem, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityItem.getContext());
        builder.setTitle("删除活动");
        builder.setMessage("确定删除" + activity.getName() + "吗？");

        builder.setPositiveButton("确定", (dialog, which) -> {
            // 删除活动
            Application application = (Application) ActivityItem.getContext().getApplicationContext();
            ActivityRepository activityRepository = new ActivityRepository(application);
            activityRepository.deleteActivityAsync(activity);
            Toast.makeText(ActivityItem.getContext(), "删除" + activity.getName(), Toast.LENGTH_SHORT).show();
        });

        // 取消按钮
        builder.setNegativeButton("取消", (dialog, which) -> {
            // do nothing
        });
        builder.show();
    }

    // 绑定事件，长按删除活动
    // 为了实现长按删除活动，需要在ActivityItem上绑定长按事件，然后在长按事件中调用RecordRepository的deleteRecordAsync方法
    private void bindLongClickActivityEvent(View ActivityItem, Activity activity) {

        ActivityItem.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 长按删除活动
                PopupMenu popupMenu = new PopupMenu(ActivityItem.getContext(), ActivityItem);
                popupMenu.getMenuInflater().inflate(R.menu.activity_card_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.activity_delete) {
                        // 删除活动
                        showDeleteActivityDialog(ActivityItem, activity);
                    }
                    return true;
                });

                popupMenu.show();
                return true;
            }
        });



//        ActivityItem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // 长按事件
//                // 弹出对话框
//                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityItem.getContext());
//                builder.setTitle("删除活动");
//                builder.setMessage("确定删除" + activity.getName() + "吗？");
//
//                builder.setPositiveButton("确定", (dialog, which) -> {
//                    // 删除活动
//                    Application application = (Application) ActivityItem.getContext().getApplicationContext();
//                    ActivityRepository activityRepository = new ActivityRepository(application);
//                    activityRepository.deleteActivityAsync(activity);
//                    Toast.makeText(ActivityItem.getContext(), "删除" + activity.getName(), Toast.LENGTH_SHORT).show();
//                });
//
//                // 取消按钮
//                builder.setNegativeButton("取消", (dialog, which) -> {
//                    // do nothing
//                });
//                builder.show();
//                return true;
//            }
//        });
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
