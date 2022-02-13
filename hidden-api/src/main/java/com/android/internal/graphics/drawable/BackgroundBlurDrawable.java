package com.android.internal.graphics.drawable;

import android.graphics.drawable.Drawable;

public abstract class BackgroundBlurDrawable extends Drawable {

    public void setCornerRadius(float cornerRadiusTL, float cornerRadiusTR, float cornerRadiusBL,
                                float cornerRadiusBR) {}

    public void setBlurRadius(int blurRadius) {}

    public void setColor(int color) {}
}
