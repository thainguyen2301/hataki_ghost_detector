package com.ghost.finder.detector.radar.tracker.ads.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Configuration for language screen behavior
 */
@Keep
data class HKTLanguageScreenConfig(
    @SerializedName("done_button_position")
    private val _doneButtonPosition: String? = null,

    @SerializedName("done_button_show_after_second")
    private val _doneButtonShowAfterSecond: Int? = null,

    @SerializedName("is_show_translating_pop_up")
    private val _isShowTranslatingPopUp: Boolean? = null,

    @SerializedName("translating_loading_second")
    private val _translatingLoadingSecond: Long? = null,

    @SerializedName("ignore_versions")
    val ignoreVersions: List<String>? = null,

    @SerializedName("language_order")
    private val _languageOrderMain: Map<String, Int>? = null
) {
    // Properties with default values when JSON is null
    val doneButtonPosition: HKTDoneButtonPosition
        get() = when (_doneButtonPosition?.lowercase()) {
            "left" -> HKTDoneButtonPosition.LEFT
            "right" -> HKTDoneButtonPosition.RIGHT
            else -> HKTDoneButtonPosition.RIGHT // Default
        }

    val doneButtonShowAfterSecond: Int
        get() = _doneButtonShowAfterSecond ?: 0

    val isShowTranslatingPopUp: Boolean
        get() = _isShowTranslatingPopUp ?: false

    val translatingLoadingSecond: Long
        get() = _translatingLoadingSecond ?: 0L

    val languageOrderMain: Map<String, Int>
        get() = _languageOrderMain ?: emptyMap()

    companion object {
        var default = HKTLanguageScreenConfig()
    }
}

enum class HKTDoneButtonPosition {
    @SerializedName("left")
    LEFT,

    @SerializedName("right")
    RIGHT
}
