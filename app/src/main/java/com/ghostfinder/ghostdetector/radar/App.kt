package com.ghostfinder.ghostdetector.radar

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.ghostfinder.ghostdetector.radar.data.repository.language.LanguageRepository
import com.ghostfinder.ghostdetector.radar.data.system.locale.LocaleManager
import com.ghostfinder.ghostdetector.radar.data.system.network.NetworkManager
import com.ghostfinder.ghostdetector.radar.ui.language.LanguageActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@HiltAndroidApp
class App : MyApplication() {
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

    override fun attachBaseContext(base: Context?) {
        val contextBase = base?.let {
            val lang = PreferenceManager.getDefaultSharedPreferences(base)
                .getString(LanguageActivity.Companion.APP_LANG, "en") ?: "en"
            LocaleManager(it).setLocale(lang)
        }
        super.attachBaseContext(contextBase)
    }


    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.Companion.instance(this).unregister()
        appJob.cancel()
    }
}
