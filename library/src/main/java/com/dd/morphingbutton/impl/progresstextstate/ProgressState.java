package com.dd.morphingbutton.impl.progresstextstate;

import android.animation.ValueAnimator;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.R;
import com.dd.morphingbutton.impl.CircularAnimatedDrawable;
import com.dd.morphingbutton.impl.CircularProgressDrawable;
import com.dd.morphingbutton.impl.CircularProgressButton;

public class ProgressState implements ProgressTextState {

    //TODO fixme
    public static final int MAX_PROGRESS = 101;
    public static final int MIN_PROGRESS = 0;
    public static final int IDLE_STATE_PROGRESS = 0;
    public static final int ERROR_STATE_PROGRESS = -1;
    private static int PROGRESS_ANIMATION_DURATION = 800;

    private final CircularProgressButton mButton;

    private Drawable mDefaultDrawable;
    private int mProgressStrokeWidth;
    private String mProgressText = "";
    private int mColorProgress;
    private int mColorIndicator;
    private int mColorIndicatorBackground;
    private boolean mIndeterminateProgressMode;
    private int mPaddingProgress;

    private CircularAnimatedDrawable mAnimatedDrawable;
    private CircularProgressDrawable mProgressDrawable;
    private boolean mNeedInvalidateCenterIcon;

    private int mProgress;
    private int mAnimCurrentProgress;
    private boolean isUseTransitionAnim;
    private ValueAnimator mProgressAnimation;
    private boolean mShouldShowCenterIcon;

    public ProgressState(CircularProgressButton button) {
        mButton = button;
        initAttrs();
    }

    protected int getColor(int id) {
        return mButton.getResources().getColor(id);
    }

    private void initAttrs() {
        TypedArray attr = mButton.getTypedArray();
        if (attr == null) {
            return;
        }

        int blue = getColor(R.color.mc_cir_progress_button_blue);
        int white = getColor(R.color.mc_cir_progress_button_white);
        int grey = getColor(R.color.mc_cir_progress_button_grey);
        int blank = getColor(R.color.mc_cir_progress_button_blank);

        mProgressStrokeWidth = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonStrokeWidth,
                (int) mButton.getContext().getResources().getDimension(
                        R.dimen.mc_cir_progress_button_stroke_width));
        mProgressText = attr
                .getString(R.styleable.CircularProgressButton_mcCirButtonTextProgress);
        mColorProgress = attr.getColor(
                R.styleable.CircularProgressButton_mcCirButtonColorProgress, white);
        mColorIndicator = attr.getColor(
                R.styleable.CircularProgressButton_mcCirButtonColorIndicator, blue);
        mColorIndicatorBackground =
                attr.getColor(
                        R.styleable.CircularProgressButton_mcCirButtonColorIndicatorBackground,
                        blank);
        mPaddingProgress = attr.getDimensionPixelSize(
                R.styleable.CircularProgressButton_mcCirButtonPaddingProgress, 0);
        attr.recycle();
    }


    public void setProgress(int progress, boolean useAnim) {
        mProgress = progress;
        isUseTransitionAnim = useAnim;
        if (mButton.isAnimationInProgress() || mButton.getWidth() == 0) {
            return;
        }

        CircularProgressButton.StateEnum currentState = mButton.getCurrentStateEnum();

        if (mProgress >= MAX_PROGRESS) {
            if (currentState == CircularProgressButton.StateEnum.PROGRESS) {
                mButton.setState(CircularProgressButton.StateEnum.COMPLETE, true);
            } else if (currentState == CircularProgressButton.StateEnum.IDLE) {
                mButton.setState(CircularProgressButton.StateEnum.COMPLETE, true);
            }
        } else if (mProgress > IDLE_STATE_PROGRESS) {
            if (currentState == CircularProgressButton.StateEnum.IDLE || currentState == CircularProgressButton.StateEnum.ERROR) {
                mButton.setState(CircularProgressButton.StateEnum.PROGRESS, true);
            } else if (currentState == CircularProgressButton.StateEnum.PROGRESS) {
                cancelProgressAnimation();
                if (useAnim) {
                    startProgressAnimation();
                } else {
                    mAnimCurrentProgress = mProgress;
                    mButton.invalidate();
                }
            }
        } else if (mProgress == ERROR_STATE_PROGRESS) {
            if (currentState == CircularProgressButton.StateEnum.PROGRESS) {
                mButton.setState(CircularProgressButton.StateEnum.ERROR, true);
            } else if (currentState == CircularProgressButton.StateEnum.IDLE) {
                mButton.setState(CircularProgressButton.StateEnum.ERROR, true);
            }
        } else if (mProgress == IDLE_STATE_PROGRESS) {
            if (currentState == CircularProgressButton.StateEnum.COMPLETE) {
                mButton.setState(CircularProgressButton.StateEnum.IDLE, true);
            } else if (currentState == CircularProgressButton.StateEnum.PROGRESS) {
                mButton.setState(CircularProgressButton.StateEnum.IDLE, true);
            } else if (currentState == CircularProgressButton.StateEnum.ERROR) {
                mButton.setState(CircularProgressButton.StateEnum.IDLE, true);
            }
        }
    }

    private void cancelProgressAnimation() {
        if (mProgressAnimation != null) {
            mProgressAnimation.cancel();
            mProgressAnimation.removeAllUpdateListeners();
            mProgressAnimation.removeAllListeners();
        }
    }

    private void startProgressAnimation() {
        mProgressAnimation = ValueAnimator.ofInt(mAnimCurrentProgress, mProgress);
        mProgressAnimation.setDuration(PROGRESS_ANIMATION_DURATION);
        mProgressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        mProgressAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimCurrentProgress = (Integer) animation.getAnimatedValue();
                mButton.invalidate();
            }
        });
        mProgressAnimation.start();
    }

    @Override
    public void start() {
        mDefaultDrawable = mButton.getBackground();
        if (mIndeterminateProgressMode) {
            initAnimatedDrawable();
            mButton.setBackgroundDrawable(mAnimatedDrawable);
            mAnimatedDrawable.start();
            mAnimatedDrawable.setAllowLoading(true);
        } else {
            initProgressDrawable();
        }
    }

    private void initProgressDrawable() {
//        int offset = (mButton.getWidth() - mButton.getHeight()) / 2;
        int size = mButton.getHeight() - mPaddingProgress * 2;
        mProgressDrawable = new CircularProgressDrawable(size, mProgressStrokeWidth, mColorIndicator);
//        int left = offset + mPaddingProgress;
//        mProgressDrawable.setBounds(left, mPaddingProgress, left, mPaddingProgress);
    }

    private void initAnimatedDrawable() {
        if (mAnimatedDrawable == null) {
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mProgressStrokeWidth);
        }
    }

    @Override
    public void stop() {
        if (mDefaultDrawable != null) {
            mButton.setBackgroundDrawable(mDefaultDrawable);
        }
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable.stop();
            mAnimatedDrawable.setAllowLoading(false);
        }
    }

    @NonNull
    @Override
    public MorphingButton.Params getParams() {
        int offset = (mButton.getWidth() - mButton.getHeight()) / 2;
        int left = offset + mPaddingProgress;
        int right = mButton.getWidth() - offset - mPaddingProgress;
        int bottom = mButton.getHeight() - mPaddingProgress;
        int top = mPaddingProgress;

        return MorphingButton.Params.create()
                .width(right - left)
                .height(bottom - top)
                .solidColor(mColorProgress)
                .strokeColor(mColorIndicatorBackground)
                .text(mProgressText);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (mIndeterminateProgressMode) {
            drawIndeterminateProgress(canvas);
        } else {
            drawProgress(canvas);
        }
    }

    private void drawProgress(Canvas canvas) {
        if (mNeedInvalidateCenterIcon) {
            mNeedInvalidateCenterIcon = false;
            mProgressDrawable.setShowCenterIcon(mShouldShowCenterIcon);
        }
        // float angle = (360f / mMaxProgress) * mProgress;
        float startAngle = 90 + (180f / MAX_PROGRESS) * mAnimCurrentProgress;
        float sweepAngle = -(180f / MAX_PROGRESS) * 2 * mAnimCurrentProgress;
        mProgressDrawable.setStartAngle(startAngle);
        mProgressDrawable.setSweepAngle(sweepAngle);
        mProgressDrawable.draw(canvas);
    }

    private void drawIndeterminateProgress(Canvas canvas) {
//        if (mAnimatedDrawable == null) {
//            initAnimatedDrawable();
//        } else {
//            mAnimatedDrawable.setAllowLoading(true);
//            mAnimatedDrawable.draw(canvas);
//        }
    }


    /**
     * 获取设置进度状态时，进度条样式
     *
     * @return
     */
    public boolean isIndeterminateProgressMode() {
        return mIndeterminateProgressMode;
    }

    /**
     * 设置进度模式，true为无进度模式
     *
     * @param indeterminateProgressMode
     */
    public void setIndeterminateProgressMode(boolean indeterminateProgressMode) {
        this.mIndeterminateProgressMode = indeterminateProgressMode;
    }



    /**
     * 是否显示中心图标，默认为暂停图标
     *
     * @param showCenterIcon
     */
    public void setShowCenterIcon(boolean showCenterIcon) {
        mShouldShowCenterIcon = showCenterIcon;
        mNeedInvalidateCenterIcon = true;
    }

}
