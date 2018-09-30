package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class CompleteState extends AbsProgressTextState {

    private final CircularProgressButton mButton;
    private String mCompleteText;
//    private int mIconComplete;
    private float mCornerRadius;
    private ColorStateList mTextColorComplete;
    private ColorStateList mCompleteColorState;
    private ColorStateList mStrokeColorComplete;
    private int mStrokeWidth;
    private TextPaint mTextPaint;
    private StaticLayout mTextLayout;

    public CompleteState(CircularProgressButton button) {
        mButton = button;
        initTextPaint();
    }

    private void initTextPaint() {
        mTextPaint = mButton.getPaint();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public void setColorState(ColorStateList completeColorState) {
        mCompleteColorState = completeColorState;
    }

    public void setStrokeColor(ColorStateList strokeColorComplete) {
        mStrokeColorComplete = strokeColorComplete;
    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams.create()
                .backgroundWidth(mButton.getWidth())
                .solidColor(mCompleteColorState)
                .strokeWidth(mStrokeWidth)
                .strokeColor(mStrokeColorComplete)
                .cornerRadius((int) mCornerRadius);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (mTextLayout == null && !TextUtils.isEmpty(mCompleteText)) {
            mTextLayout = new StaticLayout(mCompleteText, mTextPaint, mButton.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 0, true);
        }
        if (mTextLayout != null) {
            int pading = (mButton.getHeight() - mTextLayout.getHeight()) / 2;
            canvas.translate(0, pading);
            mTextPaint.setColor(mTextColorComplete.getDefaultColor());
            mTextLayout.draw(canvas);
            canvas.translate(0, -pading);
        }
    }

    @Override
    public void initAttrs(TypedArray typedArray) {
        if (typedArray == null) {
            return;
        }
        mCompleteText = typedArray
                .getString(R.styleable.CircularProgressButton_mcCirButtonTextComplete);
//        mIconComplete = typedArray.getResourceId(
//                R.styleable.CircularProgressButton_mcCirButtonIconComplete, 0);
        mCornerRadius = typedArray.getDimension(
                R.styleable.CircularProgressButton_mcCirButtonCornerRadius, 0);
        int completeStateSelector = typedArray.getResourceId(
                R.styleable.CircularProgressButton_mcCirButtonSelectorComplete,
                R.color.mc_cir_progress_button_white);
        mCompleteColorState = mButton.getResources().getColorStateList(completeStateSelector);
        int completeStrokeColorSelector = typedArray.getResourceId(R.styleable.CircularProgressButton_mcCirButtonStrokeColorComplete, completeStateSelector);
        mStrokeColorComplete = mButton.getResources().getColorStateList(completeStrokeColorSelector);

        mStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) mButton.getResources().getDimension(
                        R.dimen.mc_cir_progress_button_progress_stroke_width));

        mTextColorComplete = typedArray.getColorStateList(R.styleable.CircularProgressButton_mcCirButtonTextColorComplete);
        if (mTextColorComplete == null) {
            mTextColorComplete = mButton.getTextColors();
        }
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    public void setTextColor(ColorStateList colorStateList) {
        mTextColorComplete = colorStateList;
    }

    public void setText(String text) {
        mCompleteText = text;
        mTextLayout = null;
    }

}
