package com.hataki.ghostdetector

import android.app.Application
import com.hataki.ghostdetector.data.repository.network.NetworkRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var networkRepository: NetworkRepository

    override fun onCreate() {
        super.onCreate()
        networkRepository.registerNetworkChange()
    }

    override fun onTerminate() {
        super.onTerminate()
        networkRepository.unRegisterNetworkChange()
    }
}
