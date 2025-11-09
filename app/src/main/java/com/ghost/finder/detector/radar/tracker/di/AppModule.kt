package com.ghost.finder.detector.radar.tracker.di

import android.content.Context
import com.ghost.finder.detector.radar.tracker.data.framework.compass.CompassManager
import com.ghost.finder.detector.radar.tracker.data.framework.evp.EVPRecorderManager
import com.ghost.finder.detector.radar.tracker.data.framework.sensor.SensorDetectManager
import com.ghost.finder.detector.radar.tracker.data.local.DataStoreManager
import com.ghost.finder.detector.radar.tracker.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideSensorDetectManager(@ApplicationContext context: Context): SensorDetectManager =
        SensorDetectManager(context)

    @Provides
    @Singleton
    fun provideEVPRecorderManager(): EVPRecorderManager =
        EVPRecorderManager()

    @Provides
    @Singleton
    fun provideCompassManager(@ApplicationContext context: Context): CompassManager =
        CompassManager(context)

    @Provides
    @Singleton
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)
}
