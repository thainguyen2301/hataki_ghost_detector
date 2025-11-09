package com.ghost.finder.detector.radar.tracker

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.ghost.finder.detector.radar.tracker.data.repository.language.LanguageRepository
import com.ghost.finder.detector.radar.tracker.data.system.locale.LocaleManager
import com.ghost.finder.detector.radar.tracker.data.system.network.NetworkManager
import com.ghost.finder.detector.radar.tracker.ui.language.LanguageHataki1Activity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    companion object {
        const val DEFAULT_LANGUAGE = "en"
    }

    private val appJob = SupervisorJob()
    val appScope = CoroutineScope(Dispatchers.Default + appJob)

    @Inject
    lateinit var languageRepository: LanguageRepository

    override fun onCreate() {
        super.onCreate()
        NetworkManager.Companion.instance(this).register()
    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.Companion.instance(this).unregister()
        appJob.cancel()
    }

    override fun attachBaseContext(base: Context?) {
        val contextBase = base?.let {
            val lang = PreferenceManager.getDefaultSharedPreferences(base)
                .getString(LanguageHataki1Activity.Companion.APP_LANG, "en") ?: "en"
            LocaleManager(it).setLocale(lang)
        }
        super.attachBaseContext(contextBase)
    }

}
