package com.example.timetracker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TimeGridView extends View {

    private int rows = 24;
    private int cols = 12;
    private Paint paint;
    private int startRow = -1;
    private int startCol = -1;
    private int endRow = -1;
    private int endCol = -1;

    public TimeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFF000000); // 黑色
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / cols;
        int cellHeight = height / rows;

        // 绘制网格
        for (int i = 0; i <= rows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, paint);
        }
        for (int i = 0; i <= cols; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, paint);
        }

        // 绘制选中区域
        if (startRow != -1 && startCol != -1 && endRow != -1 && endCol != -1) {
            paint.setColor(0x5500FF00); // 半透明绿色
            int left = Math.min(startCol, endCol) * cellWidth;
            int top = Math.min(startRow, endRow) * cellHeight;
            int right = (Math.max(startCol, endCol) + 1) * cellWidth;
            int bottom = (Math.max(startRow, endRow) + 1) * cellHeight;
            canvas.drawRect(left, top, right, bottom, paint);
            paint.setColor(0xFF000000); // 还原颜色
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
        // 在这里处理选中的格子
        int startR = Math.min(startRow, endRow);
        int endR = Math.max(startRow, endRow);
        int startC = Math.min(startCol, endCol);
        int endC = Math.max(startCol, endCol);

        for (int i = startR; i <= endR; i++) {
            for (int j = startC; j <= endC; j++) {
                // 处理每一个选中的格子
                // 这里可以做任何需要的操作，例如保存选中的时间段等
            }
        }
    }
}

