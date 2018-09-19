package com.dd.morphingbutton.impl;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.dd.morphingbutton.IProgress;
import com.dd.morphingbutton.MorphingAnimation;
import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.progresstextstate.CompleteState;
import com.dd.morphingbutton.impl.progresstextstate.ErrorState;
import com.dd.morphingbutton.impl.progresstextstate.IdleState;
import com.dd.morphingbutton.impl.progresstextstate.ProgressState;
import com.dd.morphingbutton.impl.progresstextstate.ProgressTextState;
import com.dd.morphingbutton.impl.progresstextstate.TextState;

import java.util.Map;

public class ProgressTextButton extends MorphingButton implements IProgress {

    private AttributeSet mAttrs;

    public enum StateEnum {
        /***/
        PROGRESS,
        IDLE,
        COMPLETE,
        ERROR,
        TEXT;
        private ProgressTextState createStateInstance(ProgressTextButton button, AttributeSet attributeSet) {
            ProgressTextState state = null;
            switch (this) {
                case IDLE:
                    state = new IdleState(button, attributeSet);
                    break;
                case TEXT:
                    state = new TextState(button, attributeSet);
                    break;
                case ERROR:
                    state = new ErrorState(button, attributeSet);
                    break;
                case COMPLETE:
                    state = new CompleteState(button, attributeSet);
                    break;
                case PROGRESS:
                    state = new ProgressState(button, attributeSet);
                    break;
                default:
                    break;
            }
            return state;
        }
    }

    private Map<StateEnum, ProgressTextState> mStateMap = new ArrayMap<>(5);

    private ProgressTextState mState;
    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;

    private int mProgress;
    private int mProgressColor;
    private int mProgressCornerRadius;
    private Paint mPaint;
    private RectF mRectF;

    private boolean isIndeterminateRunning = false;
    private CircularAnimatedDrawable mCircularProgressDrawable;

    public ProgressTextButton(Context context) {
        super(context);
        init();
    }

    public ProgressTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAttrs = attrs;
        init();
    }

    public ProgressTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setState(final StateEnum stateEnum) {
        mState = initState(stateEnum);
        morph(mState.getParams());
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

    public static class CircularProgressDrawable extends Drawable {
        private float mSweepAngle;
        private float mStartAngle;
        private int mSize;
        private int mStrokeWidth;
        private int mStrokeColor;
        private Drawable mCenterIcon;
        private Paint mIconPaint;
        private boolean mShouldIcon = false;
        private RectF mRectF;
        private Paint mPaint;
        private Path mPath;

        public CircularProgressDrawable(int size, int strokeWidth, int strokeColor) {
            this.mSize = size;
            this.mStrokeWidth = strokeWidth;
            this.mStrokeColor = strokeColor;
            this.mStartAngle = 90.0F;
            this.mSweepAngle = 0.0F;
        }

        public void setStartAngle(float startAngle) {
            this.mStartAngle = startAngle;
        }

        public void setSweepAngle(float sweepAngle) {
            this.mSweepAngle = sweepAngle;
        }

        public int getSize() {
            return this.mSize;
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect bounds = this.getBounds();
            if (this.mPath == null) {
                this.mPath = new Path();
            }

            this.mPath.reset();
            this.mPath.addArc(this.getRect(), this.mStartAngle, this.mSweepAngle);
            this.mPath.offset((float)bounds.left, (float)bounds.top);
            canvas.drawPath(this.mPath, this.createPaint());
            if (this.mCenterIcon != null) {
                canvas.save();
                this.mCenterIcon.setBounds(0, 0, this.mCenterIcon.getIntrinsicWidth(), this.mCenterIcon.getIntrinsicHeight());
                canvas.translate((float)(bounds.left + this.getSize() / 2 - this.mCenterIcon.getIntrinsicWidth() / 2), (float)(bounds.top + this.getSize() / 2 - this.mCenterIcon.getIntrinsicHeight() / 2));
                this.mCenterIcon.draw(canvas);
                canvas.restore();
            } else {
                if (!this.mShouldIcon) {
                    return;
                }

                if (this.mIconPaint == null) {
                    this.mIconPaint = new Paint();
                    this.mIconPaint.setStrokeCap(Paint.Cap.ROUND);
                    this.mIconPaint.setColor(this.mStrokeColor);
                    this.mIconPaint.setAntiAlias(true);
                }

                int height = this.getSize();
                int width = this.getSize();
                int lineWidth = this.mStrokeWidth;
                int lineHeight = (int)((float)height / 3.1F);
                int lineGap = (int)(0.12D * (double)width);
                this.mIconPaint.setStrokeWidth((float)lineWidth);
                canvas.drawLine((float)(bounds.left + width / 2 - lineGap / 2 - lineWidth / 2), (float)(bounds.top + height / 2 - lineHeight / 2 + lineWidth / 2), (float)(bounds.left + width / 2 - lineGap / 2 - lineWidth / 2), (float)(bounds.top + height / 2 + lineHeight / 2 - lineWidth / 2), this.mIconPaint);
                canvas.drawLine((float)(bounds.left + width / 2 + lineGap / 2 + lineWidth / 2), (float)(bounds.top + height / 2 - lineHeight / 2 + lineWidth / 2), (float)(bounds.left + width / 2 + lineGap / 2 + lineWidth / 2), (float)(bounds.top + height / 2 + lineHeight / 2 - lineWidth / 2), this.mIconPaint);
            }
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.OPAQUE;
        }

        private RectF getRect() {
            if (this.mRectF == null) {
                float index = (float)this.mStrokeWidth / 2.0F;
                this.mRectF = new RectF(index, index, (float)this.getSize() - index, (float)this.getSize() - index);
            }

            return this.mRectF;
        }

        private Paint createPaint() {
            if (this.mPaint == null) {
                this.mPaint = new Paint();
                this.mPaint.setAntiAlias(true);
                this.mPaint.setStyle(Paint.Style.STROKE);
                this.mPaint.setStrokeWidth((float)this.mStrokeWidth);
                this.mPaint.setColor(this.mStrokeColor);
                this.mPaint.setStrokeCap(Paint.Cap.ROUND);
                this.mPaint.setStrokeJoin(Paint.Join.ROUND);
            }

            return this.mPaint;
        }

        public void setCenterIcon(Drawable centerIcon) {
            this.mCenterIcon = centerIcon;
        }

        public void setShowCenterIcon(boolean showCenterIcon) {
            this.mShouldIcon = showCenterIcon;
        }

        public void setIndicatorColor(int color) {
            this.createPaint();
            this.mStrokeColor = color;
            this.mPaint.setColor(this.mStrokeColor);
        }

        public void setStrokeWidth(int width) {
            if (width > 0 && this.mStrokeWidth != width) {
                this.mStrokeWidth = width;
                if (this.mRectF != null) {
                    int index = this.mStrokeWidth / 2;
                    this.mRectF.set((float)index, (float)index, (float)(this.getSize() - index), (float)(this.getSize() - index));
                }

                if (this.mPaint != null) {
                    this.mPaint.setStrokeWidth((float)this.mStrokeWidth);
                }
            }

        }
    }

    public static class CircularAnimatedDrawable extends Drawable implements Animatable {
        private final long LOADING_ANIM_DURATION = 1760L;
        public static final String START_ANGLE_PROPERTY = "startAngle";
        public static final String SWEEP_ANGLE_PROPERTY = "sweepAngle";
        private final RectF fBounds = new RectF();
        private Paint mPaint;
        private float mBorderWidth;
        private boolean mRunning;
        private Animator mLoadingAnimator = null;
        private float mStartAngle;
        private float mSweepAngle;
        private boolean mAllowLoading = true;

        public CircularAnimatedDrawable(int color, float borderWidth) {
            this.mBorderWidth = borderWidth;
            this.mPaint = new Paint();
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(borderWidth);
            this.mPaint.setColor(color);
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mLoadingAnimator = this.createLoadingAnimator();
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawArc(this.fBounds, this.mStartAngle, this.mSweepAngle, false, this.mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            this.mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            this.mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSPARENT;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            this.fBounds.left = (float)bounds.left + this.mBorderWidth / 2.0F;
            this.fBounds.right = (float)bounds.right - this.mBorderWidth / 2.0F;
            this.fBounds.top = (float)bounds.top + this.mBorderWidth / 2.0F;
            this.fBounds.bottom = (float)bounds.bottom - this.mBorderWidth / 2.0F;
        }

        @Override
        public void start() {
            if (!this.isRunning()) {
                this.mRunning = true;
                this.mLoadingAnimator.start();
                this.invalidateSelf();
            }
        }

        @Override
        public void stop() {
            if (this.isRunning()) {
                this.mRunning = false;
                this.mLoadingAnimator.cancel();
                this.invalidateSelf();
            }
        }

        @Override
        public boolean isRunning() {
            return this.mRunning;
        }

        private Animator createLoadingAnimator() {
            Keyframe key1 = Keyframe.ofFloat(0.0F, -90.0F);
            Keyframe key2 = Keyframe.ofFloat(0.5F, 330.0F);
            Keyframe key3 = Keyframe.ofFloat(1.0F, 630.0F);
            PropertyValuesHolder pvhStart = PropertyValuesHolder.ofKeyframe("startAngle", new Keyframe[]{key1, key2, key3});
            PropertyValuesHolder pvhSweep = PropertyValuesHolder.ofFloat("sweepAngle", new float[]{0.0F, -120.0F, 0.0F});
            ObjectAnimator loadingAnim = ObjectAnimator.ofPropertyValuesHolder(this, new PropertyValuesHolder[]{pvhStart, pvhSweep});
            loadingAnim.setDuration(1760L);
            loadingAnim.setInterpolator(new LinearInterpolator());
            loadingAnim.setRepeatCount(-1);
            return loadingAnim;
        }

        public float getSweepAngle() {
            return this.mSweepAngle;
        }

        public void setSweepAngle(float sweepAngle) {
            this.mSweepAngle = sweepAngle;
            if (this.mAllowLoading) {
                this.invalidateSelf();
            }

        }

        public float getStartAngle() {
            return this.mStartAngle;
        }

        public void setStartAngle(float startAngle) {
            this.mStartAngle = startAngle;
            if (this.mAllowLoading) {
                this.invalidateSelf();
            }

        }

        public void setAllowLoading(boolean allow) {
            this.mAllowLoading = allow;
        }

        public void setIndicatorColor(int color) {
            if (this.mPaint != null) {
                this.mPaint.setColor(color);
            }

        }

        public void setStrokeWidth(int width) {
            if (width > 0 && (int)this.mBorderWidth != width) {
                this.mBorderWidth = (float)width;
                this.onBoundsChange(this.getBounds());
                if (this.mPaint != null) {
                    this.mPaint.setStrokeWidth(this.mBorderWidth);
                }
            }

        }
    }

}
