package com.ghostfinder.ghostdetector.radar.ads

import com.ghostfinder.ghostdetector.radar.BuildConfig


object AdConfig {
    private const val IS_DEV_MODE = BuildConfig.IS_TEST_AD // Set to false for release mode
    private var isFirstTimeOpenApp = false

    object AdUnitIds {
        // Native Ad Unit IDs
        const val NATIVE_SPLASH_AD_UNIT_ID = "ca-app-pub-9094493509224480/7654105252"

        const val NATIVE_FULL_AFTER_SPLASH_INTER = "" //M
        const val NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-9094493509224480/1060421527" //M

        const val NATIVE_INTRO1_AD_UNIT_ID = "ca-app-pub-9094493509224480/1060421527"//M

        const val NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID = ""//M

        const val NATIVE_INTRO2_AD_UNIT_ID = ""//M

        // Native language
        //MAIN
        const val NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID = "ca-app-pub-9094493509224480/5594562907" // M
        const val NATIVE_LANGUAGE_MAIN_AD_UNIT_ID = "ca-app-pub-9094493509224480/5594562907" // M

        //SELECTED
        const val NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID = ""
        const val NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID = "" //M

        //DROP
        const val NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID = "" // M
        const val NATIVE_LANGUAGE_DROP_AD_UNIT_ID = ""

        const val NATIVE_FULL_SCREEN_AFTER_INTER_HOME = "" //M
        const val NATIVE_PERMISSION_AD_UNIT_ID = "ca-app-pub-9094493509224480/7837582865" //M
        const val NATIVE_WELCOME_BACK_AD_UNIT_ID = "ca-app-pub-9094493509224480/2308363511" //M

        const val NATIVE_FULL_AD_UNIT_ID = "ca-app-pub-9094493509224480/9150664530" //M

        // Interstitial Ad Unit IDs
        const val INTERSTITIAL_SPLASH_AD_UNIT_ID = "ca-app-pub-9094493509224480/7298526817" //M
        const val INTERSTITIAL_INTRO_AD_UNIT_ID = "ca-app-pub-9094493509224480/8643398070" //M
        const val INTERSTITIAL_HOME_AD_UNIT_ID = "ca-app-pub-9094493509224480/2162939573"//M
        const val INTERSTITIAL_BACK_AD_UNIT_ID = ""
        const val INTERSTITIAL_SUCCESS_AD_UNIT_ID = ""
        // App Open Ad Unit ID
        const val INTER_OPEN_AD_UNIT_ID = "ca-app-pub-9094493509224480/2102040452"

        // Banner Ad Unit ID
        const val BANNER_AD_UNIT_ID = "ca-app-pub-9094493509224480/3355412378" //M

        const val HOME_BANNER_AD_UNIT_ID = "ca-app-pub-9094493509224480/2641096501" //M
        const val HOME_DIALOG_AD_UNIT_ID = ""//M

        const val NATIVE_FULL_SCREEN_AFTER_INTRO = ""

        const val OPEN_RESUME_SPLASH_UNIT_ID = "ca-app-pub-9094493509224480/4528893246"
        const val APP_OPEN_AD_UNIT_ID = ""
        const val NATIVE_SUCCESS_AD_UNIT_ID = ""


        // Dev mode test IDs (same as above for this example, replace with actual test IDs in dev)
        const val DEV_NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO1_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO2_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_FULL_AFTER_SPLASH_INTER = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_FULL_SCREEN_AFTER_INTER_HOME = "ca-app-pub-3940256099942544/2247696110"

        // Native language

        const val DEV_NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID =
            "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_MAIN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID =
            "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID =
            "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_DROP_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_SPLASH_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_INTERSTITIAL_SPLASH_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_INTRO_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_HOME_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_BACK_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_SUCCESS_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_NATIVE_PERMISSION_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_WELCOME_BACK_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_FULL_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_INTER_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val DEV_HOME_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

        const val DEV_HOME_DIALOG_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_FULL_SCREEN_AFTER_INTRO = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_OPEN_RESUME_SPLASH_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
        const val DEV_APP_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"
        const val DEV_NATIVE_SUCCESS_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    }

    fun load(isFirstOpenApp: Boolean) {
        this.isFirstTimeOpenApp = isFirstOpenApp
    }

    val nativeFullAfterHomeInter: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_FULL_SCREEN_AFTER_INTER_HOME else AdUnitIds.NATIVE_FULL_SCREEN_AFTER_INTER_HOME

    // Getters for ad unit IDs based on mode
    val nativeSplashAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_SPLASH_AD_UNIT_ID else AdUnitIds.NATIVE_SPLASH_AD_UNIT_ID

    val nativeFullScreenAfterIntroInter: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_FULL_SCREEN_AFTER_INTRO else AdUnitIds.NATIVE_FULL_SCREEN_AFTER_INTRO

    val nativeFullAfterSplashInter: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_FULL_AFTER_SPLASH_INTER else AdUnitIds.NATIVE_FULL_AFTER_SPLASH_INTER

    val nativeFullAfterIntroInter: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_FULL_SCREEN_AFTER_INTRO else AdUnitIds.NATIVE_FULL_SCREEN_AFTER_INTRO

    fun getNativeIntro1AdUnitId(): String {
        if (isFirstTimeOpenApp) {
            return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID else AdUnitIds.NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID
        }

        return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_INTRO1_AD_UNIT_ID else AdUnitIds.NATIVE_INTRO1_AD_UNIT_ID
    }

    fun getNativeIntro4AdUnitId(): String {
        if (isFirstTimeOpenApp) {
            return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID else AdUnitIds.NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID
        }

        return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_INTRO2_AD_UNIT_ID else AdUnitIds.NATIVE_INTRO2_AD_UNIT_ID
    }

    fun getNativeLanguageMainAdUnitId(): String {
        if (isFirstTimeOpenApp) {
            return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID
        }

        return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_MAIN_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_MAIN_AD_UNIT_ID
    }

    fun getNativeLanguageSelectedAdUnitId(): String {
        if (isFirstTimeOpenApp) {
            return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID
        }

        return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID
    }

    fun getNativeLanguageDropAdUnitId(): String {
        if (isFirstTimeOpenApp) {
            return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID
        }

        return if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_LANGUAGE_DROP_AD_UNIT_ID else AdUnitIds.NATIVE_LANGUAGE_DROP_AD_UNIT_ID
    }

    val interstitialSplashAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTERSTITIAL_SPLASH_AD_UNIT_ID else AdUnitIds.INTERSTITIAL_SPLASH_AD_UNIT_ID

    val interstitialIntroAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTERSTITIAL_INTRO_AD_UNIT_ID else AdUnitIds.INTERSTITIAL_INTRO_AD_UNIT_ID

    val interstitialHomeAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTERSTITIAL_HOME_AD_UNIT_ID else AdUnitIds.INTERSTITIAL_HOME_AD_UNIT_ID

    val nativePermissionAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_PERMISSION_AD_UNIT_ID else AdUnitIds.NATIVE_PERMISSION_AD_UNIT_ID

    val nativeWelcomeBackAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_WELCOME_BACK_AD_UNIT_ID else AdUnitIds.NATIVE_WELCOME_BACK_AD_UNIT_ID


    val nativeFullAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_FULL_AD_UNIT_ID else AdUnitIds.NATIVE_FULL_AD_UNIT_ID
    val interOpenAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTER_OPEN_AD_UNIT_ID else AdUnitIds.INTER_OPEN_AD_UNIT_ID

    val bannerAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_BANNER_AD_UNIT_ID else AdUnitIds.BANNER_AD_UNIT_ID

    val homeBannerAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_HOME_BANNER_AD_UNIT_ID else AdUnitIds.HOME_BANNER_AD_UNIT_ID

    val nativeHomeDialogUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_HOME_DIALOG_AD_UNIT_ID else AdUnitIds.HOME_DIALOG_AD_UNIT_ID

    val splashOpenResumeUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_OPEN_RESUME_SPLASH_UNIT_ID else AdUnitIds.OPEN_RESUME_SPLASH_UNIT_ID
    val appOpenAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_APP_OPEN_AD_UNIT_ID else AdUnitIds.APP_OPEN_AD_UNIT_ID
    val nativeSuccessAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_SUCCESS_AD_UNIT_ID else AdUnitIds.NATIVE_SUCCESS_AD_UNIT_ID

    val interstitialBackAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTERSTITIAL_BACK_AD_UNIT_ID else AdUnitIds.INTERSTITIAL_BACK_AD_UNIT_ID

    val interstitialSuccessAdUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_INTERSTITIAL_SUCCESS_AD_UNIT_ID else AdUnitIds.INTERSTITIAL_SUCCESS_AD_UNIT_ID
}
