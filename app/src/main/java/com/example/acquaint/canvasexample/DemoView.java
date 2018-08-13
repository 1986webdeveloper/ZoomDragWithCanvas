package com.example.acquaint.canvasexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.RelativeLayout;

public class DemoView extends View implements ScaleGestureDetector.OnScaleGestureListener {

    private static final String TAG = DemoView.class.getSimpleName();
    private static final int INVALID_POINTER_ID = -1;
    private ScaleGestureDetector gestureScale;
    private float scaleFactor = 1;
    private boolean inScale;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private float mPosX = 350;
    private float mPosY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private boolean isInitialized;
    private Canvas mCanvas;
    private float maxLeft, maxRight, maxTop, maxBottom;
    private float heightOfText, widthOfText;
    private Paint paint;
    private Rect rect;
    private Rect parentRect;

    public DemoView(Context context, RelativeLayout relativeLayout) {
        super(context);
        gestureScale = new ScaleGestureDetector(context, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        parentRect = new Rect();
        mCanvas.getClipBounds(parentRect);
        maxLeft = parentRect.left;
        maxTop = parentRect.top;
        maxRight = parentRect.right;
        maxBottom = parentRect.bottom;

        // custom drawing code here
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        // make the entire canvas white
        paint.setColor(Color.GRAY);
        canvas.drawPaint(paint);

        String txt = getContext().getString(R.string.txt_graphics_rotation);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(scaleFactor, scaleFactor);

        rect = new Rect();
        paint.getTextBounds(txt, 0, txt.length(), rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        Log.e(TAG, "onDraw: rect : " + rect.left + " : " + rect.top + " : " + rect.right + " : " + rect.bottom);
        Paint paint1 = new Paint();
        paint1.setColor(Color.BLUE);
        paint1.setStrokeWidth(5);
        paint1.setStyle(Paint.Style.STROKE);
//        canvas.drawRect(rect, paint1);
        canvas.save();

        TextPaint textPaint = new TextPaint();
        textPaint.set(paint);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StaticLayout staticLayout = StaticLayout.Builder
                    .obtain(txt, 0, txt.length(), textPaint, parentRect.width())
                    .setBreakStrategy(Layout.BREAK_STRATEGY_BALANCED)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setIncludePad(true)
                    .build();
            if (staticLayout.getLineCount() == 1) {
                widthOfText = textPaint.measureText(txt);
                heightOfText = -textPaint.ascent() + textPaint.descent();
            } else {
                widthOfText = staticLayout.getWidth();
                heightOfText = (staticLayout.getHeight());
            }
          //  float textHeight = getTextHeight(txt, textPaint);
            int numberOfTextLines = staticLayout.getLineCount();
            float textYCoordinate = rect.exactCenterY() -
                    ((numberOfTextLines * heightOfText) / 2);

            //text will be drawn from left
            float textXCoordinate = rect.left;
            canvas.translate(0, heightOfText);
            staticLayout.draw(canvas);
            canvas.drawRect(maxLeft, maxTop, widthOfText, heightOfText, paint1);
            canvas.restore();
        } else {
            canvas.drawText(txt, rect.left, rect.top / 2, paint);
            widthOfText = paint.measureText(txt) / 2;
            heightOfText = rect.height();
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e(TAG, "onSizeChanged: " + h + ":" + w);
        maxBottom = h - 40;
        maxRight = w - 10;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureScale.onTouchEvent(event);

        if (event.getPointerCount() == 1) {
            float[] bounds = new float[4];
            // LEFT
            bounds[0] = event.getX() + rect.left;
            if (bounds[0] < maxLeft) {
                bounds[0] = maxLeft;

            }
            // RIGHT
            bounds[2] = bounds[0] + rect.right;
            if (bounds[2] > maxRight) {
                bounds[2] = maxRight;
                bounds[0] = bounds[2] - rect.right;
            }
            // TOP
            bounds[1] = event.getY() + rect.top;
            if (bounds[1] < maxTop) {
                bounds[1] = maxTop;
            }
            // BOTTOM
            bounds[3] = bounds[1] + rect.bottom;
            if (bounds[3] > maxBottom) {
                bounds[3] = maxBottom;
                bounds[1] = bounds[3] - heightOfText;
            }


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    final float x = event.getX();
                    final float y = event.getY();

                    // Remember where we started
                    mLastTouchX = x;
                    mLastTouchY = y;
                    mActivePointerId = event.getPointerId(0);
                    break;
                }

                case MotionEvent.ACTION_MOVE:
                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!gestureScale.isInProgress()) {
                        try {
                            final int pointerIndex = event.findPointerIndex(mActivePointerId);
                            final float x = event.getX(pointerIndex);
                            final float y = event.getY(pointerIndex);

                            mPosX = bounds[0];
                            mPosY = bounds[1];
                            invalidate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_POINTER_UP: {
                    // Extract the index of the pointer that left the touch sensor
                    final int pointerIndex1 = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    final int pointerId = event.getPointerId(pointerIndex1);
                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex1 == 0 ? 1 : 0;
                        mLastTouchX = event.getX(newPointerIndex);
                        mLastTouchY = event.getY(newPointerIndex);
                        mActivePointerId = event.getPointerId(newPointerIndex);
                    }
                    break;
                }
            }

        }
        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = (scaleFactor < 1 ? 1 : scaleFactor); // prevent our view from becoming too small //
        scaleFactor = ((float) ((int) (scaleFactor * 100))) / 100; // Change precision to help with jitter when user just rests their fingers //
        paint.setTextSize(scaleFactor);
        invalidate();
//        this.setScaleX(scaleFactor);
//        this.setScaleY(scaleFactor);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        inScale = true;
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        inScale = false;
    }


}