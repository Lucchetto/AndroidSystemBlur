package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import kotlin.math.roundToInt

import androidx.core.graphics.ColorUtils
import androidx.annotation.ColorInt

import com.android.internal.graphics.drawable.BackgroundBlurDrawable

class BlurLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    @ColorInt val backgroundColour: Int
    // Opacity applied to the background colour when blur is available
    val blurBackgroundColourOpacity: Float
    val blurRadius: Int
    val cornerRadius: Int

    init {
        val a = attrs?.let {
            context.obtainStyledAttributes(
                attrs, R.styleable.BlurLinearLayout, defStyleAttr, defStyleRes)
        }

        if (a != null) {
            backgroundColour = a.getColor(R.styleable.BlurLinearLayout_backgroundColour, Color.TRANSPARENT)
            blurBackgroundColourOpacity = a.getFloat(
                R.styleable.BlurLinearLayout_blurBackgroundColourOpacity,
                DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
            )
            blurRadius = a.getInteger(R.styleable.BlurLinearLayout_blurRadius, DEFAULT_BLUR_RADIUS)
            cornerRadius = a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadius, 0)

            a.recycle()
        } else {
            backgroundColour = Color.TRANSPARENT
            blurBackgroundColourOpacity = DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
            blurRadius = DEFAULT_BLUR_RADIUS
            cornerRadius = 0
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val blurDrawable: BackgroundBlurDrawable = getViewRootImpl().createBackgroundBlurDrawable()
        blurDrawable.setBlurRadius(blurRadius)
        blurDrawable.setColor(applyOpacityToColour(backgroundColour, blurBackgroundColourOpacity))
        blurDrawable.setCornerRadius(cornerRadius.toFloat())

        background = blurDrawable
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    companion object {
        const val DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY = 0.7f
        const val DEFAULT_BLUR_RADIUS = 25

        @ColorInt
        fun applyOpacityToColour(@ColorInt colour: Int, opacity: Float): Int {
            val targetAlpha = Color.alpha(colour) * opacity
            return ColorUtils.setAlphaComponent(colour, targetAlpha.roundToInt())
        }
    }
} 
