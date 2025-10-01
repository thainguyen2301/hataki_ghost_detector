package com.hataki.ghostdetector

import android.app.Application
import com.hataki.ghostdetector.data.repository.language.LanguageRepository
import com.hataki.ghostdetector.data.system.network.NetworkManager
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
        appScope.launch {
            languageRepository.getCurrentLanguage().onSuccess { language ->
                language?.let {
                    LocaleHelper.setLocale(this@App, it)
                }
            }
        }
    }


    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.instance(this).unregister()
        appJob.cancel()
    }
}
