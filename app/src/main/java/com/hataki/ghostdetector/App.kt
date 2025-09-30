package com.hataki.ghostdetector

import android.app.Application
import com.hataki.ghostdetector.data.repository.language.LanguageRepository
import com.hataki.ghostdetector.data.repository.network.NetworkRepository
import com.hataki.ghostdetector.utils.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var networkRepository: NetworkRepository

    private val appJob = SupervisorJob()
    val appScope = CoroutineScope(Dispatchers.Default + appJob)

    @Inject
    lateinit var languageRepository: LanguageRepository

    override fun onCreate() {
        super.onCreate()
        networkRepository.registerNetworkChange()
        appScope.launch {
            languageRepository.getCurrentLanguage().collect { language ->
                LocaleHelper.setLocale(this@App, language)
            }
        }
    }


    override fun onTerminate() {
        super.onTerminate()
        networkRepository.unRegisterNetworkChange()
        appJob.cancel()
    }
}
