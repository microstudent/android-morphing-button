package com.dd.morphingbutton.impl.progresstextstate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;

import hugo.weaving.DebugLog;

public class CompleteState implements ProgressTextState {

    private final CircularProgressButton mButton;
    private String mCompleteText;
    private int mIconComplete;
    private float mCornerRadius;
    private ColorStateList mTextColorComplete;
    private int mCompleteColor;
    private int mCompleteStrokeColor;
    private int mStrokeWidth;

    public CompleteState(CircularProgressButton button) {
        mButton = button;
        initAttrs(mButton.getContext());
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @DebugLog
    private void initAttrs(Context context) {
        TypedArray attr = mButton.getTypedArray();
        if (attr == null) {
            return;
        }
        mCompleteText = attr
                .getString(R.styleable.CircularProgressButton_mcCirButtonTextComplete);
        mIconComplete = attr.getResourceId(
                R.styleable.CircularProgressButton_mcCirButtonIconComplete, 0);
        mCornerRadius = attr.getDimension(
                R.styleable.CircularProgressButton_mcCirButtonCornerRadius, 0);
        mCompleteColor = attr.getColor(
                R.styleable.CircularProgressButton_mcCirButtonCompleteColor,
                context.getResources().getColor(R.color.mc_cir_progress_button_white));
        mCompleteStrokeColor = attr.getColor(R.styleable.CircularProgressButton_mcCirButtonStrokeColorComplete,
                context.getResources().getColor(R.color.mc_cir_progress_button_green));

        mStrokeWidth = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) context.getResources().getDimension(
                        R.dimen.mc_cir_progress_button_stroke_width));

        mTextColorComplete = attr.getColorStateList(R.styleable.CircularProgressButton_mcCirButtonTextColorComplete);
        if (mTextColorComplete == null) {
            mTextColorComplete = mButton.getTextColors();
        }

        attr.recycle();
    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams.create()
                .backgroundWidth(mButton.getWidth())
                .solidColor(mCompleteColor)
                .strokeWidth(mStrokeWidth)
                .strokeColor(mCompleteStrokeColor)
                .textColor(mTextColorComplete.getDefaultColor())
                .text("完成");
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
