package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.roundToInt

import androidx.core.graphics.ColorUtils
import androidx.annotation.ColorInt

class BlurFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @ColorInt val backgroundColour: Int
    // Opacity applied to the background colour when blur is available
    val blurBackgroundColourOpacity: Float
    val blurRadius: Int
    val cornerRadiusTopLeft: Float
    val cornerRadiusTopRight: Float
    val cornerRadiusBottomLeft: Float
    val cornerRadiusBottomRight: Float

    init {
        val a = attrs?.let {
            context.obtainStyledAttributes(
                attrs, R.styleable.BlurFrameLayout, defStyleAttr, defStyleRes)
        }

        if (a != null) {
            backgroundColour = a.getColor(R.styleable.BlurFrameLayout_backgroundColour, Color.TRANSPARENT)
            blurBackgroundColourOpacity = a.getFloat(
                R.styleable.BlurFrameLayout_blurBackgroundColourOpacity,
                DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
            )
            blurRadius = a.getInteger(R.styleable.BlurFrameLayout_blurRadius, DEFAULT_BLUR_RADIUS)

            val allEdgesCornerRadius = a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadius, 0)

            cornerRadiusTopLeft = formatEdgeCornerRadius(
                allEdgesCornerRadius,
                a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusTopLeft, -1)
            )
            cornerRadiusTopRight = formatEdgeCornerRadius(
                allEdgesCornerRadius,
                a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusTopRight, -1)
            )
            cornerRadiusBottomLeft = formatEdgeCornerRadius(
                allEdgesCornerRadius,
                a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusBottomLeft, -1)
            )
            cornerRadiusBottomRight = formatEdgeCornerRadius(
                allEdgesCornerRadius,
                a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusBottomRight, -1)
            )

            a.recycle()
        } else {
            backgroundColour = Color.TRANSPARENT
            blurBackgroundColourOpacity = DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
            blurRadius = DEFAULT_BLUR_RADIUS

            cornerRadiusTopLeft = 0f
            cornerRadiusTopRight = 0f
            cornerRadiusBottomLeft = 0f
            cornerRadiusBottomRight = 0f
        }

        // Setup outline for clipping and shadow
        outlineProvider = getRoundedOutline(
            cornerRadiusTopLeft,
            cornerRadiusTopRight,
            cornerRadiusBottomLeft,
            cornerRadiusBottomRight
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        createBackgroundBlurDrawable()?.let {
            it.setBlurRadius(blurRadius)
            it.setColor(applyOpacityToColour(backgroundColour, blurBackgroundColourOpacity))
            it.setCornerRadius(
                cornerRadiusTopLeft,
                cornerRadiusTopRight,
                cornerRadiusBottomLeft,
                cornerRadiusBottomRight
            )

            background = it
        }
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

        // Consider edgeRadius when more than -1, otherwise use allSidesRadius
        private fun formatEdgeCornerRadius(allSidesRadius: Int, edgeRadius: Int): Float {
            return if (edgeRadius > -1) {
                edgeRadius
            } else {
                allSidesRadius
            }.toFloat()
        }
    }
} 
