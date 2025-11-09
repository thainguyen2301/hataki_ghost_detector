package com.ghost.finder.detector.radar.tracker.data.system.locale

import android.content.Context
import java.util.Locale

class LocaleManager(private val context: Context) {

    fun setLocale(language: String): Context {
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun getLocale(): String {
        return Locale.getDefault().language
    }
}
