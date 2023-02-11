package com.project.rezsim.device

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import com.project.rezsim.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

class ScreenRepository : KoinComponent {

    private val context: Context by inject()

    private val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager


    private var screenSize: Size? = null

    fun screenSize(): Size = screenSize ?: calculateScreenSize()

    fun isTablet() = context.resources.getBoolean(R.bool.is_tablet)

    private fun calculateScreenSize(): Size = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = wm.currentWindowMetrics
        val rect = windowMetrics.bounds
        Size(rect.width(), rect.height())
    } else {
        val metrics = DisplayMetrics()
        val point = Point()
        val display = wm.defaultDisplay
        display.getRealSize(point)
        display.getMetrics(metrics)
        Size(point.x, point.y)
    }

}
val Int.dp: Int
    get() = (this * (Resources.getSystem().displayMetrics.densityDpi / 160f)).roundToInt()
val Int.px: Int
    get() = (this / (Resources.getSystem().displayMetrics.densityDpi / 160f)).roundToInt()
