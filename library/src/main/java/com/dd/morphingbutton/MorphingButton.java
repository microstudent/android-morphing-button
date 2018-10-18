package com.dd.morphingbutton;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import hugo.weaving.DebugLog;

public class MorphingButton extends AppCompatButton {

    private Padding mPadding;
    private int mHeight;
    private int mWidth;
    @Nullable
    private ColorStateList mSolidColor;
    @Nullable
    private Float mCornerRadius;
    private int mStrokeWidth;
    private ColorStateList mStrokeColor;
    @Nullable
    private ColorStateList mTextColor;

    protected boolean mAnimationInProgress;

    private StrokeGradientDrawable mDrawableNormal;
    private MorphingAnimation mMorphingAnimation;

//    public boolean needLog;

    public MorphingButton(Context context) {
        super(context);
        initView();
    }

    public MorphingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MorphingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mHeight == 0 && mWidth == 0 && w != 0 && h != 0) {
            mHeight = getHeight();
            mWidth = getWidth();
        }
    }

    public StrokeGradientDrawable getDrawableNormal() {
        return mDrawableNormal;
    }

    @DebugLog
    public void morph(@NonNull MorphingParams params) {
        if (mDrawableNormal == null) {
            mDrawableNormal = createDrawable(mSolidColor, mStrokeColor, 0, 0);
            mDrawableNormal.getGradientDrawable().setState(getDrawableState());
            setBackgroundCompat(null);
            //onDraw手动绘制，因为setBackGround会使得此时setBound方法无效，在onDraw还是变成了view的大小,ref:http://www.voidcn.com/article/p-qzuadyjo-bbq.html
            mDrawableNormal.getGradientDrawable().setCallback(this);
        }

        if (!mAnimationInProgress) {

            if (params.getDuration() == 0) {
                morphWithoutAnimation(params);
            } else {
                morphWithAnimation(params);
            }

            mSolidColor = params.getSolidColor();
            if (params.getCornerRadius() != null) {
                mCornerRadius = params.getCornerRadius();
            }
            mStrokeWidth = params.getStrokeWidth();
            mStrokeColor = params.getStrokeColor();
            mTextColor = params.getTextColor();
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || (mDrawableNormal != null && who == mDrawableNormal.getGradientDrawable());
    }

    @Override
    public void draw(Canvas canvas) {
        if (mDrawableNormal != null) {
            ensureDrawableBounds(mDrawableNormal);
            mDrawableNormal.getGradientDrawable().draw(canvas);
        }
        super.draw(canvas);
    }

    private void ensureDrawableBounds(StrokeGradientDrawable strokeGradientDrawable) {
        if (strokeGradientDrawable != null) {
            if (strokeGradientDrawable.isBoundsDefault()) {
                strokeGradientDrawable.setBounds(0, 0, getRight() - getLeft(), getBottom() - getTop());
            }
        }
    }

    private void morphWithAnimation(@NonNull final MorphingParams params) {
        cancelAllAnimation();
        mAnimationInProgress = true;
        setText(null);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setPadding(mPadding.left, mPadding.top, mPadding.right, mPadding.bottom);

        int fromBackgroundWidth = mDrawableNormal.getGradientDrawable().getBounds().width();
        if (fromBackgroundWidth == 0) {
            fromBackgroundWidth = getWidth();
        }

        MorphingAnimation.Params animationParams = MorphingAnimation.Params.create(this)
                .textColor(mTextColor == null ? Color.TRANSPARENT : mTextColor.getDefaultColor(), params.getTextColor().getDefaultColor())
                .solidColor(mSolidColor == null ? Color.TRANSPARENT : mSolidColor.getDefaultColor(), params.getSolidColor().getDefaultColor())
                .cornerRadius(mCornerRadius == null ? 0 : mCornerRadius, params.getCornerRadius())
                .strokeWidth(mStrokeWidth, params.getStrokeWidth())
                .strokeColor(mStrokeColor == null ? Color.TRANSPARENT : mStrokeColor.getDefaultColor(), params.getStrokeColor().getDefaultColor())
                .height(mHeight, params.getHeight())
                .width(mWidth, params.getWidth())
                .backgroundWidth(fromBackgroundWidth, params.getBackgroundWidth())
                .duration(params.getDuration())
                .listener(new MorphingAnimation.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        finalizeMorphing(params);
                        unblockTouch();
                    }
                });

        mMorphingAnimation = new MorphingAnimation(animationParams);
        blockTouch();
        mMorphingAnimation.start();
    }

    @DebugLog
    private void morphWithoutAnimation(@NonNull MorphingParams params) {
        if (params.getCornerRadius() != null) {
            mDrawableNormal.setCornerRadius(params.getCornerRadius());
        }
        mDrawableNormal.setColor(params.getSolidColor());
        mDrawableNormal.setStrokeColor(params.getStrokeColor());
        mDrawableNormal.setStrokeWidth(params.getStrokeWidth());

        int fromBackgroundWidth = mDrawableNormal.getBounds().width();
        if (fromBackgroundWidth == 0) {
            fromBackgroundWidth = getWidth();
        }
        final int toBackgroundWidth = params.getBackgroundWidth();

        if (fromBackgroundWidth != toBackgroundWidth && toBackgroundWidth > 0) {
            int width = toBackgroundWidth;

            int leftOffset;
            int rightOffset;
            int padding;

            if (fromBackgroundWidth > toBackgroundWidth) {
                leftOffset = (fromBackgroundWidth - width) / 2;
                rightOffset = fromBackgroundWidth - leftOffset;
                padding = 0;
            } else {
                leftOffset = (toBackgroundWidth - width) / 2;
                rightOffset = toBackgroundWidth - leftOffset;
                padding = 0;
            }

            mDrawableNormal.setBounds(leftOffset + padding, padding, rightOffset - padding - 1,
                            getHeight() - padding - 1);
            mDrawableNormal.getGradientDrawable().invalidateSelf();
//            Log.e("CRAZY", "no anim ,rect = " + mDrawableNormal.getGradientDrawable().getBounds() + ", this = " + MorphingButton.this.toString());
//            needLog = true;
        }

        if (params.getWidth() != 0 && params.getHeight() != 0) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.width = params.getWidth();
            layoutParams.height = params.getHeight();
            setLayoutParams(layoutParams);
        }
        if (params.getTextColor() != null) {
            setTextColor(params.getTextColor());
        }

        finalizeMorphing(params);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mDrawableNormal != null) {
            //由于压下放手时动画已经开始假如这个时候更新各状态的Drawable，很可能把动画过程中的原先设置的属性给覆盖掉，因此在这里判断假如正在动画则保存状态。
            Rect r = recordBackgroundBoundIfNeed();
            mDrawableNormal.getGradientDrawable().setState(getDrawableState());
            restoreBackgroundBoundIfNeed(r);
        }
    }

    private Rect recordBackgroundBoundIfNeed() {
        if (!isAnimationInProgress()) {
            return null;
        }
        Rect r = new Rect();
        if (mDrawableNormal != null) {
            r.set(mDrawableNormal.getGradientDrawable().getBounds());
        }
        return r;
    }

    private void restoreBackgroundBoundIfNeed(Rect r) {
        if (isAnimationInProgress() && r != null && mDrawableNormal != null) {
            mDrawableNormal.getGradientDrawable().setBounds(r);
        }
    }

    private void setBackgroundState(Drawable d, int[] state) {
        if (d == null) {
            return;
        }
    }

    public boolean isAnimationInProgress() {
        return mAnimationInProgress;
    }

    private void finalizeMorphing(@NonNull MorphingParams params) {
        mAnimationInProgress = false;

        if (params.getTextSize() != null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, params.getTextSize());
        }

        if (params.getIcon() != 0 && params.getText() != null) {
            setIconLeft(params.getIcon());
            setText(params.getText());
        } else if (params.getIcon() != 0) {
            setIcon(params.getIcon());
        } else if (params.getText() != null && !TextUtils.equals(params.getText(), getText())) {
            setText(params.getText());
        }

        if (params.getAnimationListener() != null) {
            params.getAnimationListener().onAnimationEnd();
        }
    }

    public void blockTouch() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void unblockTouch() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void initView() {
        mPadding = new Padding();
        mPadding.left = getPaddingLeft();
        mPadding.right = getPaddingRight();
        mPadding.top = getPaddingTop();
        mPadding.bottom = getPaddingBottom();

        Resources resources = getResources();
        int cornerRadius = (int) resources.getDimension(R.dimen.v7_btn_install_corner_radius);
        int blue = resources.getColor(R.color.mb_blue);
        int blueDark = resources.getColor(R.color.mb_blue_dark);
        int unableColor = resources.getColor(R.color.btn_unable);
        int[] colors = new int[]{blueDark, blueDark, unableColor, blue, blue};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled};
        states[4] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);


        mSolidColor = colorStateList;
        mStrokeColor = colorStateList;
        mCornerRadius = (float) cornerRadius;
        mTextColor = getTextColors();
    }

    protected StrokeGradientDrawable createDrawable(ColorStateList color, ColorStateList strokeColor, int cornerRadius, int strokeWidth) {
        StrokeGradientDrawable drawable = new StrokeGradientDrawable(new GradientDrawable());
        drawable.getGradientDrawable().setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadius(cornerRadius);

        return drawable;
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(@Nullable Drawable drawable) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
    }

    public void setIcon(@DrawableRes final int icon) {
        // post is necessary, to make sure getWidth() doesn't return 0
        post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = getResources().getDrawable(icon);
                int padding = (getWidth() / 2) - (drawable.getIntrinsicWidth() / 2);
                setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
                setPadding(padding, 0, 0, 0);
            }
        });
    }

    public void setIconLeft(@DrawableRes int icon) {
        setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
    }

    private class Padding {
        public int left;
        public int right;
        public int top;
        public int bottom;
    }

    /**
     * 取消动画,在列表中，为防止状态错乱，应该首先取消动画状态
     */
    public void cancelAllAnimation() {
        if (mMorphingAnimation != null) {
            mMorphingAnimation.cancel();
        }
        mAnimationInProgress = false;
    }

}
