package com.dd.morphingbutton.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;

import com.dd.morphingbutton.IProgress;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.progresstextstate.CompleteState;
import com.dd.morphingbutton.impl.progresstextstate.ErrorState;
import com.dd.morphingbutton.impl.progresstextstate.IdleState;
import com.dd.morphingbutton.impl.progresstextstate.ProgressState;
import com.dd.morphingbutton.impl.progresstextstate.ProgressTextState;
import com.dd.morphingbutton.impl.progresstextstate.TextState;

import java.util.Map;

public class CircularProgressButton extends MorphingButton implements IProgress {

    private AttributeSet mAttrs;

    private StateEnum mCurrentStateEnum;

    public enum StateEnum {
        /***/
        PROGRESS,
        IDLE,
        COMPLETE,
        ERROR,
        TEXT;
        private ProgressTextState createStateInstance(CircularProgressButton button) {
            ProgressTextState state = null;
            switch (this) {
                case IDLE:
                    state = new IdleState(button);
                    break;
                case TEXT:
                    state = new TextState(button);
                    break;
                case ERROR:
                    state = new ErrorState(button);
                    break;
                case COMPLETE:
                    state = new CompleteState(button);
                    break;
                case PROGRESS:
                    state = new ProgressState(button);
                    break;
                default:
                    break;
            }
            return state;
        }
    }

    private Map<StateEnum, ProgressTextState> mStateMap = new ArrayMap<>(5);

    private ProgressTextState mCurrentStateImpl;

    public CircularProgressButton(Context context) {
        super(context);
        init();
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttrs = attrs;
        init();
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
        init();
    }

    private void init() {
    }

    public void setState(final StateEnum stateEnum, boolean withAnim) {
        if (mCurrentStateImpl != null) {
            mCurrentStateImpl.stop();
        }
        mCurrentStateImpl = getState(stateEnum);
        Params params = mCurrentStateImpl.getParams();
        morph(withAnim ? params.duration(300) : params.duration(0));
        mCurrentStateImpl.start();
        mCurrentStateEnum = stateEnum;
    }

    private ProgressTextState getState(StateEnum state) {
        ProgressTextState progressTextState;
        if (mStateMap.get(state) == null) {
            progressTextState = state.createStateInstance(this);
            mStateMap.put(state, progressTextState);
        } else {
            progressTextState = mStateMap.get(state);
        }
        return progressTextState;
    }


    public ProgressTextState getCurrentStateImpl() {
        return mCurrentStateImpl;
    }

    public StateEnum getCurrentStateEnum() {
        return mCurrentStateEnum;
    }


    public TypedArray getTypedArray() {
        return getContext().obtainStyledAttributes(mAttrs, R.styleable.CircularProgressButton, 0, 0);
    }

    @Override
    public void morph(@NonNull Params params) {
        super.morph(params);
    }

    @Override
    public void setProgress(int progress, boolean useAnim) {
        if (mCurrentStateEnum == StateEnum.PROGRESS) {
            ((ProgressState) getState(StateEnum.PROGRESS)).setProgress(progress, useAnim);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentStateImpl != null) {
            mCurrentStateImpl.onDraw(canvas);
        }
    }
}
