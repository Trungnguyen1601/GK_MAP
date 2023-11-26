package com.example.project_test1.Decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint paint;

    public DividerItemDecoration(Context context, int color, float strokeWidth) {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(color));
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int width = parent.getWidth();

        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom();
            int bottom = top + (int) paint.getStrokeWidth();
            c.drawRect(0, top, width, bottom, paint);
        }
    }
}
