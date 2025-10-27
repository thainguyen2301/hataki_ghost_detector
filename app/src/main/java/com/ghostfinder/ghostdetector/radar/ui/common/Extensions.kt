package com.ghostfinder.ghostdetector.radar.ui.common

import android.content.res.Resources

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
