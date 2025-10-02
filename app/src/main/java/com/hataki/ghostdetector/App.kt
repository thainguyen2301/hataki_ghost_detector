package com.hataki.ghostdetector

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.hataki.ghostdetector.data.repository.language.LanguageRepository
import com.hataki.ghostdetector.data.system.locale.LocaleManager
import com.hataki.ghostdetector.data.system.network.NetworkManager
import com.hataki.ghostdetector.ui.language.LanguageActivity.Companion.APP_LANG
import com.hataki.ghostdetector.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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
        NetworkManager.instance(this).register()
    }

    override fun attachBaseContext(base: Context?) {
        val contextBase = base?.let {
            val lang = PreferenceManager.getDefaultSharedPreferences(base)
                .getString(APP_LANG, "en") ?: "en"
            LocaleManager(it).setLocale(lang)
        }
        super.attachBaseContext(contextBase)
    }


    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.instance(this).unregister()
        appJob.cancel()
    }
}
