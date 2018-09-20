package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingButton;

public interface ProgressTextState {

    void start();

    void stop();

    @NonNull
    MorphingButton.Params getParams();

    void onDraw(@NonNull Canvas canvas);
}
