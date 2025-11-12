package com.ghost.finder.detector.radar.tracker.di

import android.content.Context
import com.ghost.finder.detector.radar.tracker.data.framework.compass.CompassManager
import com.ghost.finder.detector.radar.tracker.data.framework.evp.EVPRecorderManager
import com.ghost.finder.detector.radar.tracker.data.framework.sensor.SensorDetectManager
import com.ghost.finder.detector.radar.tracker.data.local.DataStoreManager
import com.ghost.finder.detector.radar.tracker.data.remote.ApiService
import com.ghost.finder.detector.radar.tracker.data.repository.UserRepository
import com.ghost.finder.detector.radar.tracker.data.repository.UserRepositoryImpl
import com.ghost.finder.detector.radar.tracker.data.repository.compass.CompassRepository
import com.ghost.finder.detector.radar.tracker.data.repository.compass.CompassRepositoryImpl
import com.ghost.finder.detector.radar.tracker.data.repository.evp.EvpRepository
import com.ghost.finder.detector.radar.tracker.data.repository.evp.EvpRepositoryImpl
import com.ghost.finder.detector.radar.tracker.data.repository.language.LanguageRepository
import com.ghost.finder.detector.radar.tracker.data.repository.language.LanguageRepositoryImpl
import com.ghost.finder.detector.radar.tracker.data.repository.sensor.SensorRepository
import com.ghost.finder.detector.radar.tracker.data.repository.sensor.SensorRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideUserRepository(api: ApiService): UserRepository = UserRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSensorRepository(sensorManager: SensorDetectManager): SensorRepository =
        SensorRepositoryImpl(sensorManager)

    @Provides
    @Singleton
    fun provideEvpRepository(recorderManager: EVPRecorderManager): EvpRepository =
        EvpRepositoryImpl(recorderManager)

    @Provides
    @Singleton
    fun provideCompassRepository(compassManager: CompassManager): CompassRepository =
        CompassRepositoryImpl(compassManager)

    @Provides
    @Singleton
    fun provideLanguageRepository(dataStoreManager: DataStoreManager): LanguageRepository =
        LanguageRepositoryImpl(dataStoreManager = dataStoreManager)
}