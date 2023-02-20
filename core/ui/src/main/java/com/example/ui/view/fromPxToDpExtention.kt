package com.example.ui.view

import android.content.res.Resources
import kotlin.math.roundToInt

internal fun Int.fromPxToDp() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
