package edu.sdsu.cs.assignment4;

import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by Horsie on 3/24/15.
 */
public class Circle {

    public static final String TAG = "Circle";
    private final long REPEAT_DELAY = 50;

    private Handler mIncreaseRadiusHandler = new Handler();
    private Handler mMoveCircleHandler = new Handler();

    public static final float DEFAULT_RADIUS = 30;
    private float x;
    private float y;
    private float xVelocity;
    private float yVelocity;
    private float radius;
    private boolean mKeepIncreasing;
    private boolean mKeepMoving;

    public Circle(float x, float y) {
        radius = DEFAULT_RADIUS;
        this.x = x;
        this.y = y;
        mKeepIncreasing = false;
        mKeepMoving = false;
    }

    public void increaseRadiusGradually(View view) {
        mKeepIncreasing = true;
        mIncreaseRadiusHandler.post(new RadiusUpdater(view));
    }

    public void stopIncreasingRadius() {
        mKeepIncreasing = false;
    }

    public float getRadius() {
        return radius;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public boolean contains(float x, float y) {
        return  ((x  - getX()) * (x  - getX()) + (y  - getY()) * (y  - getY()))  < (radius * radius);
    }

    public void move(View view) {
        mKeepMoving = true;
        mMoveCircleHandler.post(new MoveCircle(view));
    }

    public void stop() {
        mKeepMoving = false;
        xVelocity = 0;
        yVelocity = 0;
    }

    class RadiusUpdater implements Runnable {
        private View view;
        public RadiusUpdater(View view) {
            this.view = view;
        }
        public void run() {
            if (mKeepIncreasing) {
                radius = radius + 1;
                view.postInvalidate();
                mIncreaseRadiusHandler.postDelayed(new RadiusUpdater(view), REPEAT_DELAY);
            }
        }
    }

    class MoveCircle implements Runnable {
        private View view;
        public MoveCircle(View view) {
            this.view = view;
        }
        public void run() {
            if (mKeepMoving) {
                int screenHeight = view.getHeight();
                int screenWidth = view.getWidth();
                x += xVelocity;
                y += yVelocity;

                if ((x + radius) >= screenWidth) {
                    x = screenWidth - radius;
                    xVelocity = -1 * xVelocity;
                } else if ((x - radius) <= 0) {
                    x = radius;
                    xVelocity = -1 * xVelocity;
                }

                if ((y + radius) >= screenHeight) {
                    y = screenHeight - radius;
                    yVelocity = -1 * yVelocity;
                } else if ((y - radius) <= 0) {
                    y = radius;
                    yVelocity = -1 * yVelocity;
                }

                view.postInvalidate();
                mMoveCircleHandler.postDelayed(new MoveCircle(view), REPEAT_DELAY);
            }
        }
    }
}

/*
    dx = ball._x-ball2._x;
	dy = ball._y-ball2._y;
	collisionision_angle = Math.atan2(dy, dx);
	magnitude_1 = Math.sqrt(ball.xspeed*ball.xspeed+ball.yspeed*ball.yspeed);
	magnitude_2 = Math.sqrt(ball2.xspeed*ball2.xspeed+ball2.yspeed*ball2.yspeed);
	direction_1 = Math.atan2(ball.yspeed, ball.xspeed);
	direction_2 = Math.atan2(ball2.yspeed, ball2.xspeed);
	new_xspeed_1 = magnitude_1*Math.cos(direction_1-collisionision_angle);
	new_yspeed_1 = magnitude_1*Math.sin(direction_1-collisionision_angle);
	new_xspeed_2 = magnitude_2*Math.cos(direction_2-collisionision_angle);
	new_yspeed_2 = magnitude_2*Math.sin(direction_2-collisionision_angle);
	final_xspeed_1 = ((ball.mass-ball2.mass)*new_xspeed_1+(ball2.mass+ball2.mass)*new_xspeed_2)/(ball.mass+ball2.mass);
	final_xspeed_2 = ((ball.mass+ball.mass)*new_xspeed_1+(ball2.mass-ball.mass)*new_xspeed_2)/(ball.mass+ball2.mass);
	final_yspeed_1 = new_yspeed_1;
	final_yspeed_2 = new_yspeed_2;
	ball.xspeed = Math.cos(collisionision_angle)*final_xspeed_1+Math.cos(collisionision_angle+Math.PI/2)*final_yspeed_1;
	ball.yspeed = Math.sin(collisionision_angle)*final_xspeed_1+Math.sin(collisionision_angle+Math.PI/2)*final_yspeed_1;
	ball2.xspeed = Math.cos(collisionision_angle)*final_xspeed_2+Math.cos(collisionision_angle+Math.PI/2)*final_yspeed_2;
	ball2.yspeed = Math.sin(collisionision_angle)*final_xspeed_2+Math.sin(collisionision_angle+Math.PI/2)*final_yspeed_2;
* */