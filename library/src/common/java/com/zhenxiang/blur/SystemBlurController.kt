package com.zhenxiang.blur

import android.content.Context
import android.graphics.Color
import android.graphics.Outline
import android.graphics.Path
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.android.internal.graphics.drawable.BackgroundBlurDrawable
import com.zhenxiang.blur.model.CornersRadius
import java.util.function.Consumer
import kotlin.math.roundToInt

class SystemBlurController(
    private val view: View,
    @ColorInt backgroundColour: Int = Color.TRANSPARENT,
    blurBackgroundColourOpacity: Float = DEFAULT_BLUR_BACKGROUND_COLOUR_OPACITY,
    blurRadius: Int = DEFAULT_BLUR_RADIUS,
    cornerRadius: CornersRadius = CornersRadius.all(0f),
): View.OnAttachStateChangeListener {

    private var windowManager: WindowManager? = null
    private val crossWindowBlurListener = Consumer<Boolean> {
        blurEnabled = it
    }

    private val blurDrawable: BackgroundBlurDrawable?
    get() {
        return view.background?.let {
            if (it is BackgroundBlurDrawable) it else null
        }
    }

    private var blurEnabled: Boolean = false
    set(value) {
        if (value != field) {
            field = value
            updateBackgroundColour()
        }
    }

    @ColorInt
    var backgroundColour = backgroundColour
    set (value) {
        field = value
        updateBackgroundColour()
    }
    // Opacity applied to the background colour when blur is available
    var blurBackgroundColourOpacity = blurBackgroundColourOpacity
    set(value) {
        field = value
        updateBackgroundColour()
    }
    var blurRadius = blurRadius
    set(value) {
        field = value
        blurDrawable?.setBlurRadius(value)
    }
    var cornerRadius = cornerRadius
    set(value) {
        field = value
        blurDrawable?.let {
            setCornerRadius(it, value)
        }
    }

    init {
        view.addOnAttachStateChangeListener(this)
    }

    override fun onViewAttachedToWindow(_v: View) {
        windowManager = getWindowManager(view.context)
        windowManager?.addCrossWindowBlurEnabledListener(crossWindowBlurListener)

        view.createBackgroundBlurDrawable()?.let {
            // Configure blur drawable with current values
            it.setColor(
                if (blurEnabled) applyOpacityToColour(backgroundColour, blurBackgroundColourOpacity) else backgroundColour
            )
            it.setBlurRadius(blurRadius)
            setCornerRadius(it, cornerRadius)

            view.background = it
        }
    }

    override fun onViewDetachedFromWindow(_v: View) {
        // Clear blur drawable
        if (view.background is BackgroundBlurDrawable) {
            view.background = null
        }

        windowManager?.removeCrossWindowBlurEnabledListener(crossWindowBlurListener)
        windowManager = null
    }

    private fun updateBackgroundColour() {
        blurDrawable?.setColor(
            if (blurEnabled) applyOpacityToColour(backgroundColour, blurBackgroundColourOpacity) else backgroundColour
        )
    }

    private fun setCornerRadius(blurDrawable: BackgroundBlurDrawable, corners: CornersRadius) {
        blurDrawable.setCornerRadius(
            corners.topLeft, corners.topRight, corners.bottomLeft, corners.bottomRight)

        view.outlineProvider = getViewOutlineProvider(corners)
    }

    private fun getViewOutlineProvider(corners: CornersRadius): ViewOutlineProvider {
        return object: ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {

                val radius = floatArrayOf(
                    corners.topLeft,
                    corners.topLeft,
                    corners.topRight,
                    corners.topRight,
                    corners.bottomRight,
                    corners.bottomRight,
                    corners.bottomLeft,
                    corners.bottomLeft,
                )
                val path = Path()
                path.addRoundRect(
                    0f, 0f, view.width.toFloat(), view.height.toFloat(), radius, Path.Direction.CW
                )
                outline.setPath(path)
            }
        }
    }

    private fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
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