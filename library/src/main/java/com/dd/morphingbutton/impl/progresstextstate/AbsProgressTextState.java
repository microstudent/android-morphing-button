package com.dd.morphingbutton.impl.progresstextstate;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.dd.morphingbutton.MorphingParams;

/**
 * @author leaves
 */
public abstract class AbsProgressTextState {

    private boolean isDirty;

    public abstract void start();

    public abstract void stop();

    @NonNull
    public abstract MorphingParams getParams();

    public void onDraw(@NonNull Canvas canvas){

    }

    public abstract void initAttrs(TypedArray typedArray);

    public boolean isDirty() {
        return isDirty;
    }

    public void makeDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

}
