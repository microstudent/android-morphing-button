package com.dd.morphingbutton;

import android.animation.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MorphingAnimation {

    public interface Listener {
        void onAnimationEnd();
    }

    public static class Params {

        private float fromCornerRadius;
        private float toCornerRadius;

        private int fromHeight;
        private int toHeight;

        private int fromWidth;
        private int toWidth;

        @Nullable
        private Integer fromColor;
        @Nullable
        private Integer toColor;

        private int duration;

        private int fromStrokeWidth;
        private int toStrokeWidth;

        private int fromStrokeColor;
        private int toStrokeColor;

        @Nullable
        private Integer fromTextColor;
        @Nullable
        private Integer toTextColor;

        private MorphingButton button;
        private MorphingAnimation.Listener animationListener;

        private Params(@NonNull MorphingButton button) {
            this.button = button;
        }

        public static Params create(@NonNull MorphingButton button) {
            return new Params(button);
        }

        public Params duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Params listener(@NonNull MorphingAnimation.Listener animationListener) {
            this.animationListener = animationListener;
            return this;
        }

        public Params solidColor(Integer fromColor, Integer toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
            return this;
        }

        public Params cornerRadius(int fromCornerRadius, int toCornerRadius) {
            this.fromCornerRadius = fromCornerRadius;
            this.toCornerRadius = toCornerRadius;
            return this;
        }

        public Params height(int fromHeight, int toHeight) {
            this.fromHeight = fromHeight;
            this.toHeight = toHeight;
            return this;
        }

        public Params width(int fromWidth, int toWidth) {
            this.fromWidth = fromWidth;
            this.toWidth = toWidth;
            return this;
        }

        public Params strokeWidth(int fromStrokeWidth, int toStrokeWidth) {
            this.fromStrokeWidth = fromStrokeWidth;
            this.toStrokeWidth = toStrokeWidth;
            return this;
        }

        public Params strokeColor(int fromStrokeColor, int toStrokeColor) {
            this.fromStrokeColor = fromStrokeColor;
            this.toStrokeColor = toStrokeColor;
            return this;
        }

        public Params textColor(Integer fromTextColor, Integer toTextColor) {
            this.fromTextColor = fromTextColor;
            this.toTextColor = toTextColor;
            return this;
        }
    }

    private Params mParams;

    public MorphingAnimation(@NonNull Params params) {
        mParams = params;
    }

    public void start() {
        List<Animator> animators = new ArrayList<>();

        StrokeGradientDrawable background = mParams.button.getDrawableNormal();

        ObjectAnimator cornerAnimation =
                ObjectAnimator.ofFloat(background, "cornerRadius", mParams.fromCornerRadius, mParams.toCornerRadius);

        ObjectAnimator strokeWidthAnimation =
                ObjectAnimator.ofInt(background, "strokeWidth", mParams.fromStrokeWidth, mParams.toStrokeWidth);
        animators.add(strokeWidthAnimation);

        ObjectAnimator strokeColorAnimation = ObjectAnimator.ofInt(background, "strokeColor", mParams.fromStrokeColor, mParams.toStrokeColor);
        strokeColorAnimation.setEvaluator(new ArgbEvaluator());
        animators.add(strokeColorAnimation);

        ObjectAnimator bgColorAnimation = null;
        if (mParams.toColor != null) {
            bgColorAnimation = ObjectAnimator.ofInt(background, "color", mParams.fromColor == null ? 0 : mParams.fromColor, mParams.toColor);
            bgColorAnimation.setEvaluator(new ArgbEvaluator());
            animators.add(bgColorAnimation);
        }

        ObjectAnimator textColorAnimation = null;
        if (mParams.toTextColor != null) {
            textColorAnimation = ObjectAnimator.ofInt(mParams.button, "textColor", mParams.fromTextColor == null ? 0 : mParams.fromTextColor, mParams.toTextColor);
            textColorAnimation.setEvaluator(new ArgbEvaluator());
            animators.add(textColorAnimation);
        }


        if (mParams.fromHeight != mParams.toHeight && mParams.toHeight > 0 || mParams.fromWidth != mParams.toWidth && mParams.toWidth > 0) {
            PropertyValuesHolder heightHolder = PropertyValuesHolder.ofInt("height", mParams.fromHeight, mParams.toHeight);
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofInt("width", mParams.fromWidth, mParams.toWidth);

            ValueAnimator sizeAnimator = ValueAnimator.ofPropertyValuesHolder(widthHolder, heightHolder);
            sizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int width = (Integer) animation.getAnimatedValue("width");
                    int height = (int) animation.getAnimatedValue("height");
                    ViewGroup.LayoutParams layoutParams = mParams.button.getLayoutParams();
                    layoutParams.width = width;
                    layoutParams.height = height;
                    mParams.button.setLayoutParams(layoutParams);
                }
            });
            animators.add(sizeAnimator);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mParams.duration);

        animatorSet.playTogether(animators);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mParams.animationListener != null) {
                    mParams.animationListener.onAnimationEnd();
                }
            }
        });
        animatorSet.start();
    }

}
