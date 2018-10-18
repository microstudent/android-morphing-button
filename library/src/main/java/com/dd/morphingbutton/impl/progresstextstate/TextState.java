package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.impl.CircularProgressButton;
import com.dd.morphingbutton.utils.DensityUtil;

public class TextState extends AbsProgressTextState {

    private final CircularProgressButton mButton;
    private String mText;
    private float mTextSize;
    private float mOriginTextSize;

    private TextPaint mTextPaint;
    private StaticLayout mStaticLayout;
    private ColorStateList mTextColor;

    public TextState(CircularProgressButton button) {
        mButton = button;
        initTextPaint();
    }

    private void initTextPaint() {
        mTextPaint = mButton.getPaint();
        mTextColor = ColorStateList.valueOf(Color.GRAY);
    }

    @Override
    public void start() {
        mOriginTextSize = mTextPaint.getTextSize();
        if (mTextSize > 0) {
            mTextPaint.setTextSize(DensityUtil.sp2px(mButton.getContext(), mTextSize));
        }
    }

    @Override
    public void stop() {
        mTextPaint.setTextSize(mOriginTextSize);
    }

    @NonNull
    @Override
    public MorphingParams getParams() {
        return MorphingParams
                .create()
                .solidColor(Color.TRANSPARENT)
                .textColor(Color.GRAY);
    }

    @Override
    public void initAttrs(TypedArray typedArray) {
    }

    public void setText(String text) {
        if (TextUtils.equals(text, mText)) {
            return;
        }
        mText = text;
        mStaticLayout = null;
        makeDirty(true);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (mStaticLayout == null && !TextUtils.isEmpty(mText)) {
            mStaticLayout = new StaticLayout(mText, mTextPaint, mButton.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 0, true);
        }
        if (mStaticLayout != null) {
            int pading = (mButton.getHeight() - mStaticLayout.getHeight()) / 2;
            canvas.translate(0, pading);
            mTextPaint.setColor(mTextColor.getDefaultColor());
            mStaticLayout.draw(canvas);
            canvas.translate(0, -pading);
        }
    }

    public void setTextSize(float textSizeInSp) {
        if (textSizeInSp == mTextSize) {
            return;
        }
        mTextSize = textSizeInSp;
        makeDirty(true);
    }

    public void setTextColor(ColorStateList colorStateList) {
        mTextColor = colorStateList;
    }


    @Override
    public String toString() {
        return "TextState: " + "mText = " + mText + "mTextSize = " + mTextSize;
    }
}
