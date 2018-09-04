package com.dd.morphingbutton.impl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.dd.morphingbutton.IProgress;
import com.dd.morphingbutton.MorphingButton;

public class ProgressTextButton extends MorphingButton implements IProgress {

    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;

    private int mProgress;
    private int mProgressColor;
    private int mProgressCornerRadius;
    private Paint mPaint;
    private RectF mRectF;

    public ProgressTextButton(Context context) {
        super(context);
        init();
    }

    public ProgressTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public void morphToProgress(int color, int progressColor, int progressCornerRadius, int width, int height, int duration) {
        mProgressCornerRadius = progressCornerRadius;
        mProgressColor = progressColor;

        MorphingButton.Params longRoundedSquare = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(mProgressCornerRadius)
                .width(width)
                .height(height)
                .solidColor(color)
                .colorPressed(color);
        morph(longRoundedSquare);
    }

    @Override
    public void setProgress(int progress) {

    }
}
