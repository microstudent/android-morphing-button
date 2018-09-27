package com.dd.morphingbutton.impl.progresstextstate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;

import hugo.weaving.DebugLog;

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
        initAttrs(button.getContext());
    }

    @DebugLog
    private void initAttrs(Context context) {
        TypedArray attr = mButton.getTypedArray();
        if (attr == null) {
            return;
        }
        mStrokeWidth = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) mButton.getContext().getResources().getDimension(
                        R.dimen.mc_cir_progress_button_stroke_width));
        mIdleText = attr.getString(R.styleable.CircularProgressButton_mcCirButtonTextIdle);
        mCornerRadius = attr.getDimension(
                R.styleable.CircularProgressButton_mcCirButtonCornerRadius, 0);
        int idleStateSelector = attr.getResourceId(
                R.styleable.CircularProgressButton_mcCirButtonSelectorIdle,
                R.color.mc_cir_progress_button_blue);
        mIdleColorState = context.getResources().getColorStateList(idleStateSelector);
        int idleStrokeColorSelector = attr.getResourceId(R.styleable.CircularProgressButton_mcCirButtonStrokeColorIdle, idleStateSelector);
        mStrokeColorIdle = context.getResources().getColorStateList(idleStrokeColorSelector);
        attr.recycle();
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
                .cornerRadius(mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_corner_radius))
                .duration(300)
                .text("安装");
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

    }
}
