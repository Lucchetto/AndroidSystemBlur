package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout

import com.zhenxiang.blur.model.CornersRadius

class WindowBlurLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val blurController: SystemBlurController

    init {
        val a = attrs?.let {
            context.obtainStyledAttributes(
                attrs, R.styleable.BlurLinearLayout, defStyleAttr, defStyleRes)
        }

        if (a != null) {
            val allEdgesCornerRadius = a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadius, 0)

            val cornerRadius = CornersRadius(
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadiusTopLeft, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadiusTopRight, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadiusBottomLeft, -1)
                ),
                formatEdgeCornerRadius(
                    allEdgesCornerRadius,
                    a.getDimensionPixelSize(R.styleable.BlurLinearLayout_cornerRadiusBottomRight, -1)
                )
            )

            blurController = SystemBlurController(
                this,
                a.getColor(R.styleable.BlurLinearLayout_backgroundColour, Color.TRANSPARENT),
                a.getFloat(
                    R.styleable.BlurLinearLayout_blurBackgroundColourOpacity,
                    SystemBlurController.DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY
                ),
                a.getInteger(R.styleable.BlurLinearLayout_blurRadius, SystemBlurController.DEFAULT_BLUR_RADIUS),
                cornerRadius,
            )

            a.recycle()
        } else {
            blurController = SystemBlurController(this)
        }
    }
} 
