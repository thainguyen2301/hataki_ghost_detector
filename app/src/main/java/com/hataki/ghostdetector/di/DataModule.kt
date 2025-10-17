package com.hataki.ghostdetector.di

import com.hataki.ghostdetector.data.framework.compass.CompassManager
import com.hataki.ghostdetector.data.framework.evp.EVPRecorderManager
import com.hataki.ghostdetector.data.framework.sensor.SensorDetectManager
import com.hataki.ghostdetector.data.local.DataStoreManager
import com.hataki.ghostdetector.data.remote.ApiService
import com.hataki.ghostdetector.data.repository.UserRepository
import com.hataki.ghostdetector.data.repository.UserRepositoryImpl
import com.hataki.ghostdetector.data.repository.compass.CompassRepository
import com.hataki.ghostdetector.data.repository.compass.CompassRepositoryImpl
import com.hataki.ghostdetector.data.repository.evp.EvpRepository
import com.hataki.ghostdetector.data.repository.evp.EvpRepositoryImpl
import com.hataki.ghostdetector.data.repository.language.LanguageRepository
import com.hataki.ghostdetector.data.repository.language.LanguageRepositoryImpl
import com.hataki.ghostdetector.data.repository.sensor.SensorRepository
import com.hataki.ghostdetector.data.repository.sensor.SensorRepositoryImpl
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