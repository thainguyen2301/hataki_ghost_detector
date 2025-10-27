package com.ghostfinder.ghostdetector.radar.di

import com.ghostfinder.ghostdetector.radar.data.framework.compass.CompassManager
import com.ghostfinder.ghostdetector.radar.data.framework.evp.EVPRecorderManager
import com.ghostfinder.ghostdetector.radar.data.framework.sensor.SensorDetectManager
import com.ghostfinder.ghostdetector.radar.data.local.DataStoreManager
import com.ghostfinder.ghostdetector.radar.data.remote.ApiService
import com.ghostfinder.ghostdetector.radar.data.repository.UserRepository
import com.ghostfinder.ghostdetector.radar.data.repository.UserRepositoryImpl
import com.ghostfinder.ghostdetector.radar.data.repository.compass.CompassRepository
import com.ghostfinder.ghostdetector.radar.data.repository.compass.CompassRepositoryImpl
import com.ghostfinder.ghostdetector.radar.data.repository.evp.EvpRepository
import com.ghostfinder.ghostdetector.radar.data.repository.evp.EvpRepositoryImpl
import com.ghostfinder.ghostdetector.radar.data.repository.language.LanguageRepository
import com.ghostfinder.ghostdetector.radar.data.repository.language.LanguageRepositoryImpl
import com.ghostfinder.ghostdetector.radar.data.repository.sensor.SensorRepository
import com.ghostfinder.ghostdetector.radar.data.repository.sensor.SensorRepositoryImpl
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