package com.dd.morphingbutton.impl;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

public class CircularAnimatedDrawable extends Drawable implements Animatable {
    private final long LOADING_ANIM_DURATION = 1760L;
    public static final String START_ANGLE_PROPERTY = "startAngle";
    public static final String SWEEP_ANGLE_PROPERTY = "sweepAngle";
    private final RectF fBounds = new RectF();
    private Paint mPaint;
    private float mBorderWidth;
    private boolean mRunning;
    private Animator mLoadingAnimator = null;
    private float mStartAngle;
    private float mSweepAngle;
    private boolean mAllowLoading = true;

    public CircularAnimatedDrawable(int color, float borderWidth) {
        this.mBorderWidth = borderWidth;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(borderWidth);
        this.mPaint.setColor(color);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mLoadingAnimator = this.createLoadingAnimator();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(this.fBounds, this.mStartAngle, this.mSweepAngle, false, this.mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.fBounds.left = (float)bounds.left + this.mBorderWidth / 2.0F;
        this.fBounds.right = (float)bounds.right - this.mBorderWidth / 2.0F;
        this.fBounds.top = (float)bounds.top + this.mBorderWidth / 2.0F;
        this.fBounds.bottom = (float)bounds.bottom - this.mBorderWidth / 2.0F;
    }

    @Override
    public void start() {
        if (!this.isRunning()) {
            this.mRunning = true;
            this.mLoadingAnimator.start();
            this.invalidateSelf();
        }
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            this.mRunning = false;
            this.mLoadingAnimator.cancel();
            this.invalidateSelf();
        }
    }

    @Override
    public boolean isRunning() {
        return this.mRunning;
    }

    private Animator createLoadingAnimator() {
        Keyframe key1 = Keyframe.ofFloat(0.0F, -90.0F);
        Keyframe key2 = Keyframe.ofFloat(0.5F, 330.0F);
        Keyframe key3 = Keyframe.ofFloat(1.0F, 630.0F);
        PropertyValuesHolder pvhStart = PropertyValuesHolder.ofKeyframe("startAngle", new Keyframe[]{key1, key2, key3});
        PropertyValuesHolder pvhSweep = PropertyValuesHolder.ofFloat("sweepAngle", new float[]{0.0F, -120.0F, 0.0F});
        ObjectAnimator loadingAnim = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{pvhStart, pvhSweep});
        loadingAnim.setDuration(1760L);
        loadingAnim.setInterpolator(new LinearInterpolator());
        loadingAnim.setRepeatCount(-1);
        return loadingAnim;
    }

    public float getSweepAngle() {
        return this.mSweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.mSweepAngle = sweepAngle;
        if (this.mAllowLoading) {
            this.invalidateSelf();
        }

    }

    public float getStartAngle() {
        return this.mStartAngle;
    }

    public void setStartAngle(float startAngle) {
        this.mStartAngle = startAngle;
        if (this.mAllowLoading) {
            this.invalidateSelf();
        }
    }

    public void setAllowLoading(boolean allow) {
        this.mAllowLoading = allow;
    }

    public void setIndicatorColor(int color) {
        if (this.mPaint != null) {
            this.mPaint.setColor(color);
        }

    }

    public void setStrokeWidth(int width) {
        if (width > 0 && (int)this.mBorderWidth != width) {
            this.mBorderWidth = (float)width;
            this.onBoundsChange(this.getBounds());
            if (this.mPaint != null) {
                this.mPaint.setStrokeWidth(this.mBorderWidth);
            }
        }

    }
}
