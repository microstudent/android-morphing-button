package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;
import com.dd.morphingbutton.utils.DensityUtil;

public class TextState extends AbsProgressTextState {

    private final CircularProgressButton mButton;
    private String mText;
    private float mTextSize;
    private float mOriginTextSize;

    public TextState(CircularProgressButton button) {
        mButton = button;
    }

    @Override
    public void start() {
        mOriginTextSize = mButton.getTextSize();
        if (mTextSize > 0) {
            mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        }
    }

    @Override
    public void stop() {
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginTextSize);
    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams
                .create()
                .solidColor(Color.TRANSPARENT)
                .strokeWidth(0)
                .textColor(Color.GRAY)
                .text(mText);
    }

    @Override
    public void initAttrs(TypedArray typedArray) {
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
