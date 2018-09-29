package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;
import com.dd.morphingbutton.utils.DensityUtil;

public class TextState extends AbsProgressTextState {

    private final CircularProgressButton mButton;
    private String mText;
    // TODO: 18-9-28
    private float mTextSize;

    public TextState(CircularProgressButton button) {
        mButton = button;
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
        return MorphingParams
                .create()
                .width(mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_width))
                .height(mButton.getResources().getDimensionPixelSize(R.dimen.v7_btn_install_height))
                .solidColor(Color.TRANSPARENT)
                .strokeWidth(0)
                .textColor(Color.GRAY)
                .text(mText);
    }

    @Override
    public void initAttrs(TypedArray typedArray) {
        mTextSize = DensityUtil.px2sp(mButton.getContext(), mButton.getTextSize());
    }

    public void setText(String text) {
        if (TextUtils.equals(text, mText)) {
            return;
        }
        mText = text;
        makeDirty(true);
    }

    public void setTextSize(float textSizeInSp) {
        if (textSizeInSp == mTextSize) {
            return;
        }
        mTextSize = textSizeInSp;
        makeDirty(true);
    }

    @Override
    public String toString() {
        return "TextState: " + "mText = " + mText + "mTextSize = " + mTextSize;
    }
}
