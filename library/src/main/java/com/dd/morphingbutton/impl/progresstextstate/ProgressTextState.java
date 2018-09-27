package com.dd.morphingbutton.impl.progresstextstate;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;

public interface ProgressTextState {

    void start();

    void stop();

    @NonNull
    MorphingParams getParams();

    void onDraw(@NonNull Canvas canvas);
}
