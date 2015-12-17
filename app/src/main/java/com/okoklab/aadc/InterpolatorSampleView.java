package com.okoklab.aadc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by amyu on 12/16/15
 */
public class InterpolatorSampleView extends View {

    private static final long DROP_BOUNCE_ANIMATOR_DURATION = 5000;

    private float mDropCircleRadius = 1000;

    private ValueAnimator mDropBounceVerticalAnimator;

    private ValueAnimator mDropBounceHorizontalAnimator;

    private Paint mPaint;

    private Paint mVerticalGraphPaint;

    private Paint mHorizontalGraphPaint;

    private Path mDropCirclePath;

    private Path mVerticalGraphPath;

    private Path mHorizontalGraphPath;

    private RectF mDropRect;

    private int mWidth;

    private int mHeight;

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener =
            new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    postInvalidate();
                }
            };

    public InterpolatorSampleView(Context context) {
        this(context, null, 0);
    }

    public InterpolatorSampleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterpolatorSampleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setUpPaint();
        setUpPath();
        resetAnimator();

        mDropRect = new RectF();
    }

    private void setUpPaint() {
        mPaint = new Paint();
        mPaint.setColor(0xff2196F3);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        mVerticalGraphPaint = new Paint();
        mVerticalGraphPaint.setColor(Color.RED);
        mVerticalGraphPaint.setStyle(Paint.Style.STROKE);
        mVerticalGraphPaint.setStrokeWidth(50);

        mHorizontalGraphPaint = new Paint();
        mHorizontalGraphPaint.setColor(Color.BLUE);
        mHorizontalGraphPaint.setStyle(Paint.Style.STROKE);
        mHorizontalGraphPaint.setStrokeWidth(50);
    }

    private void setUpPath() {
        mDropCirclePath = new Path();
        mVerticalGraphPath = new Path();
        mHorizontalGraphPath = new Path();
    }

    private void resetAnimator() {
        mDropBounceVerticalAnimator = ValueAnimator.ofFloat(0.f, 0.f);
        mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(0.f, 0.f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDropCircleRadius = w / 14.4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDropCirclePath.rewind();

        float circleCenterY = mHeight / 2.f;
        float circleCenterX = mWidth / 2.f;
        mDropRect.setEmpty();
        float vertical = (Float) mDropBounceVerticalAnimator.getAnimatedValue();
        float horizontal = (Float) mDropBounceHorizontalAnimator.getAnimatedValue();
        mDropRect.set(
                circleCenterX - mDropCircleRadius * (1 + vertical) + mDropCircleRadius * horizontal / 2,
                circleCenterY + mDropCircleRadius * (1 + horizontal) - mDropCircleRadius * vertical / 2,
                circleCenterX + mDropCircleRadius * (1 + vertical) - mDropCircleRadius * horizontal / 2,
                circleCenterY - mDropCircleRadius * (1 + horizontal) + mDropCircleRadius * vertical / 2);

        mDropCirclePath.addOval(mDropRect, Path.Direction.CCW);
        canvas.drawPath(mDropCirclePath, mPaint);
        mVerticalGraphPath.lineTo((float) ((1.0 * mDropBounceVerticalAnimator.getCurrentPlayTime() / mDropBounceVerticalAnimator.getDuration()) * mWidth), mWidth * vertical);
        mHorizontalGraphPath.lineTo((float) ((1.0 * mDropBounceVerticalAnimator.getCurrentPlayTime() / mDropBounceVerticalAnimator.getDuration()) * mWidth), mWidth * horizontal);
        canvas.drawPath(mVerticalGraphPath, mVerticalGraphPaint);
        canvas.drawPath(mHorizontalGraphPath, mHorizontalGraphPaint);
    }

    public void startAnimation() {
        mDropBounceVerticalAnimator = ValueAnimator.ofFloat(0.f, 1.f);
        mDropBounceVerticalAnimator.setDuration(DROP_BOUNCE_ANIMATOR_DURATION);
        mDropBounceVerticalAnimator.addUpdateListener(mAnimatorUpdateListener);
        mDropBounceVerticalAnimator.setInterpolator(new DropBounceInterpolator());
        mDropBounceVerticalAnimator.start();

        mDropBounceHorizontalAnimator = ValueAnimator.ofFloat(0.f, 1.f);
        mDropBounceHorizontalAnimator.setDuration(DROP_BOUNCE_ANIMATOR_DURATION);
        mDropBounceHorizontalAnimator.addUpdateListener(mAnimatorUpdateListener);
        mDropBounceHorizontalAnimator.setInterpolator(new DropBounceInterpolator());
        mDropBounceHorizontalAnimator.setStartDelay((long) (DROP_BOUNCE_ANIMATOR_DURATION * 0.25));
        mDropBounceHorizontalAnimator.start();
    }
}
