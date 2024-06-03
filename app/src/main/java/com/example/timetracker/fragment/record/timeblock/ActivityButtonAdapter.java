package com.example.timetracker.fragment.record.timeblock;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityButtonAdapter extends RecyclerView.Adapter<ActivityButtonAdapter.ViewHolder>{
    List<Activity> activityList = new ArrayList<>();
    private OnActivityClickListener onActivityClickListener;


    public void setOnActivityClickListener(OnActivityClickListener onActivityClickListener) {
        this.onActivityClickListener = onActivityClickListener;
    }


    @NonNull
    @Override
    public ActivityButtonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.activity_button_small_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityButtonAdapter.ViewHolder holder, int position) {
        // 绑定数据到ViewHolder
        // position是当前item的位置,从0开始
        Activity activity = activityList.get(position);
        holder.activityName.setText(activity.getName());
        holder.activityCardView.setCardBackgroundColor(Color.parseColor(activity.getColor()));
        holder.activityCardView.setOnClickListener(v -> {
            if (onActivityClickListener != null) {

                onActivityClickListener.onActivityClick(activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView activityName;
        private final CardView activityCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.activityButtonName);
            activityCardView = itemView.findViewById(R.id.activityButtonSmallCardView);
        }
    }

    public interface OnActivityClickListener {
        void onActivityClick(Activity activity);
    }
}
