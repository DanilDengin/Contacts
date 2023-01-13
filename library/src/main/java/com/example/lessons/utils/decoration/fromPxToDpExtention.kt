package com.example.lessons.utils.decoration

import android.content.res.Resources
import kotlin.math.roundToInt

internal fun Int.fromPxToDp() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
