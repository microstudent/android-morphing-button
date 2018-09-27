package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class IdleState implements ProgressTextState {

    private final CircularProgressButton mButton;
    private int mStrokeWidth;
    private String mIdleText;
    private float mCornerRadius;
    private ColorStateList mIdleColorState;
    private ColorStateList mStrokeColorIdle;
    private ColorStateList mTextColor;

    public IdleState(CircularProgressButton button) {
        mButton = button;
    }

    @Override
    public void initAttrs(TypedArray typedArray) {
        if (typedArray == null) {
            return;
        }
        mStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) mButton.getContext().getResources().getDimension(
                        R.dimen.mc_cir_progress_button_progress_stroke_width));
        mIdleText = typedArray.getString(R.styleable.CircularProgressButton_mcCirButtonTextIdle);
        mCornerRadius = typedArray.getDimension(
                R.styleable.CircularProgressButton_mcCirButtonCornerRadius, mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_corner_radius));
        int idleStateSelector = typedArray.getResourceId(
                R.styleable.CircularProgressButton_mcCirButtonSelectorIdle,
                R.color.mc_cir_progress_button_blue);
        mIdleColorState = mButton.getResources().getColorStateList(idleStateSelector);
        int idleStrokeColorSelector = typedArray.getResourceId(R.styleable.CircularProgressButton_mcCirButtonStrokeColorIdle, idleStateSelector);
        mStrokeColorIdle = mButton.getResources().getColorStateList(idleStrokeColorSelector);
    }

    @Override
    public void start() {
        //do nothing...
    }

    @Override
    public void stop() {
        //do nothing...
    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams
                .create()
                .textColor(Color.WHITE)
                .solidColor(mIdleColorState)
                .strokeWidth(mStrokeWidth)
                .width(mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_width))
                .height(mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_height))
                .backgroundWidth(mButton.getWidth())
                .cornerRadius((int) mCornerRadius)
                .text(mIdleText);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }

    public void setColorState(ColorStateList idleColorState) {
        mIdleColorState = idleColorState;
    }

    public void setStrokeColorIdle(ColorStateList strokeColorIdle) {
        mStrokeColorIdle = strokeColorIdle;
    }

    public void setTextColor(ColorStateList colorStateList) {
        mTextColor = colorStateList;
    }

    public void setText(String text) {
        mIdleText = text;
    }
}
