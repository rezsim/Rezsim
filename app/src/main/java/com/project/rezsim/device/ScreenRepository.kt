package com.project.rezsim.device

import android.content.Context
import android.content.res.Resources
import com.project.rezsim.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.roundToInt

class ScreenRepository : KoinComponent {

    private val context: Context by inject()

    fun isTablet() = context.resources.getBoolean(R.bool.is_tablet)

}
val Int.dp: Int
    get() = (this * (Resources.getSystem().displayMetrics.densityDpi / 160f)).roundToInt()
val Int.px: Int
    get() = (this / (Resources.getSystem().displayMetrics.densityDpi / 160f)).roundToInt()
