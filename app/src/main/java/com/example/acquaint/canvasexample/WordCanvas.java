package com.example.acquaint.canvasexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class WordCanvas extends View {
    private ArrayList<Word> mWords;
    private int mAngle;
    private int mCenterX;
    private int mCenterY;
    private int mMovementX;
    private int mMovementY;
    private TextPaint mTextPaint;
    private int mInitialX;
    private int mInitialY;

    public WordCanvas(Context context) {
        super(context);
        mWords = new ArrayList<>();

        Word word1 = new Word();
        word1.x = 50;
        word1.y = 90;
        word1.text = "Hello";

        Word word2 = new Word();
        word2.x = 500;
        word2.y = 280;
        word2.text = "Words";

        Word word3 = new Word();
        word3.x = 220;
        word3.y = 600;
        word3.text = "Heh";

        mWords.add(word1);
        mWords.add(word2);
        mWords.add(word3);

        mTextPaint = new TextPaint();
        mAngle = 30;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(30);
        canvas.drawPaint(paint);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // This should be done only once, at start.
        Matrix rmatrix = new Matrix();
        rmatrix.postRotate(mAngle, centerX, centerY);
        Matrix matrix = new Matrix();
        rmatrix.invert(matrix);

        //canvas.rotate(mAngle, centerX, centerY);
        for (Word word : mWords) {
            int wordX = word.x + mMovementX;
            int wordY = word.y + mMovementY;
            float[] coords = new float[]{wordX, wordY};
            matrix.mapPoints(coords);

            canvas.drawText(word.text, coords[0], coords[1], mTextPaint);
        }

        canvas.restore();
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialX = (int) event.getX();
                mInitialY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();
                int xMovement = currentX - mInitialX;
                int yMovement = currentY - mInitialY;
                dragWords(xMovement, yMovement);
                break;
        }

        return true;
    }

    private void dragWords(int xMovement, int yMovement) {
        mMovementX = xMovement;
        mMovementY = yMovement;
        invalidate();
    }

    public class Word {
        private int x;
        private int y;
        private String text;
    }
}
