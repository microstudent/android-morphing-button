package com.dd.morphingbutton.impl.progresstextstate;

import com.dd.morphingbutton.MorphingButton;

public interface ProgressTextState {

    void start();

    void stop();

    MorphingButton.Params getParams();
}
