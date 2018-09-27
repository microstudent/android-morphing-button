package com.dd.morphingbutton.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.TextView;

import com.dd.morphingbutton.sample.R;


/**
 * Created by wuyongtian on 15-7-22.
 */
public class ButtomUtils {

    /**
     *
     * @param textView
     * @param colorId 颜色id
     * @param radius 弧度半径
     * @param bSolid 是否实心
     */
    public static void setBackgroundAndTextColor(TextView textView, int colorId, float radius, boolean bSolid) {
        // 设置背景
        textView.setBackground(getBackgroundWithId(textView.getContext(), colorId, radius, bSolid));

        // 设置字体颜色
        textView.setTextColor(getTextColorWithId(textView.getContext(), colorId, bSolid));
    }

    public static Drawable getBackgroundWithId(Context context, int colorId, float radius, boolean bSolid) {
        return getBackground(context, context.getResources().getColor(colorId), radius, bSolid);
    }

    public static Drawable getBackground(Context context, int color, float radius, boolean bSolid) {
        GradientDrawable normalDrawable = new GradientDrawable();
        GradientDrawable pressedDrawable = new GradientDrawable();
        GradientDrawable unableDrawable = new GradientDrawable();

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;
        if (bSolid) {
            normalDrawable.setColor(color);
            pressedDrawable.setColor(Color.HSVToColor(hsv));
        } else {
            normalDrawable.setStroke(context.getResources().getDimensionPixelSize(R.dimen.cir_progress_button_normal_stroke_width), color);
//                pressedDrawable.setColor(strColor);
            pressedDrawable.setStroke(4, Color.HSVToColor(hsv));
        }
        unableDrawable.setColor(context.getResources().getColor(android.R.color.transparent));

        normalDrawable.setCornerRadius(radius);
        pressedDrawable.setCornerRadius(radius);

        StateListDrawable seletor = new StateListDrawable();// 背景选择器
        seletor.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        seletor.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        seletor.addState(new int[]{-android.R.attr.state_enabled}, unableDrawable);
        seletor.addState(new int[]{}, normalDrawable);

        return seletor;
    }

    public static ColorStateList getTextColorWithId(Context context, int colorId, boolean bSolid) {
        return getTextColor(context, context.getResources().getColor(colorId), bSolid,context.getResources().getColor(android.R.color.white));
    }

    public static ColorStateList getTextColor(Context context, int color, boolean bSolid, int solidColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;

        int pressedColor = 0;
        int normalColor = 0;
        int unableColor = context.getResources().getColor(R.color.btn_unable);
        if (bSolid) {
            normalColor = solidColor;
            pressedColor = normalColor;
        } else {
            normalColor = color;
            pressedColor = Color.HSVToColor(hsv);//context.getResources().getColor(android.R.color.white);
        }

        int[] colors = new int[]{pressedColor, pressedColor, unableColor, normalColor, normalColor};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled};
        states[4] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;
    }

    public static Drawable getBtnBackground(Context context, int lineColor, int color, float radius) {
        GradientDrawable normalDrawable = new GradientDrawable();
        GradientDrawable pressedDrawable = new GradientDrawable();

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;

        float[] hsvLine = new float[3];
        Color.colorToHSV(lineColor, hsvLine);
        hsvLine[2] -= hsvLine[2] * 0.15f;

        normalDrawable.setColor(color);
        normalDrawable.setStroke(4, lineColor);
        pressedDrawable.setColor(Color.HSVToColor(hsv));
        pressedDrawable.setStroke(4, Color.HSVToColor(hsvLine));

        normalDrawable.setCornerRadius(radius);
        pressedDrawable.setCornerRadius(radius);

        StateListDrawable seletor = new StateListDrawable();// 背景选择器
        seletor.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        seletor.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        seletor.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        seletor.addState(new int[]{}, normalDrawable);

        return seletor;
    }


    public static Drawable getBackground2(Context context, int color, float radius, boolean bSolid) {
        GradientDrawable normalDrawable = new GradientDrawable();
        GradientDrawable pressedDrawable = new GradientDrawable();
        GradientDrawable unableDrawable = new GradientDrawable();

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;
        if (bSolid) {
            normalDrawable.setColor(color);
            pressedDrawable.setColor(Color.HSVToColor(hsv));
            unableDrawable.setColor(context.getResources().getColor(R.color.btn_unable));
        } else {
            normalDrawable.setStroke(4, color);
//                pressedDrawable.setColor(strColor);
            pressedDrawable.setStroke(4, Color.HSVToColor(hsv));
            unableDrawable.setStroke(4, context.getResources().getColor(R.color.btn_unable));
        }

        normalDrawable.setCornerRadius(radius);
        pressedDrawable.setCornerRadius(radius);
        unableDrawable.setCornerRadius(radius);

        StateListDrawable seletor = new StateListDrawable();// 背景选择器
        seletor.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        seletor.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        seletor.addState(new int[]{-android.R.attr.state_enabled}, unableDrawable);
        seletor.addState(new int[]{}, normalDrawable);

        return seletor;
    }

    public static ColorStateList getTextColor2(Context context, int color, boolean bSolid, int solidColor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;

        int pressedColor = 0;
        int normalColor = 0;
        int unableColor = 0;
        if (bSolid) {
            normalColor = solidColor;
            pressedColor = normalColor;
            unableColor = context.getResources().getColor(android.R.color.white);
        } else {
            normalColor = color;
            pressedColor = Color.HSVToColor(hsv);//context.getResources().getColor(android.R.color.white);
            unableColor = context.getResources().getColor(R.color.btn_unable);
        }

        int[] colors = new int[]{pressedColor, pressedColor, unableColor, normalColor};
        int[][] states = new int[4][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;
    }


    public static ColorStateList getCirProBackgroundColorWithId(Context context, int colorId) {
        int color = context.getResources().getColor(colorId);
        return getCirProBackgroundColor(context, color);
    }

    public static ColorStateList getCirProBackgroundColor(Context context, int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] -= hsv[2] * 0.15f;

        int pressedColor = 0;
        int normalColor = 0;
        int unableColor = context.getResources().getColor(R.color.btn_unable);
        normalColor = color;
        pressedColor = Color.HSVToColor(hsv);//context.getResources().getColor(android.R.color.white);

        int[] colors = new int[]{pressedColor, pressedColor, unableColor, normalColor, normalColor};
        int[][] states = new int[5][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_focused};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled};
        states[4] = new int[]{};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;
    }

    public static ColorStateList getCirProTextColorWithId(Context context, int colorId) {
        int color = context.getResources().getColor(colorId);
        return getCirProTextColor(context, color);
    }

    public static ColorStateList getCirProTextColor(Context context, int color) {
        return ColorStateList.valueOf(color);
    }
}
