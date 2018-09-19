package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularAnimatedDrawable;
import com.dd.morphingbutton.impl.CircularProgressDrawable;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class ProgressState implements ProgressTextState {

    private final CircularProgressButton mButton;
    private CircularAnimatedDrawable mCircularAnimatedDrawable;

    private CircularProgressDrawable mCircularProgressDrawable;
    private Drawable mDefaultDrawable;
    private int mProgressStrokeWidth;
    private String mProgressText;
    private int mColorProgress;
    private int mColorIndicator;
    private int mColorIndicatorBackground;

    public ProgressState(CircularProgressButton button) {
        mButton = button;
        initAttrs();
        mCircularAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mProgressStrokeWidth);
    }

    protected int getColor(int id) {
        return mButton.getResources().getColor(id);
    }

    private void initAttrs() {
        TypedArray attr = mButton.getTypedArray();
        if (attr == null) {
            return;
        }

        int blue = getColor(R.color.mc_cir_progress_button_blue);
        int white = getColor(R.color.mc_cir_progress_button_white);
        int grey = getColor(R.color.mc_cir_progress_button_grey);
        int blank = getColor(R.color.mc_cir_progress_button_blank);

        mProgressStrokeWidth = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) mButton.getContext().getResources().getDimension(
                        R.dimen.mc_cir_progress_button_stroke_width));
        mProgressText = attr
                .getString(R.styleable.CircularProgressButton_mcCirButtonTextProgress);
        mColorProgress = attr.getColor(
                R.styleable.CircularProgressButton_mcCirButtonColorProgress, white);
        mColorIndicator = attr.getColor(
                R.styleable.CircularProgressButton_mcCirButtonColorIndicator, blue);
        mColorIndicatorBackground =
                attr.getColor(
                        R.styleable.CircularProgressButton_mcCirButtonColorIndicatorBackground,
                        blank);
        attr.recycle();
    }

    @Override
    public void start() {
        mDefaultDrawable = mButton.getBackground();
        mButton.setBackgroundDrawable(mCircularAnimatedDrawable);
        mCircularAnimatedDrawable.start();
    }

    @Override
    public void stop() {
        mCircularAnimatedDrawable.stop();
        if (mDefaultDrawable != null) {
            mButton.setBackgroundDrawable(mDefaultDrawable);
        }
    }

    @Override
    public MorphingButton.Params getParams() {
        return MorphingButton.Params.create()
                .width(80)
                .height(80)
                .text("");
    }
}
