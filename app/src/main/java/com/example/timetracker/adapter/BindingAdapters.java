package com.example.timetracker.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;

import com.example.timetracker.R;
import com.example.timetracker.utils.FormatRunTime;

public class BindingAdapters {

    @BindingAdapter({"cardBackgroundColor"})
    public static void setCardBackgroundColor(CardView view, String color) {
        if (color != null) {
            try {
                view.setCardBackgroundColor(Color.parseColor(color));
            } catch (IllegalArgumentException e) {
                // 如果颜色字符串格式不正确，使用默认颜色
                view.setCardBackgroundColor(Color.parseColor("#000000"));
            }
        } else {
            // 如果颜色字符串为空，使用默认颜色
            view.setCardBackgroundColor(Color.parseColor("#000000"));
        }
    }


}
