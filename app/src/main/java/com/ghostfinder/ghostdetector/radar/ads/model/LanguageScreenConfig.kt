package com.ghostfinder.ghostdetector.radar.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Configuration for language screen behavior
 */
@Keep
data class LanguageScreenConfig(
    @SerializedName("done_button_position")
    private val _doneButtonPosition: String? = null,

    @SerializedName("done_button_show_after_second")
    private val _doneButtonShowAfterSecond: Int? = null,

    @SerializedName("is_show_translating_pop_up")
    private val _isShowTranslatingPopUp: Boolean? = null,

    @SerializedName("translating_loading_second")
    private val _translatingLoadingSecond: Long? = null,

    @SerializedName("ignore_versions")
    val ignoreVersions: List<String>? = null
) {
    // Properties with default values when JSON is null
    val doneButtonPosition: DoneButtonPosition
        get() = when (_doneButtonPosition?.lowercase()) {
            "left" -> DoneButtonPosition.LEFT
            "right" -> DoneButtonPosition.RIGHT
            else -> DoneButtonPosition.RIGHT // Default
        }

    val doneButtonShowAfterSecond: Int
        get() = _doneButtonShowAfterSecond ?: 0

    val isShowTranslatingPopUp: Boolean
        get() = _isShowTranslatingPopUp ?: false

    val translatingLoadingSecond: Long
        get() = _translatingLoadingSecond ?: 0L

    companion object {
        var default = LanguageScreenConfig()
    }
}

enum class DoneButtonPosition {
    @SerializedName("left")
    LEFT,

    @SerializedName("right")
    RIGHT
}
