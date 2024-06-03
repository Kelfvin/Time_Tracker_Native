package com.example.timetracker.fragment.record.timeblock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeGridView extends View {

    private int rows = 24;
    private int cols = 12;
    private Paint paint;
    private int startRow = -1;
    private int startCol = -1;
    private int endRow = -1;
    private int endCol = -1;

    private List<ActivityAndRecords> activityAndRecordsList;


    private OnTimeSelectedListener onTimeSelectedListener;

    public TimeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
        this.onTimeSelectedListener = onTimeSelectedListener;
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF000000); // 黑色
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawRecordedTime(canvas);
        // 绘制选中的格子
        drawSelectedCells(canvas);
        drawTimeCursor(canvas);
    }

    private void drawBackground(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / cols;
        int cellHeight = height / rows;

        // 绘制一个一个的格子
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int left = j * cellWidth;
                int top = i * cellHeight;
                int right = left + cellWidth;
                int bottom = top + cellHeight;
                // 浅灰色
                paint.setColor(Color.parseColor("#cbe4ff"));
                // 绘制圆角矩形
                // 绘制padding的圆角矩形
                float padding = 1;
                canvas.drawRoundRect(left + padding, top + padding, right - padding, bottom - padding, 10, 10, paint);
            }
        }
    }

    private void drawOneRecord(Canvas canvas, String name, int startHour, int startMinute, int endHour, int endMinute,String color) {
        float padding = 1;

        int width = getWidth();
        int height = getHeight();
        int cellHeight = getCellHeight();

        int x = startMinute * width / 60;
        int y = (startHour + 1) * cellHeight;

        paint.setColor(Color.parseColor(color));
        paint.setStrokeWidth(5);

        // 每一个事件画一个圆角矩形，如果是跨小时的，就要绘制多行
        // 如果是同一小时的
        RectF rectF;
        if (startHour == endHour) {
            int x1 = startMinute * width / 60;
            int y1 = startHour * cellHeight;
            int x2 = endMinute * width / 60;
            int y2 = (endHour + 1) * cellHeight;

            rectF = new RectF(x1 + padding, y1 + padding, x2 - padding, y2 - padding);
            canvas.drawRoundRect(rectF, 10, 10, paint);
        } else {

            // 如果是跨小时的
            // 首先绘制起点那一行，从起点到该行的末尾
            int x1 = startMinute * width / 60;
            int y1 = startHour * cellHeight;
            int x2 = width;
            int y2 = (startHour + 1) * cellHeight;
            rectF = new RectF(x1 + padding, y1 + padding, x2 - padding, y2 - padding);
            canvas.drawRoundRect(rectF, 10, 10, paint);

            // 绘制中间的行，全部选中
            for (int i = startHour + 1; i < endHour; i++) {
                int x3 = 0;
                int y3 = i * cellHeight;
                int x4 = width;
                int y4 = (i + 1) * cellHeight;
                RectF rectF1 = new RectF(x3 + padding, y3 + padding, x4 - padding, y4 - padding);
                canvas.drawRoundRect(rectF1, 10, 10, paint);
            }

            // 绘制终点那一行，从0到终点
            int x5 = 0;
            int y5 = endHour * cellHeight;
            int x6 = endMinute * width / 60;
            int y6 = (endHour + 1) * cellHeight;
            RectF rectF2 = new RectF(x5 + padding, y5 + padding, x6 - padding, y6 - padding);
            canvas.drawRoundRect(rectF2, 10, 10, paint);
        }

        // 绘制记录的名称
        // 计算矩形中心
        float centerY = rectF.centerY();
        float startX = rectF.left + 10;

        // 获取文字的宽度和高度
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(30);
        textPaint.setColor(0xFFFFFFFF);
        float textWidth = textPaint.measureText(name);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;

        // 判断文字是否超出矩形的宽度
        if (textWidth <= rectF.width()) {
            // 绘制文字在矩形的中心
            float textBaseY = centerY - fontMetrics.ascent / 2;
            canvas.drawText(name, startX, textBaseY, textPaint);
        }

    }

    private void drawRecordedTime(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        if (activityAndRecordsList == null) {
            return;
        }

        for (ActivityAndRecords activityAndRecords : activityAndRecordsList) {
            for (Record record : activityAndRecords.getRecords()) {
                Calendar startTime = record.getStartTime();
                Calendar endTime = record.getEndTime();
                if (endTime == null) {
                    endTime = Calendar.getInstance();
                }

                String name = activityAndRecords.getActivity().getName();
                long startHour = startTime.get(Calendar.HOUR_OF_DAY);
                long startMinute = startTime.get(Calendar.MINUTE);
                long endHour = endTime.get(Calendar.HOUR_OF_DAY);
                long endMinute = endTime.get(Calendar.MINUTE);
                String color = activityAndRecords.getActivity().getColor();
                drawOneRecord(canvas, name, (int) startHour, (int) startMinute, (int) endHour, (int) endMinute,color);
            }
        }

    }

    private void drawTimeCursor(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 获取当前的时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 绘制时间光标，红色
        paint.setColor(0xFFFF0000);
        paint.setStrokeWidth(5);
        int x = width * minute / 60;
        int y = height * hour / 24;
        canvas.drawLine(x, y, x, y + height / 24, paint);

    }

    private int getCellWidth() {
        return getWidth() / cols;
    }

    private int getCellHeight() {
        return getHeight() / rows;
    }

    private void drawSelectedCells(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / cols;
        int cellHeight = height / rows;

        int drawStartRow = Math.min(startRow, endRow);
        int drawEndRow = Math.max(startRow, endRow);
        int drawStartCol = Math.min(startCol, endCol);
        int drawEndCol = Math.max(startCol, endCol);
        if (startRow != -1 && startCol != -1 && endRow != -1 && endCol != -1) {
            float padding = 1;
            // 透明度为0.3的灰色
            paint.setColor(0x4D808080);
            // 如果起点和终点在同一行
            if (drawStartRow == drawEndRow) {
                for (int j = drawStartCol; j <= drawEndCol; j++) {
                    int left = j * cellWidth;
                    int top = drawStartRow * cellHeight;
                    int right = left + cellWidth;
                    int bottom = top + cellHeight;
                    canvas.drawRoundRect(left + padding, top + padding, right - padding, bottom - padding, 10, 10, paint);
                }
                return;
            }

            // 先绘制起点的那一行，那一行到末尾全部选中
            for (int j = drawStartCol; j < cols; j++) {
                int left = j * cellWidth;
                int top = drawStartRow * cellHeight;
                int right = left + cellWidth;
                int bottom = top + cellHeight;
                canvas.drawRoundRect(left + padding, top + padding, right - padding, bottom - padding, 10, 10, paint);
            }

            // 绘制中间的行，中间的行全部选中
            for (int i = drawStartRow + 1; i < drawEndRow; i++) {
                for (int j = 0; j < cols; j++) {
                    int left = j * cellWidth;
                    int top = i * cellHeight;
                    int right = left + cellWidth;
                    int bottom = top + cellHeight;
                    canvas.drawRoundRect(left + padding, top + padding, right - padding, bottom - padding, 10, 10, paint);
                }
            }
            // 绘制终点的那一行，那一行到终点全部选中
            for (int j = 0; j <= drawEndCol; j++) {
                int left = j * cellWidth;
                int top = drawEndRow * cellHeight;
                int right = left + cellWidth;
                int bottom = top + cellHeight;
                canvas.drawRoundRect(left + padding, top + padding, right - padding, bottom - padding, 10, 10, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / cols;
        int cellHeight = height / rows;

        int row = (int) (event.getY() / cellHeight);
        int col = (int) (event.getX() / cellWidth);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // 如果已经选中了格子，就清空
                if (startRow != -1 && startCol != -1 && endRow != -1 && endCol != -1) {
                    startRow = -1;
                    startCol = -1;
                    endRow = -1;
                    endCol = -1;
                    invalidate();
                    return true;
                }

                startRow = row;
                startCol = col;
                endRow = row;
                endCol = col;
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE:
                endRow = row;
                endCol = col;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                endRow = row;
                endCol = col;
                invalidate();
                // 处理选中的格子
                handleSelectedCells();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleSelectedCells() {

        // 如果有-1，说明没有选中格子
        if (startRow == -1 || startCol == -1 || endRow == -1 || endCol == -1) {
            onTimeSelectedListener.onTimeSelected(-1, -1, -1, -1);
            return;
        }


        // 确保结束时间大于开始时间
        int rightStartRow = Math.min(startRow, endRow);
        int rightEndRow = Math.max(startRow, endRow);
        int rightStartCol = Math.min(startCol, endCol);
        int rightEndCol = Math.max(startCol, endCol);

        onTimeSelectedListener.onTimeSelected(rightStartRow, rightStartCol * 5, rightEndRow, (rightEndCol+1) * 5);

    }

    public void clearSelectedCells() {
        startRow = -1;
        startCol = -1;
        endRow = -1;
        endCol = -1;
        invalidate();
    }

    public void setActivityAndRecordsList(List<ActivityAndRecords> activityAndRecords) {
        this.activityAndRecordsList = activityAndRecords;
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int startHour, int startMinute, int endHour, int endMinute);
    }
}

