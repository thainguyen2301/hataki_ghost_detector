package com.ghostfinder.ghostdetector.radar.ads.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AdConfiguration(
    @SerializedName("splash_inter_ad_ratio")
    val splashInterAdRatio: Int? = 0,
    @SerializedName("native_reload_time_second")
    private val _nativeReloadTime: Int? = null,
) {
    val nativeReloadTime: Long?
        get() = _nativeReloadTime?.let { (it * 1000).toLong() }
}
