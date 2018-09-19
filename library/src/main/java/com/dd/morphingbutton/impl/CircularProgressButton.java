package com.dd.morphingbutton.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;

import com.dd.morphingbutton.IProgress;
import com.dd.morphingbutton.MorphingAnimation;
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
        private ProgressTextState createStateInstance(CircularProgressButton button, AttributeSet attributeSet) {
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

    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;

    private int mProgress;
    private int mProgressColor;
    private int mProgressCornerRadius;
    private Paint mPaint;
    private RectF mRectF;

    private boolean isIndeterminateRunning = false;

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

    public void morphToProgress(int color, int progressColor, int progressCornerRadius, int width, int height, int duration) {
        mProgressCornerRadius = progressCornerRadius;
        mProgressColor = progressColor;

        MorphingButton.Params longRoundedSquare = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(mProgressCornerRadius)
                .width(width)
                .height(height)
                .solidColor(color)
                .colorPressed(color);
        morph(longRoundedSquare);
    }

    public void setState(final StateEnum stateEnum, boolean withAnim) {
        if (mCurrentStateImpl != null) {
            mCurrentStateImpl.stop();
        }
        mCurrentStateImpl = initState(stateEnum);
        morph(withAnim ? mCurrentStateImpl.getParams() : mCurrentStateImpl.getParams().duration(0));
        mCurrentStateImpl.start();
        mCurrentStateEnum = stateEnum;
    }

    private ProgressTextState initState(StateEnum state) {
        ProgressTextState progressTextState;
        if (mStateMap.get(state) == null) {
            progressTextState = state.createStateInstance(this, mAttrs);
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

    public void morphToCompleted() {
        MorphingButton.Params params = MorphingButton.Params
                .create()
                .strokeColor(Color.parseColor("#198DED"))
                .textColor(Color.parseColor("#198DED"))
                .duration(300)
                .animationListener(new MorphingAnimation.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        isIndeterminateRunning = true;
                    }
                })
                .text("打开");
    }

    public TypedArray getTypedArray() {
        return getContext().obtainStyledAttributes(mAttrs, R.styleable.CircularProgressButton, 0, 0);
    }

    @Override
    public void morph(@NonNull Params params) {
        isIndeterminateRunning = false;
        super.morph(params);
    }

    @Override
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
