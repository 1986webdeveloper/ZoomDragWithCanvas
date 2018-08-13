package com.example.acquaint.canvasexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DemoViewNew extends View {

    Paint paint;
    Rect r;

    public DemoViewNew(Context context) {
        super(context);
        paint = new Paint();
        r = new Rect(10, 10, 200, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(r, paint);

        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText("abcd", r.right / 2, r.bottom / 2, paint);
    }
}
