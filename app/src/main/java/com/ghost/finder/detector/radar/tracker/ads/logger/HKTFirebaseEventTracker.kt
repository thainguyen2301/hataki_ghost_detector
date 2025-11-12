package com.ghost.finder.detector.radar.tracker.ads.logger

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.ghost.finder.detector.radar.tracker.BuildConfig
import com.mobile.hataki_ad_lib.event_tracker.EventTracker

class HKTFirebaseEventTracker : EventTracker {

    companion object {
        private const val TAG = "HKTFirebaseEventTracker"
    }

    private var androidContext: Context? = null

    fun setAndroidContext(context: Context) {
        this.androidContext = context.applicationContext
    }

    override fun log(context: Context?, name: String, bundle: Bundle) {
        val ctx = context ?: androidContext

        if (ctx == null) {
            HKTAppAdvertiseManager.fileLogger.log(null,"Both context and androidContext are null, cannot log Firebase event: $name")
            return
        }

        try {
            val enrichedBundle = Bundle(bundle).apply {
                putString("app_version", BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE)
                putString("device_id", getDeviceId(ctx))
            }

            FirebaseAnalytics.getInstance(ctx).logEvent(name, enrichedBundle)

            HKTAppAdvertiseManager.fileLogger.log(ctx, name, enrichedBundle)
        } catch (e: Exception) {
            HKTAppAdvertiseManager.fileLogger.log(ctx, name, "Error logging Firebase event: ${e.message}")
        }
    }

    private fun getDeviceId(context: Context): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
        } catch (e: Exception) {
            Log.e(TAG, "Error getting device ID: ${e.message}")
            "unknown"
        }
    }
}