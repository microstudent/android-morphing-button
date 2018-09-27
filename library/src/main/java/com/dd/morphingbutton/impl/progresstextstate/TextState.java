package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class TextState implements ProgressTextState {

    private final CircularProgressButton mButton;

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
                .duration(300)
                .text("正在安装");
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
