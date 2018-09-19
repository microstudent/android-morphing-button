package com.dd.morphingbutton.impl;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class CircularProgressDrawable extends Drawable {
    private float mSweepAngle;
    private float mStartAngle;
    private int mSize;
    private int mStrokeWidth;
    private int mStrokeColor;
    private Drawable mCenterIcon;
    private Paint mIconPaint;
    private boolean mShouldIcon = false;
    private RectF mRectF;
    private Paint mPaint;
    private Path mPath;

    public CircularProgressDrawable(int size, int strokeWidth, int strokeColor) {
        this.mSize = size;
        this.mStrokeWidth = strokeWidth;
        this.mStrokeColor = strokeColor;
        this.mStartAngle = 90.0F;
        this.mSweepAngle = 0.0F;
    }

    public void setStartAngle(float startAngle) {
        this.mStartAngle = startAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.mSweepAngle = sweepAngle;
    }

    public int getSize() {
        return this.mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = this.getBounds();
        if (this.mPath == null) {
            this.mPath = new Path();
        }

        this.mPath.reset();
        this.mPath.addArc(this.getRect(), this.mStartAngle, this.mSweepAngle);
        this.mPath.offset((float)bounds.left, (float)bounds.top);
        canvas.drawPath(this.mPath, this.createPaint());
        if (this.mCenterIcon != null) {
            canvas.save();
            this.mCenterIcon.setBounds(0, 0, this.mCenterIcon.getIntrinsicWidth(), this.mCenterIcon.getIntrinsicHeight());
            canvas.translate((float)(bounds.left + this.getSize() / 2 - this.mCenterIcon.getIntrinsicWidth() / 2), (float)(bounds.top + this.getSize() / 2 - this.mCenterIcon.getIntrinsicHeight() / 2));
            this.mCenterIcon.draw(canvas);
            canvas.restore();
        } else {
            if (!this.mShouldIcon) {
                return;
            }

            if (this.mIconPaint == null) {
                this.mIconPaint = new Paint();
                this.mIconPaint.setStrokeCap(Paint.Cap.ROUND);
                this.mIconPaint.setColor(this.mStrokeColor);
                this.mIconPaint.setAntiAlias(true);
            }

            int height = this.getSize();
            int width = this.getSize();
            int lineWidth = this.mStrokeWidth;
            int lineHeight = (int)((float)height / 3.1F);
            int lineGap = (int)(0.12D * (double)width);
            this.mIconPaint.setStrokeWidth((float)lineWidth);
            canvas.drawLine((float)(bounds.left + width / 2 - lineGap / 2 - lineWidth / 2), (float)(bounds.top + height / 2 - lineHeight / 2 + lineWidth / 2), (float)(bounds.left + width / 2 - lineGap / 2 - lineWidth / 2), (float)(bounds.top + height / 2 + lineHeight / 2 - lineWidth / 2), this.mIconPaint);
            canvas.drawLine((float)(bounds.left + width / 2 + lineGap / 2 + lineWidth / 2), (float)(bounds.top + height / 2 - lineHeight / 2 + lineWidth / 2), (float)(bounds.left + width / 2 + lineGap / 2 + lineWidth / 2), (float)(bounds.top + height / 2 + lineHeight / 2 - lineWidth / 2), this.mIconPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    private RectF getRect() {
        if (this.mRectF == null) {
            float index = (float)this.mStrokeWidth / 2.0F;
            this.mRectF = new RectF(index, index, (float)this.getSize() - index, (float)this.getSize() - index);
        }

        return this.mRectF;
    }

    private Paint createPaint() {
        if (this.mPaint == null) {
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth((float)this.mStrokeWidth);
            this.mPaint.setColor(this.mStrokeColor);
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        }

        return this.mPaint;
    }

    public void setCenterIcon(Drawable centerIcon) {
        this.mCenterIcon = centerIcon;
    }

    public void setShowCenterIcon(boolean showCenterIcon) {
        this.mShouldIcon = showCenterIcon;
    }

    public void setIndicatorColor(int color) {
        this.createPaint();
        this.mStrokeColor = color;
        this.mPaint.setColor(this.mStrokeColor);
    }

    public void setStrokeWidth(int width) {
        if (width > 0 && this.mStrokeWidth != width) {
            this.mStrokeWidth = width;
            if (this.mRectF != null) {
                int index = this.mStrokeWidth / 2;
                this.mRectF.set((float)index, (float)index, (float)(this.getSize() - index), (float)(this.getSize() - index));
            }

            if (this.mPaint != null) {
                this.mPaint.setStrokeWidth((float)this.mStrokeWidth);
            }
        }

    }
}
