package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class ErrorState implements ProgressTextState {

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
    public MorphingButton.Params getParams() {
        return MorphingButton.Params.create()
                .textColor(Color.WHITE)
                .solidColor(Color.RED)
                .strokeWidth(0)
                .text("错误");
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
