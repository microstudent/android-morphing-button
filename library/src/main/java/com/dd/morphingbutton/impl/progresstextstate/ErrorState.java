package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class ErrorState extends AbsProgressTextState {

    private ColorStateList mTextColor;

    public ErrorState(CircularProgressButton button) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams.create()
                .textColor(Color.WHITE)
                .solidColor(Color.RED)
                .strokeWidth(0)
                .text("错误");
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }

    @Override
    public void initAttrs(TypedArray typedArray) {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    public void setTextColor(ColorStateList colorStateList) {
        mTextColor = colorStateList;
    }

    public void setText(String text) {

    }
}
