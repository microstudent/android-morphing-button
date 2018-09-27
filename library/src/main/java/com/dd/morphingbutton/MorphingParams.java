package com.dd.morphingbutton;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;

public class MorphingParams {
    @Nullable
    private Float cornerRadius;
    private int backgroundWidth;
    private int width;
    private int height;
    private ColorStateList textColor;

    private int duration;
    private int icon;
    private int strokeWidth;
    private String text;
    private MorphingAnimation.Listener animationListener;


    private Integer strokeColorNormal;
    private Integer strokeColorPressed;
    private ColorStateList strokeColor;

    private Integer colorNormal;
    private Integer colorPressed;
    private ColorStateList solidColor;


    private MorphingParams() {

    }

    public static MorphingParams create() {
        return new MorphingParams();
    }

    public MorphingParams text(@NonNull String text) {
        this.text = text;
        return this;
    }

    public MorphingParams icon(@DrawableRes int icon) {
        this.icon = icon;
        return this;
    }

    public MorphingParams cornerRadius(int cornerRadius) {
        this.cornerRadius = (float) cornerRadius;
        return this;
    }

    public MorphingParams backgroundWidth(int backgroundWidth) {
        this.backgroundWidth = backgroundWidth;
        return this;
    }

    public MorphingParams width(int width) {
        this.width = width;
        return this;
    }

    public MorphingParams height(int height) {
        this.height = height;
        return this;
    }

    public MorphingParams solidColor(int color) {
        this.colorNormal = color;
        return this;
    }

    public MorphingParams solidColor(ColorStateList colorStateList) {
        this.solidColor = colorStateList;
        return this;
    }

    public MorphingParams textColor(@ColorInt int color) {
        this.textColor = ColorStateList.valueOf(color);
        return this;
    }

    public MorphingParams textColor(ColorStateList color) {
        this.textColor = color;
        return this;
    }

    public MorphingParams colorPressed(int colorPressed) {
        this.colorPressed = colorPressed;
        return this;
    }


    public MorphingParams duration(int duration) {
        this.duration = duration;
        return this;
    }

    public MorphingParams strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public MorphingParams strokeColor(int strokeColor) {
        this.strokeColorNormal = strokeColor;
        return this;
    }

    public MorphingParams strokeColorPress(int strokeColor) {
        this.strokeColorPressed = strokeColor;
        return this;
    }

    public MorphingParams strokeColor(ColorStateList strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public MorphingParams animationListener(MorphingAnimation.Listener animationListener) {
        this.animationListener = animationListener;
        return this;
    }



    @Nullable
    public Float getCornerRadius() {
        return cornerRadius;
    }

    public int getBackgroundWidth() {
        return backgroundWidth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ColorStateList getTextColor() {
        if (textColor == null) {
            return ColorStateList.valueOf(Color.TRANSPARENT);
        }
        return textColor;
    }


    public int getDuration() {
        return duration;
    }

    public int getIcon() {
        return icon;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public String getText() {
        return text;
    }

    public MorphingAnimation.Listener getAnimationListener() {
        return animationListener;
    }

    public ColorStateList getSolidColor() {
        if (solidColor != null) {
            return solidColor;
        }

        return makeColorStateList(colorNormal, colorPressed);
    }

    public ColorStateList getStrokeColor() {
        if (strokeColor != null) {
            return strokeColor;
        }
        return makeColorStateList(strokeColorNormal, strokeColorPressed);
    }

    private ColorStateList makeColorStateList(Integer colorNormal, Integer colorPressed) {
        if (colorNormal == null) {
            return ColorStateList.valueOf(Color.TRANSPARENT);
        }
        if (colorPressed == null) {
            return ColorStateList.valueOf(colorNormal);
        }
        int[] colors = new int[]{colorPressed, colorNormal, colorNormal};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }
}
