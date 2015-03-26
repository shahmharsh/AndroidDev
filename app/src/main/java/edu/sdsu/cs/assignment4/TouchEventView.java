package edu.sdsu.cs.assignment4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by Horsie on 3/24/15.
 */
public class TouchEventView extends View {
    public static final String TAG = "TouchEventView";
    private static final int MAX_CIRCLES = 20;
    private static final int VELOCITY_SCALE = 3;
    private static final float CIRCLE_STROKE_WIDTH = 3f;

    private Paint mPaint;
    private SparseArray<Circle> mActiveCircles;
    private Circle mSelectedCircle;
    private int[] colors = { Color.BLUE, Color.GREEN, Color.CYAN, Color.RED, Color.YELLOW };

    private VelocityTracker mVelocity;
    private GestureDetectorCompat mGestureDetector;

    public TouchEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.BLACK);
        mActiveCircles = new SparseArray<>(MAX_CIRCLES);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);

        mGestureDetector = new GestureDetectorCompat(context, new GestureListener(this));
    }

    /* Returns position of circle in active circle array if present, else -1 */
    private int circleAtLocation(float x, float y) {
        for (int i=0; i < mActiveCircles.size(); i++) {
            Circle circle = mActiveCircles.get(i);
            if (circle.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mActiveCircles.size(); i++) {
            Circle circle = mActiveCircles.valueAt(i);
            mPaint.setColor(colors[i % colors.length]);
            canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mVelocity = VelocityTracker.obtain();
                mVelocity.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocity.addMovement(event);
                mSelectedCircle.setX(event.getX());
                mSelectedCircle.setY(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                mSelectedCircle.stopIncreasingRadius();
                mVelocity.computeCurrentVelocity(VELOCITY_SCALE);
                mSelectedCircle.setXVelocity(mVelocity.getXVelocity());
                mSelectedCircle.setYVelocity(mVelocity.getYVelocity());
                mSelectedCircle.move(this);
                mVelocity.recycle();
                mVelocity = null;
                //mSelectedCircle = null;
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final String DEBUG_TAG = "Gestures";
        private View view;

        public GestureListener(View view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent event) {
            int position = circleAtLocation(event.getX(), event.getY());
            if (position != -1) {
                mSelectedCircle = mActiveCircles.get(position);
                mSelectedCircle.stop();
            } else {
                if (mActiveCircles.size() != MAX_CIRCLES) {
                    mSelectedCircle = new Circle(event.getX(), event.getY());
                    mActiveCircles.put(mActiveCircles.size(), mSelectedCircle);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            mSelectedCircle.increaseRadiusGradually(view);
        }
    }
}

// Reference : http://www.vogella.com/tutorials/AndroidTouch/article.html#singletouch_draw
