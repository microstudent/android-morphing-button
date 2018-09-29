package com.dd.morphingbutton;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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

    public void morph(@NonNull MorphingParams params) {
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

    private void morphWithAnimation(@NonNull final MorphingParams params) {
        mAnimationInProgress = true;
        setText(null);
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setPadding(mPadding.left, mPadding.top, mPadding.right, mPadding.bottom);

        MorphingAnimation.Params animationParams = MorphingAnimation.Params.create(this)
                .textColor(mTextColor == null ? Color.TRANSPARENT : mTextColor.getDefaultColor(), params.getTextColor().getDefaultColor())
                .solidColor(mSolidColor == null ? Color.TRANSPARENT : mSolidColor.getDefaultColor(), params.getSolidColor().getDefaultColor())
                .cornerRadius(mCornerRadius == null ? 0 : mCornerRadius, params.getCornerRadius())
                .strokeWidth(mStrokeWidth, params.getStrokeWidth())
                .strokeColor(mStrokeColor == null ? Color.TRANSPARENT : mStrokeColor.getDefaultColor(), params.getStrokeColor().getDefaultColor())
                .height(mHeight, params.getHeight())
                .width(mWidth, params.getWidth())
                .backgroundWidth(getBackground().getBounds().width(), params.getBackgroundWidth())
                .duration(params.getDuration())
                .listener(new MorphingAnimation.Listener() {
                    @Override
                    public void onAnimationEnd() {
                        finalizeMorphing(params);
                    }
                });

        mMorphingAnimation = new MorphingAnimation(animationParams);
        mMorphingAnimation.start();
    }

    private void morphWithoutAnimation(@NonNull MorphingParams params) {
        if (params.getCornerRadius() != null) {
            mDrawableNormal.setCornerRadius(params.getCornerRadius());
        }
        mDrawableNormal.setColor(params.getSolidColor());
        mDrawableNormal.setStrokeColor(params.getStrokeColor());
        mDrawableNormal.setStrokeWidth(params.getStrokeWidth());


        final int fromBackgroundWidth = mDrawableNormal.getGradientDrawable().getBounds().width();
        final int toBackgroundWidth = params.getBackgroundWidth();

        if (fromBackgroundWidth != toBackgroundWidth && toBackgroundWidth > 0) {
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofInt("width", fromBackgroundWidth, toBackgroundWidth);

            ValueAnimator backgroundSizeAnimator = ValueAnimator.ofPropertyValuesHolder(widthHolder);
            backgroundSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int width = (Integer) animation.getAnimatedValue("width");

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

                    mDrawableNormal.getGradientDrawable()
                            .setBounds(leftOffset + padding, padding, rightOffset - padding - 1,
                                    getHeight() - padding - 1);
                }
            });
            backgroundSizeAnimator.start();
        }

        if(params.getWidth() != 0 && params.getHeight() !=0) {
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
        } else if(params.getText() != null) {
            setText(params.getText());
        }

        mDrawableNormal.setColor(params.getSolidColor());
        mDrawableNormal.setStrokeColor(params.getStrokeColor());

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
        int cornerRadius = (int) resources.getDimension(R.dimen.mb_corner_radius_2);
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

        mDrawableNormal = createDrawable(colorStateList, colorStateList, cornerRadius, 0);

        mSolidColor = colorStateList;
        mStrokeColor = colorStateList;
        mCornerRadius = (float) cornerRadius;
        mTextColor = getTextColors();

        setBackgroundCompat(mDrawableNormal.getGradientDrawable());
    }

    private StrokeGradientDrawable createDrawable(ColorStateList color, ColorStateList strokeColor, int cornerRadius, int strokeWidth) {
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
