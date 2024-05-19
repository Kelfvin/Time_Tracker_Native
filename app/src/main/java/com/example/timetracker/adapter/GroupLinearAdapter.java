package com.example.timetracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.Group;

import java.util.List;

public class GroupLinearAdapter extends RecyclerView.Adapter<GroupLinearAdapter.GroupLinearViewHolder>{
    private List<Group> groups;

    public void setOnGroupSelectListener(OnGroupSelectListener onGroupSelectListener) {
        this.onGroupSelectListener = onGroupSelectListener;
    }

    private OnGroupSelectListener onGroupSelectListener;




    public interface OnGroupSelectListener {
        void onGroupSelect(Group group);
    }


    @NonNull
    @Override
    public GroupLinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.group_linear_item, parent, false);
        GroupLinearViewHolder viewHolder = new GroupLinearViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupLinearViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.groupName.setText(group.getName());
        int color = 0;
        
        try {
               color = Color.parseColor(group.getColor());
                holder.groupColorCardView.setCardBackgroundColor(color);
        } catch (Exception e) {
            holder.groupColorCardView.setCardBackgroundColor(Color.parseColor("#000000"));
        }

        holder.groupItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGroupSelectListener != null) {
                    onGroupSelectListener.onGroupSelect(group);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (groups == null)
            return 0;

        return groups.size();
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public class GroupLinearViewHolder extends RecyclerView.ViewHolder {
        private CardView groupColorCardView;
        private TextView groupName;
        private LinearLayout groupItem;
        public GroupLinearViewHolder(@NonNull View itemView) {
            super(itemView);
            groupColorCardView = itemView.findViewById(R.id.group_item_color_card_view);
            groupName = itemView.findViewById(R.id.group_item_name);
            groupItem = itemView.findViewById(R.id.groupItemLinearLayout);
        }
    }
}
