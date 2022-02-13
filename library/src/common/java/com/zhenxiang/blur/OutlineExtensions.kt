package com.zhenxiang.blur

import android.graphics.Outline
import android.graphics.Path
import android.view.View
import android.view.ViewOutlineProvider

fun View.getRoundedOutline(topLeft: Float, topRight: Float, bottomLeft: Float, bottomRight: Float): ViewOutlineProvider {
    return object: ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {

            val radius = floatArrayOf(
                topLeft,
                topLeft,
                topRight,
                topRight,
                bottomRight,
                bottomRight,
                bottomLeft,
                bottomLeft,
            )
            val path = Path()
            path.addRoundRect(
                0f, 0f, view.width.toFloat(), view.height.toFloat(), radius, Path.Direction.CW
            )
            outline.setPath(path)
        }
    }
}