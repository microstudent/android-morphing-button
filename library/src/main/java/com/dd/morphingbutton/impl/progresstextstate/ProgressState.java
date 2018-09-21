package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularAnimatedDrawable;
import com.dd.morphingbutton.impl.CircularProgressDrawable;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class ProgressState implements ProgressTextState {

    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;

    private final CircularProgressButton mButton;

    private Drawable mDefaultDrawable;
    private int mProgressStrokeWidth;
    private String mProgressText;
    private int mColorProgress;
    private int mColorIndicator;
    private int mColorIndicatorBackground;
    private int mProgress;
    private boolean mIndeterminateProgressMode = true;
    private int mPaddingProgress;

    private CircularAnimatedDrawable mAnimatedDrawable;
    private CircularProgressDrawable mCircularProgressDrawable;

    public ProgressState(CircularProgressButton button) {
        mButton = button;
        initAttrs();
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
        mPaddingProgress = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonPaddingProgress, 0);
        attr.recycle();
    }


    public void setProgress(int progress) {
        mProgress = progress;
        mButton.invalidate();
    }

    @Override
    public void start() {
        mDefaultDrawable = mButton.getBackground();
        if (mIndeterminateProgressMode) {
            initAnimatedDrawable();
        }
        mButton.setBackgroundDrawable(mAnimatedDrawable);
        mAnimatedDrawable.start();
        mAnimatedDrawable.setAllowLoading(true);
    }

    private void initAnimatedDrawable() {
        if (mAnimatedDrawable == null) {
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mProgressStrokeWidth);
        }
    }

    @Override
    public void stop() {
        if (mDefaultDrawable != null) {
            mButton.setBackgroundDrawable(mDefaultDrawable);
        }
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable.stop();
            mAnimatedDrawable.setAllowLoading(false);
        }
    }

    @NonNull
    @Override
    public MorphingButton.Params getParams() {
        int offset = (mButton.getWidth() - mButton.getHeight()) / 2;
        int left = offset + mPaddingProgress;
        int right = mButton.getWidth() - offset - mPaddingProgress;
        int bottom = mButton.getHeight() - mPaddingProgress;
        int top = mPaddingProgress;

        return MorphingButton.Params.create()
                .width(right - left)
                .height(bottom - top)
                .text("");
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
//        if (mIndeterminateProgressMode) {
//            drawIndeterminateProgress(canvas);
//        } else {
//            drawProgress(canvas);
//        }
    }

    private void drawProgress(Canvas canvas) {

    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            initAnimatedDrawable();
        } else {
            mAnimatedDrawable.setAllowLoading(true);
            mAnimatedDrawable.draw(canvas);
        }
    }
}
