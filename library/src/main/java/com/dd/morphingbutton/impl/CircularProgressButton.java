package com.dd.morphingbutton.impl;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;

import com.dd.morphingbutton.IProgress;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.MorphingParams;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.progresstextstate.CompleteState;
import com.dd.morphingbutton.impl.progresstextstate.ErrorState;
import com.dd.morphingbutton.impl.progresstextstate.IdleState;
import com.dd.morphingbutton.impl.progresstextstate.ProgressState;
import com.dd.morphingbutton.impl.progresstextstate.AbsProgressTextState;
import com.dd.morphingbutton.impl.progresstextstate.TextState;

import java.util.Map;

import hugo.weaving.DebugLog;

public class CircularProgressButton extends MorphingButton implements IProgress {
    private static final String TAG = "CircularProgressButton";
    private static final int ANIMATION_DURATION = 300;
    private StateEnum mCurrentStateEnum;

    public enum StateEnum {
        /***/
        PROGRESS,
        IDLE,
        COMPLETE,
        ERROR,
        TEXT;
        private AbsProgressTextState createStateInstance(CircularProgressButton button) {
            AbsProgressTextState state = null;
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

    private Map<StateEnum, AbsProgressTextState> mStateMap = new ArrayMap<>(5);

    private AbsProgressTextState mCurrentStateImpl;

    public CircularProgressButton(Context context) {
        super(context);
        init(null);
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getTypedArray(attrs);
        for (StateEnum stateEnum : StateEnum.values()) {
            getState(stateEnum).initAttrs(typedArray);
        }
        typedArray.recycle();
    }

    @DebugLog
    public void setState(final StateEnum stateEnum, boolean withAnim) {
        if (mCurrentStateEnum == stateEnum) {
            if (mCurrentStateImpl != null && mCurrentStateImpl.isDirty()) {
                Log.e("LAZY", "refreshState!!! setState as " + stateEnum + ", this = " + this.toString());
                //需要刷新状态
                refreshState();
            }
            Log.e("LAZY", "setState fail!!! setState as " + stateEnum + ", this = " + this.toString());
            return;
        }
        Log.e("LAZY", "setState as " + stateEnum + ", this = " + this.toString());
        realSetState(stateEnum, withAnim);
    }

    private void realSetState(StateEnum stateEnum, boolean withAnim) {
        if (mCurrentStateImpl != null) {
            mCurrentStateImpl.stop();
        }
        mCurrentStateImpl = getState(stateEnum);
        MorphingParams params = mCurrentStateImpl.getParams();
        Log.e(TAG, mCurrentStateImpl.toString());
        morph(withAnim ? params.duration(ANIMATION_DURATION) : params.duration(0));
        mCurrentStateImpl.start();
        mCurrentStateEnum = stateEnum;
    }

    private AbsProgressTextState getState(StateEnum state) {
        AbsProgressTextState progressTextState;
        if (mStateMap.get(state) == null) {
            progressTextState = state.createStateInstance(this);
            mStateMap.put(state, progressTextState);
        } else {
            progressTextState = mStateMap.get(state);
        }
        return progressTextState;
    }

    private ProgressState getProgressStateImpl() {
        return ((ProgressState) getState(StateEnum.PROGRESS));
    }

    private ErrorState getErrorStateImpl() {
        return (ErrorState) getState(StateEnum.ERROR);
    }

    private CompleteState getCompleteStateImpl() {
        return (CompleteState) getState(StateEnum.COMPLETE);
    }

    private TextState getTextStateImpl() {
        return (TextState) getState(StateEnum.TEXT);
    }

    private IdleState getIdleStateImpl() {
        return (IdleState) getState(StateEnum.IDLE);
    }

    public AbsProgressTextState getCurrentStateImpl() {
        return mCurrentStateImpl;
    }

    public StateEnum getCurrentStateEnum() {
        return mCurrentStateEnum;
    }


    private TypedArray getTypedArray(AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        return getContext().obtainStyledAttributes(attrs, R.styleable.CircularProgressButton);
    }

    /**
     * 设置进度，状态由进度决定，-1时为错误状态，1-99为进度状态，100为完成状态，0为停息状态
     *
     * @param progress
     */
    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    @Override
    public void setProgress(int progress, boolean useAnim) {
        if (mCurrentStateEnum == StateEnum.PROGRESS) {
           getProgressStateImpl().setProgress(progress, useAnim);
        }
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgress() {
        return getProgressStateImpl().getProgress();
    }


    public void setShowCenterIcon(boolean showCenterIcon) {
        getProgressStateImpl().setShowCenterIcon(showCenterIcon);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentStateImpl != null) {
            mCurrentStateImpl.onDraw(canvas);
        }
    }

    /**
     * 获取设置进度状态时，进度条样式
     *
     * @return
     */
    public boolean isIndeterminateProgressMode() {
        return getProgressStateImpl().isIndeterminateProgressMode();
    }

    /**
     * 设置进度模式，true为无进度模式
     *
     * @param indeterminateProgressMode
     */
    public void setIndeterminateProgressMode(boolean indeterminateProgressMode) {
        getProgressStateImpl().setIndeterminateProgressMode(indeterminateProgressMode);
    }

    /**
     * 获取要在button上显示的字符串宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public int getTextWidth(Paint paint, String str) {
        TransformationMethod transformation = getTransformationMethod();
        if (transformation != null) {
            str = transformation.getTransformation(str, this).toString();
        }
        return (int) paint.measureText(str);
    }

//    /**
//     * 设置背景颜色
//     *
//     * @param color
//     */
//    @Override
//    public void setBackgroundColor(int color) {
//        mBackground.getGradientDrawable().setColor(color);
//    }

    /**
     * 设置描边颜色
     *
     * @param color
     */
    public void setStrokeColor(int color) {
        //TODO fixme
//        mBackground.setStrokeColor(color);
    }

    /**
     * 设置进度值，只在进度状态下有效
     *
     * @param progress 进度值
     */
    public void setProgressForState(int progress, boolean useAnim) {
        if (mCurrentStateEnum == StateEnum.PROGRESS) {
            getProgressStateImpl().setProgress(progress, useAnim);
        }
    }


    /**
     * 设置指定状态的背景颜色，即不同状态下的按钮背景颜色以及描边颜色
     *
     * @param state                    目标状态
     * @param backgroundColorStateList 按钮背景颜色
     * @param strokeColorStateList     描边颜色
     */
    public void setStateColorSelector(StateEnum state, ColorStateList backgroundColorStateList, ColorStateList strokeColorStateList) {
        if (backgroundColorStateList == null || strokeColorStateList == null) {
            return;
        }
        switch (state) {
            case IDLE:
                IdleState idleState = getIdleStateImpl();
                idleState.setColorState(backgroundColorStateList);
                idleState.setStrokeColorIdle(strokeColorStateList);
                break;
            case COMPLETE:
                CompleteState completeState = getCompleteStateImpl();
                completeState.setColorState(backgroundColorStateList);
                completeState.setStrokeColor(strokeColorStateList);
                break;
            case ERROR:
//                mErrorColorState = backgroundColorStateList;
//                mStrokeColorError = strokeColorStateList;
                break;
            default:
                break;
        }
        //fixme  刷新
        drawableStateChanged();
    }

    /**
     * 设置不同状态的字体颜色
     *
     * @param state
     * @param colorStateList
     */
    public void setStateTextColor(StateEnum state, ColorStateList colorStateList) {
        if (colorStateList == null) {
            return;
        }
        switch (state) {
            case IDLE:
                getIdleStateImpl().setTextColor(colorStateList);
                break;
            case COMPLETE:
                getCompleteStateImpl().setTextColor(colorStateList);
                break;
            case ERROR:
                getErrorStateImpl().setTextColor(colorStateList);
                break;
            default:
                break;
        }
        //fixme 刷新
    }


    /**
     * 设置不同状态的文字内容
     *
     * @param state
     * @param text
     */
    public void setStateText(StateEnum state, String text) {
        if (state == null) {
            return;
        }
        switch (state) {
            case IDLE:
                getIdleStateImpl().setText(text);
                break;
            case COMPLETE:
                getCompleteStateImpl().setText(text);
                break;
            case ERROR:
                getErrorStateImpl().setText(text);
                break;
            case TEXT:
                getTextStateImpl().setText(text);
                break;
            default:
                break;
        }
        if (mCurrentStateEnum == state && !isAnimationInProgress()) {
            //fixme 刷新
        }
    }


    /**
     * 设置进度条颜色
     *
     * @param color
     */
    public void setProgressIndicatorColor(int color) {
        getProgressStateImpl().setIndicatorColor(color);
    }

    /**
     * 设置进度条宽度
     *
     * @param width
     */
    public void setProgressStrokeWidth(int width) {
        getProgressStateImpl().setStrokeWidth(width);
    }

    /**
     * 设置进度条背景颜色
     *
     * @param backgroundColor
     */
    public void setIndicatorBackgroundColor(int backgroundColor) {
        getProgressStateImpl().setIndicatorBackgroundColor(backgroundColor);
    }


    public void setNoBackgroundTextSize(float textSize) {
        getTextStateImpl().setTextSize(textSize);
    }


    private void refreshState() {
        AbsProgressTextState stateImpl = getCurrentStateImpl();
        if (stateImpl != null && stateImpl.isDirty()) {
            cancelAllAnimation();
            realSetState(getCurrentStateEnum(), false);
        }
    }

}
