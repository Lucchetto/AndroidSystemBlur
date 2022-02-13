package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout

import com.zhenxiang.blur.model.CornersRadius

class BlurFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val blurController: SystemBlurController

    init {
        val a = attrs?.let {
            context.obtainStyledAttributes(
                attrs, R.styleable.BlurFrameLayout, defStyleAttr, defStyleRes)
        }

        if (a != null) {
            val allEdgesCornerRadius = a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadius, 0)

            val cornerRadius = CornersRadius(
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusTopLeft, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusTopRight, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusBottomLeft, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurFrameLayout_cornerRadiusBottomRight, -1)
                )
            )

            blurController = SystemBlurController(
                this,
                a.getColor(R.styleable.BlurFrameLayout_backgroundColour, Color.TRANSPARENT),
                a.getFloat(
                    R.styleable.BlurFrameLayout_blurBackgroundColourOpacity,
                    SystemBlurController.DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
                ),
                a.getInteger(R.styleable.BlurFrameLayout_blurRadius, SystemBlurController.DEFAULT_BLUR_RADIUS),
                cornerRadius,
            )

            a.recycle()
        } else {
            blurController = SystemBlurController(this)
        }
    }
} 
