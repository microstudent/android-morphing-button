package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Color;
import android.util.AttributeSet;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class TextState implements ProgressTextState {

    public TextState(CircularProgressButton button) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public MorphingButton.Params getParams() {
        return MorphingButton.Params
                .create()
                .solidColor(Color.TRANSPARENT)
                .strokeWidth(0)
                .textColor(Color.GRAY)
                .duration(300)
                .text("正在安装");
    }
}
