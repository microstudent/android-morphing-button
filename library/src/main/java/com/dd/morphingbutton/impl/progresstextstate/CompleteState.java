package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class CompleteState implements ProgressTextState {

    public CompleteState(CircularProgressButton button) {

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
        return null;
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

    }
}
