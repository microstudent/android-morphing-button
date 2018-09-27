package com.dd.morphingbutton;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class StrokeGradientDrawable {

    private int mStrokeWidth;
    private ColorStateList mStrokeColor;

    private GradientDrawable mGradientDrawable;
    private float mRadius;
    private ColorStateList mColor;

    public StrokeGradientDrawable(GradientDrawable drawable) {
        mGradientDrawable = drawable;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mGradientDrawable.setStroke(strokeWidth, getStrokeColor());
    }

    public ColorStateList getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(ColorStateList strokeColor) {
        mStrokeColor = strokeColor;
        mGradientDrawable.setStroke(getStrokeWidth(), strokeColor);
    }

    public void setStrokeColor(int strokeColor) {
        mGradientDrawable.setStroke(getStrokeWidth(), strokeColor);
    }

    public void setCornerRadius(float radius) {
        mRadius = radius;
        mGradientDrawable.setCornerRadius(radius);
    }

    public void setColor(ColorStateList color) {
        mColor = color;
        mGradientDrawable.setColor(color);
    }

    public void setColor(int color) {
        mGradientDrawable.setColor(color);
    }

    public ColorStateList getColor() {
        return mColor;
    }

    public float getRadius() {
        return mRadius;
    }

    public GradientDrawable getGradientDrawable() {
        return mGradientDrawable;
    }
}
