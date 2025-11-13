package com.ghost.finder.detector.radar.tracker.ads

import com.ghost.finder.detector.radar.tracker.BuildConfig


object HKTAdConfig {
    private const val IS_DEV_MODE = BuildConfig.IS_TEST_AD // Set to false for release mode
    private var isFirstTimeOpenApp = false
    object AdUnitIds {
        // Native Ad Unit IDs
        const val NATIVE_SPLASH_AD_UNIT_ID = "ca-app-pub-5457807375754173/9318410835" //M

        const val NATIVE_FULL_AFTER_SPLASH_INTER = "ca-app-pub-5457807375754173/2753002483" //M
        const val NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-5457807375754173/4133532767" //M

        const val NATIVE_INTRO1_AD_UNIT_ID = "ca-app-pub-5457807375754173/4490849311"//M

        const val NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-5457807375754173/1056777433"//M

        const val NATIVE_INTRO2_AD_UNIT_ID = "ca-app-pub-5457807375754173/7430614092"//M

        // Native language
            //MAIN
        const val NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID = "ca-app-pub-5457807375754173/4584673499" // M
        const val NATIVE_LANGUAGE_MAIN_AD_UNIT_ID = "ca-app-pub-5457807375754173/3874512460" // M
            //SELECTED
        const val NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID = "ca-app-pub-5457807375754173/6500675805"
        const val NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID = "ca-app-pub-5457807375754173/7622185784" //M
            //DROP
        const val NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID = "ca-app-pub-5457807375754173/3682940773" // M
        const val NATIVE_LANGUAGE_DROP_AD_UNIT_ID = "ca-app-pub-5457807375754173/3223775771"

        const val NATIVE_FULL_SCREEN_AFTER_INTER_HOME = "ca-app-pub-5457807375754173/1791274605" //M
        const val NATIVE_PERMISSION_AD_UNIT_ID = "ca-app-pub-5457807375754173/9781159859" //M
        const val NATIVE_WELCOME_BACK_AD_UNIT_ID = "ca-app-pub-5457807375754173/3010548005" //M

        const val NATIVE_FULL_AD_UNIT_ID = "ca-app-pub-5457807375754173/3104356272" //M

        const val NATIVE_HOME_FUNCTIONS_AD_UNIT_ID = ""//M

        const val NATIVE_WALLET_PREVIEW_AD_UNIT_ID = "" //M
        const val NATIVE_AI_PREVIEW_AD_UNIT_ID = "" //M

        const val NATIVE_VIDEO_PREVIEW_AD_UNIT_ID = ""//M

        // Interstitial Ad Unit IDs
        const val INTERSTITIAL_SPLASH_AD_UNIT_ID = "ca-app-pub-5457807375754173/9837000178" //M
        const val INTERSTITIAL_INTRO_AD_UNIT_ID = "ca-app-pub-5457807375754173/2888448442" //M
        const val INTERSTITIAL_HOME_AD_UNIT_ID = "ca-app-pub-5457807375754173/9230780928"//M
        // App Open Ad Unit ID
        const val INTER_OPEN_AD_UNIT_ID = "ca-app-pub-5457807375754173/1697466331"

        // Banner Ad Unit ID
        const val BANNER_AD_UNIT_ID = "ca-app-pub-5457807375754173/9855157952" //M

        const val HOME_BANNER_AD_UNIT_ID = "ca-app-pub-5457807375754173/3970731298" //M
        const val HOME_DIALOG_AD_UNIT_ID = "ca-app-pub-5457807375754173/6925440966"//M

        const val NATIVE_FULL_SCREEN_AFTER_INTRO = "ca-app-pub-5457807375754173/6796189276"

        // Dev mode test IDs (same as above for this example, replace with actual test IDs in dev)
        const val DEV_NATIVE_INTRO1_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO1_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO2_FIRST_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_INTRO2_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_FULL_AFTER_SPLASH_INTER = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_FULL_SCREEN_AFTER_INTER_HOME = "ca-app-pub-3940256099942544/2247696110"

        // Native language

        const val DEV_NATIVE_LANGUAGE_MAIN_FIRST_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_MAIN_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_LANGUAGE_SELECTED_FIRST_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_SELECTED_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_LANGUAGE_DROP_FIRST_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_LANGUAGE_DROP_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_HOME_FUNCTIONS_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_WALLET_PREVIEW_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_AI_PREVIEW_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_VIDEO_PREVIEW_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_SPLASH_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_INTERSTITIAL_SPLASH_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_INTRO_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_INTERSTITIAL_HOME_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

        const val DEV_NATIVE_PERMISSION_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_WELCOME_BACK_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"

        const val DEV_NATIVE_FULL_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_INTER_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        const val DEV_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
        const val DEV_HOME_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

        const val DEV_HOME_DIALOG_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
        const val DEV_NATIVE_FULL_SCREEN_AFTER_INTRO = "ca-app-pub-3940256099942544/2247696110"
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

    val nativeHomeFunctionsUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_HOME_FUNCTIONS_AD_UNIT_ID else AdUnitIds.NATIVE_HOME_FUNCTIONS_AD_UNIT_ID

    val nativePreviewWallPaperUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_WALLET_PREVIEW_AD_UNIT_ID else AdUnitIds.NATIVE_WALLET_PREVIEW_AD_UNIT_ID

    val nativePreviewAIUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_AI_PREVIEW_AD_UNIT_ID else AdUnitIds.NATIVE_AI_PREVIEW_AD_UNIT_ID

    val videoPreviewAIUnitId: String
        get() = if (IS_DEV_MODE) AdUnitIds.DEV_NATIVE_VIDEO_PREVIEW_AD_UNIT_ID else AdUnitIds.NATIVE_VIDEO_PREVIEW_AD_UNIT_ID
}
