package com.ghost.finder.detector.radar.tracker.ads

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import android.util.Log
import com.google.gson.Gson
import com.ghost.finder.detector.radar.tracker.BuildConfig
import com.ghost.finder.detector.radar.tracker.ads.model.HKTLanguageScreenConfig
import com.ghost.finder.detector.radar.tracker.ads.model.HiddenAdsVersionConfig
import com.mobile.hataki_ad_lib.event_tracker.EventTracker
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

object HKTAdRemoteConfig {
    // Remote Config keys for ad enable/disable
    const val KEY_DISABLE_ALL_ADS = "is_disable_all_ads"
    const val KEY_DISABLE_NATIVE_SPLASH_AD = "is_disable_native_splash_ad"
    const val KEY_DISABLE_NATIVE_INTRO1_FIRST_OPEN_AD = "is_disable_native_intro1_first_open_ad"
    const val KEY_DISABLE_NATIVE_INTRO1_AD = "is_disable_native_intro1_ad"
    const val KEY_DISABLE_NATIVE_INTRO2_FIRST_OPEN_AD = "is_disable_native_intro2_first_open_ad"
    const val KEY_DISABLE_NATIVE_INTRO2_AD = "is_disable_native_intro2_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_MAIN_FIRST_AD = "is_disable_native_language_main_first_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_MAIN_AD = "is_disable_native_language_main_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_FIRST_AD =
        "is_disable_native_language_selected_first_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_AD = "is_disable_native_language_selected_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_DROP_FIRST_AD = "is_disable_native_language_drop_first_ad"
    const val KEY_DISABLE_NATIVE_LANGUAGE_DROP_AD = "is_disable_native_language_drop_ad"
    const val KEY_DISABLE_NATIVE_PERMISSION_AD = "is_disable_native_permission_ad"
    const val KEY_DISABLE_NATIVE_WELCOME_BACK_AD = "is_disable_native_welcome_back_ad"
    const val KEY_DISABLE_NATIVE_HOME_AD = "is_disable_native_home_ad"
    const val KEY_DISABLE_NATIVE_SUCCESS_AD = "is_disable_native_success_ad"
    const val KEY_DISABLE_NATIVE_SMS_AD = "is_disable_native_sms_ad"
    const val KEY_DISABLE_NATIVE_FULL_AD = "is_disable_native_full_ad"
    const val KEY_DISABLE_INTERSTITIAL_SPLASH_AD = "is_disable_interstitial_splash_ad"
    const val KEY_DISABLE_INTERSTITIAL_INTRO_AD = "is_disable_interstitial_intro_ad"
    const val KEY_DISABLE_INTERSTITIAL_HOME_AD = "is_disable_interstitial_home_ad"
    const val KEY_DISABLE_APP_OPEN_AD = "is_disable_app_open_ad"
    const val KEY_DISABLE_BANNER_AD = "is_disable_banner_ad"
    const val KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_INTRO =
        "is_disable_native_full_screen_after_intro"
    const val KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_SPLASH =
        "is_disable_native_full_screen_after_splash"
    const val KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_HOME = "is_disable_native_full_screen_after_home"

    const val KEY_DISABLE_NATIVE_HOME_FUNCTIONS_AD = "is_disable_native_home_functions_ad"

    const val KEY_DISABLE_WALLET_PREVIEW_NATIVE_FULL_AD = "is_disable_wallet_preview_native_full_ad"

    const val KEY_DISABLE_AI_PREVIEW_NATIVE_FULL_AD = """is_disable_ai_preview_native_full_ad"""
    const val KEY_HIDDEN_ADS_VERSION = "hidden_ads_version"

    private const val TAG = "HKTAdRemoteConfig"
    var jsonObject: JSONObject? = null
    private var hiddenAdsVersionConfig: HiddenAdsVersionConfig? = null
    private val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private var languageScreenConfig: HKTLanguageScreenConfig? = null

    // Initialize Firebase Remote Config
    const val KEY_LANGUAGE_SCREEN_CONFIG = "language_screen_config"
    private lateinit var eventTracker: EventTracker
    suspend fun initializeRemoteConfig(eventTracker: EventTracker, context: Context): Boolean {
        // Set default values (optional, can be set in Firebase console or locally)
        this.eventTracker = eventTracker
        val defaults = mapOf(
            KEY_DISABLE_ALL_ADS to false,
            KEY_DISABLE_NATIVE_SPLASH_AD to false,
            KEY_DISABLE_NATIVE_INTRO1_FIRST_OPEN_AD to false,
            KEY_DISABLE_NATIVE_INTRO1_AD to false,
            KEY_DISABLE_NATIVE_INTRO2_FIRST_OPEN_AD to false,
            KEY_DISABLE_NATIVE_INTRO2_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_MAIN_FIRST_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_MAIN_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_FIRST_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_DROP_FIRST_AD to false,
            KEY_DISABLE_NATIVE_LANGUAGE_DROP_AD to false,
            KEY_DISABLE_NATIVE_PERMISSION_AD to false,
            KEY_DISABLE_NATIVE_WELCOME_BACK_AD to false,
            KEY_DISABLE_NATIVE_HOME_AD to false,
            KEY_DISABLE_NATIVE_SUCCESS_AD to false,
            KEY_DISABLE_NATIVE_SMS_AD to false,
            KEY_DISABLE_NATIVE_FULL_AD to false,
            KEY_DISABLE_INTERSTITIAL_SPLASH_AD to false,
            KEY_DISABLE_INTERSTITIAL_INTRO_AD to false,
            KEY_DISABLE_INTERSTITIAL_HOME_AD to false,
            KEY_DISABLE_APP_OPEN_AD to false,
            KEY_DISABLE_BANNER_AD to false,
            KEY_DISABLE_NATIVE_HOME_FUNCTIONS_AD to false,
            KEY_DISABLE_WALLET_PREVIEW_NATIVE_FULL_AD to false,
            KEY_DISABLE_AI_PREVIEW_NATIVE_FULL_AD to false
        )

        // Apply default values
        remoteConfig.setDefaultsAsync(defaults)

        val fetchTime: Long = if (BuildConfig.IS_TEST_AD) 0L else 3600L
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(fetchTime) // Fetch every hour in production, set to 0 for testing
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        return try {
            val result = remoteConfig.fetchAndActivate().await()
            eventTracker.log(context, "Remote config fetched and activated successfully")

            val jsonString = remoteConfig.getString("config_on_off_ads")
            eventTracker.log(context, "HKTEvent === Remote Raw $jsonString ===")
            jsonObject = JSONObject(jsonString)
//            printAllConfigValues(context)

            // Fetch hidden_ads_version config
            fetchHiddenAdsVersionConfig(context)
            fetchLanguageScreenConfig(context)

            result
        } catch (e: Exception) {
            Log.e(TAG, "HKTEvent get remote error due to ${e.toString()}")
            false
        }

    }

    private fun fetchHiddenAdsVersionConfig(context: Context) {
        try {
            val hiddenAdsVersionJson = remoteConfig.getString(KEY_HIDDEN_ADS_VERSION)
            eventTracker.log(context, "HKTEvent === Hidden Ads Version Raw: $hiddenAdsVersionJson ===")

            if (hiddenAdsVersionJson.isNotEmpty()) {
                val gson = Gson()
                hiddenAdsVersionConfig =
                    gson.fromJson(hiddenAdsVersionJson, HiddenAdsVersionConfig::class.java)

                eventTracker.log(
                    context,
                    "HKTEvent === Hidden Ads Versions: ${
                        hiddenAdsVersionConfig?.versions?.joinToString(", ")
                    } ==="
                )
            } else {
                eventTracker.log(context, "HKTEvent === Hidden Ads Version is empty ===")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching hidden_ads_version: ${e.message}", e)
            eventTracker.log(
                context,
                "HKTEvent === Error fetching hidden_ads_version: ${e.message} ==="
            )
        }
    }

    fun isVersionHiddenAds(versionName: String, versionCode: Int): Boolean {
        val versions = hiddenAdsVersionConfig?.versions ?: return false

        // Check both version name and version code string
        val version = "${versionName}_$versionCode"
        return versions.contains(version)
    }

    // Helper function to check if an ad is enabled
    fun isAdEnabled(key: String): Boolean {
        val isDisabled = jsonObject?.getBoolean(key) ?: false
        return !isDisabled
    }

    // Example usage for specific ad types
    fun shouldShowAllAds(): Boolean {
        // First check if current version is in hidden versions list
        // Check both version name (e.g., "1.0.0.1") and version code (e.g., "10")
        if (isVersionHiddenAds(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)) {
            return false
        }
        // If not in hidden versions, check the remote config flag
        return isAdEnabled(KEY_DISABLE_ALL_ADS)
    }

    fun shouldShowMainLanguageNativeAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_LANGUAGE_MAIN_AD)
    fun shouldShowSelectedLanguageNativeAd(): Boolean =
        isAdEnabled(KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_AD)

    fun shouldShowChildLanguageNativeAd(): Boolean =
        isAdEnabled(KEY_DISABLE_NATIVE_LANGUAGE_DROP_AD)

    fun shouldShowNativeSplashAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_SPLASH_AD)
    fun shouldShowInterstitialHomeAd(): Boolean = isAdEnabled(KEY_DISABLE_INTERSTITIAL_HOME_AD)

    fun shouldShowNativeIntro1Ad(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_INTRO1_AD)
    fun shouldShowNativeIntro2Ad(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_INTRO2_AD)
    fun shouldShowNativePermissionAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_PERMISSION_AD)
    fun shouldShowNativeWelcomeBackAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_WELCOME_BACK_AD)
    fun shouldShowNativeHomeAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_HOME_AD)
    fun shouldShowNativeFullAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_FULL_AD)
    fun shouldShowInterstitialSplashAd(): Boolean = isAdEnabled(KEY_DISABLE_INTERSTITIAL_SPLASH_AD)
    fun shouldShowInterstitialIntroAd(): Boolean = isAdEnabled(KEY_DISABLE_INTERSTITIAL_INTRO_AD)
    fun shouldShowAppOpenAd(): Boolean = isAdEnabled(KEY_DISABLE_APP_OPEN_AD)
    fun shouldShowBannerAd(): Boolean = isAdEnabled(KEY_DISABLE_BANNER_AD)
    fun shouldShowNativeFullScreenAfterIntro(): Boolean =
        isAdEnabled(KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_INTRO)

    fun shouldShowNativeFullScreenAfterSplash(): Boolean =
        isAdEnabled(KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_SPLASH)

    fun shouldShowNativeFullScreenAfterHome(): Boolean =
        isAdEnabled(KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_HOME)

    fun shouldShowHomeFunctionsAd(): Boolean = isAdEnabled(KEY_DISABLE_NATIVE_HOME_FUNCTIONS_AD)

    fun shouldShowWalletPreviewNativeFullAd(): Boolean =
        isAdEnabled(KEY_DISABLE_WALLET_PREVIEW_NATIVE_FULL_AD)

    fun shouldShowAIPreviewNativeFullAd(): Boolean =
        isAdEnabled(KEY_DISABLE_AI_PREVIEW_NATIVE_FULL_AD)
    // Add similar functions for other ad types as needed

    fun printAllConfigValues(context: Context) {

        val keys = listOf(
            KEY_DISABLE_ALL_ADS,
            KEY_DISABLE_NATIVE_SPLASH_AD,
            KEY_DISABLE_NATIVE_INTRO1_FIRST_OPEN_AD,
            KEY_DISABLE_NATIVE_INTRO1_AD,
            KEY_DISABLE_NATIVE_INTRO2_FIRST_OPEN_AD,
            KEY_DISABLE_NATIVE_INTRO2_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_MAIN_FIRST_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_MAIN_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_FIRST_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_SELECTED_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_DROP_FIRST_AD,
            KEY_DISABLE_NATIVE_LANGUAGE_DROP_AD,
            KEY_DISABLE_NATIVE_PERMISSION_AD,
            KEY_DISABLE_NATIVE_WELCOME_BACK_AD,
            KEY_DISABLE_NATIVE_HOME_AD,
            KEY_DISABLE_NATIVE_SUCCESS_AD,
            KEY_DISABLE_NATIVE_SMS_AD,
            KEY_DISABLE_NATIVE_FULL_AD,
            KEY_DISABLE_INTERSTITIAL_SPLASH_AD,
            KEY_DISABLE_INTERSTITIAL_INTRO_AD,
            KEY_DISABLE_INTERSTITIAL_HOME_AD,
            KEY_DISABLE_APP_OPEN_AD,
            KEY_DISABLE_BANNER_AD,
            KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_INTRO,
            KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_SPLASH,
            KEY_DISABLE_NATIVE_FULL_SCREEN_AFTER_HOME
        )

        keys.forEach { key ->
            val value = jsonObject?.getBoolean(key)
            value?.let { eventTracker.log(context, "HKTEvent $key: $value (Ad Enabled: ${!it})") }
        }
        eventTracker.log(context, "===========================")
    }

    private fun fetchLanguageScreenConfig(context: Context) {
        try {
            val languageScreenConfigJson = remoteConfig.getString(KEY_LANGUAGE_SCREEN_CONFIG)
            eventTracker.log(
                context,
                "HKTEvent === Language Screen Config Raw: $languageScreenConfigJson ==="
            )

            if (languageScreenConfigJson.isNotEmpty()) {
                val gson = Gson()
                languageScreenConfig =
                    gson.fromJson(languageScreenConfigJson, HKTLanguageScreenConfig::class.java)

                eventTracker.log(
                    context, "HKTEvent === Language Screen Config: " +
                            "position=${languageScreenConfig?.doneButtonPosition}, " +
                            "showAfter=${languageScreenConfig?.doneButtonShowAfterSecond}s, " +
                            "showPopUp=${languageScreenConfig?.isShowTranslatingPopUp}, " +
                            "ignoreVersions=${languageScreenConfig?.ignoreVersions?.joinToString(", ")} ==="
                )
            } else {
                eventTracker.log(
                    context,
                    "HKTEvent === Language Screen Config is empty, using defaults ==="
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching language_screen_config: ${e.message}", e)
            eventTracker.log(
                context,
                "HKTEvent === Error fetching language_screen_config: ${e.message} ==="
            )
        }
    }

    fun getLanguageScreenConfig(): HKTLanguageScreenConfig {
        val version = BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE
        languageScreenConfig?.let {
            if (it.ignoreVersions?.contains(version) == true) {
                return HKTLanguageScreenConfig.default
            }
            return it
        }

        return HKTLanguageScreenConfig.default
    }

    /**
     * Get the order position for a language code.
     * Returns the order from language screen config if available, otherwise returns default value.
     * Lower numbers appear first in the list.
     * @param languageCode The language code (e.g., "en", "pt", "es")
     * @param defaultOrder The default order to use if not found in remote config (default is 999)
     * @return The order position for the language
     */
    fun getLanguageOrder(languageCode: String, defaultOrder: Int = 999): Int {
        return languageScreenConfig?.languageOrderMain?.get(languageCode) ?: defaultOrder
    }
}