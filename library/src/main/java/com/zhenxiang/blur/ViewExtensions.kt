package com.zhenxiang.blur

import android.util.Log
import android.view.View
import android.view.ViewRootImpl
import com.android.internal.graphics.drawable.BackgroundBlurDrawable
import org.lsposed.hiddenapibypass.HiddenApiBypass

fun View.createBackgroundBlurDrawable(): BackgroundBlurDrawable? {

    return try {
        val viewRootImpl = HiddenApiBypass.invoke(
            View::class.java,
            this,
            "getViewRootImpl" /*, args*/
        ) as ViewRootImpl

        viewRootImpl.createBackgroundBlurDrawable()
    } catch (e: Exception) {
        Log.w(null, e)
        null
    }
}