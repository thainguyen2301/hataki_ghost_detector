package com.ghost.finder.detector.radar.tracker.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.ghost.finder.detector.radar.tracker.ads.logger.HKTFirebaseEventTracker
import com.ghost.finder.detector.radar.tracker.ads.logger.HKTFileLogger
import com.ghost.finder.detector.radar.tracker.BuildConfig
import com.ghost.finder.detector.radar.tracker.R
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenAdListener
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenAdModel
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenBaseAdProducer
import com.mobile.hataki_ad_lib.ad_banner.BannerAdModel
import com.mobile.hataki_ad_lib.ad_banner.BannerBaseAdProducer
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdModel
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdProducer
import com.mobile.hataki_ad_lib.ad_manager.AdManager
import com.mobile.hataki_ad_lib.ad_native.NativeAdModel
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import com.mobile.hataki_ad_lib.adjust_tracking.AdjustEnvironment
import com.mobile.hataki_ad_lib.adjust_tracking.AdjustTracking

object HKTAppAdvertiseManager {

    var splashNativeProducer: NativeBaseAdProducer? = null
        private set
    var mainLanguageNativeProducer: NativeBaseAdProducer? = null
        private set
    var selectedLanguageNativeProducer: NativeBaseAdProducer? = null
        private set
    var selectedChildLanguageNativeProducer: NativeBaseAdProducer? = null
        private set

    var firstIntroNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var lastIntroNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var fullScreenIntroNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var interSplashAdProducer: InterstitialAdProducer? = null
        private set
    var interIntroAdProducer: InterstitialAdProducer? = null
        private set

    var permissionNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var welcomeBackNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var homeInterstitialAdProducer: InterstitialAdProducer? = null

    var interOpenResumeAd: InterstitialAdProducer? = null
        private set

    var homeFunctionsNativeAdProducer: NativeBaseAdProducer? = null
        private set

    val fileLogger = HKTFileLogger()
    private val firebaseTracker = HKTFirebaseEventTracker()

    var currentNativeFullScreen: NativeBaseAdProducer? = null

    var previewWalletNativeProducer: NativeBaseAdProducer? = null
    var previewVideoNativeProducer: NativeBaseAdProducer? = null
    var previewAINativeProducer: NativeBaseAdProducer? = null

    var homeNativeDialogAd: NativeBaseAdProducer? = null

    private var isAllowShowAd = false
    fun initialize(context: Context) {
        isAllowShowAd = HKTAdRemoteConfig.shouldShowAllAds()
        firebaseTracker.setAndroidContext(context)
        val eventToken = context.getString(R.string.adjust_token_event)
        val appToken = context.getString(R.string.adjust_token)
        val adjustEnv = if (BuildConfig.IS_TEST_AD) AdjustEnvironment.SANDBOX else AdjustEnvironment.PRODUCTION
        AdManager.instance.setupEventTracker(firebaseTracker)
        AdManager.instance.setupAdjustTracking(object : AdjustTracking.AdjustConfigProvider {
            override fun getEventToken(): String {
                return eventToken
            }

            override fun getAppToken(): String {
                return appToken
            }
        }, environment = adjustEnv)
    }

    fun loadMainLanguageNative(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadMainLanguageNative not call because isAllowShowAd = false")
            return
        }

        if (mainLanguageNativeProducer != null) {
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadMainLanguageNative called")

        if (HKTAdRemoteConfig.shouldShowMainLanguageNativeAd()) {
            val mainNativeModel = NativeAdModel(
                adUnitId = HKTAdConfig.getNativeLanguageMainAdUnitId(),
                adName = "language_main",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true
            )
            mainLanguageNativeProducer =
                AdManager.instance.produceNativeAd(context, mainNativeModel)
        } else {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent shouldShowMainLanguageNativeAd not call because shouldShowMainLanguageNativeAd = false")
        }
    }

    fun loadSelectLanguageNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadSelectLanguageNativeAd not call because isAllowShowAd = false")
            return
        }

        if (selectedLanguageNativeProducer != null) {
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadSelectLanguageNativeAd called")

        if (HKTAdRemoteConfig.shouldShowSelectedLanguageNativeAd()) {
            val selectedNativeModel = NativeAdModel(
                adUnitId = HKTAdConfig.getNativeLanguageSelectedAdUnitId(),
                adName = "language_selected",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true
            )
            selectedLanguageNativeProducer =
                AdManager.instance.produceNativeAd(context, selectedNativeModel)
        } else {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent shouldShowSelectedLanguageNativeAd not call because shouldShowSelectedLanguageNativeAd = false")
        }

    }
    fun loadDropLanguageNativeAds(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadDropLanguageNativeAds not call because isAllowShowAd = false")
            return
        }

        if (selectedChildLanguageNativeProducer != null) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadDropLanguageNativeAds not call because there is another loading process")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadDropLanguageNativeAds called")

        if (HKTAdRemoteConfig.shouldShowChildLanguageNativeAd()) {
            val selectedChildNativeModel = NativeAdModel( adUnitId = HKTAdConfig.getNativeLanguageDropAdUnitId(),
                adName = "language_drop",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true)
            selectedChildLanguageNativeProducer = AdManager.instance.produceNativeAd( context, selectedChildNativeModel)
        } else {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent not call drop native ads shouldShowChildLanguageNativeAd = false")
        }
    }

    fun loadSplashNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowNativeSplashAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent not loadSplashNativeAds because isAllowShowAd = $isAllowShowAd and shouldShowNativeSplashAd = ${HKTAdRemoteConfig.shouldShowNativeSplashAd()}")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent not call load splash native")

        val splashNativeModel = NativeAdModel(adUnitId = HKTAdConfig.nativeSplashAdUnitId,
            adName = "splash",
            resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
            adType = "medium",
            isShowAd = true
        )
        splashNativeProducer = AdManager.instance.produceNativeAd(context, splashNativeModel)
    }

    fun loadFirstIntroNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadFirstIntroNativeAd not call because isAllowShowAd = $isAllowShowAd ")
            return
        }

        if (firstIntroNativeAdProducer != null) {
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadFirstIntroNativeAd called")
        if (HKTAdRemoteConfig.shouldShowNativeIntro1Ad()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadFirstIntroNativeAd called")
            val firstIntroNativeModel = NativeAdModel(
                adUnitId = HKTAdConfig.getNativeIntro1AdUnitId(),
                adName = "intro_1",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )
            firstIntroNativeAdProducer =
                AdManager.instance.produceNativeAd(context, firstIntroNativeModel)
        } else {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadFirstIntroNativeAd not call because loadFirstIntroNativeAd = ${HKTAdRemoteConfig.shouldShowNativeIntro1Ad()}")
        }
    }

    fun loadLastIntroNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadLastIntroNativeAd not call because isAllowShowAd = $isAllowShowAd ")
            return
        }

        if (lastIntroNativeAdProducer != null) {
            return
        }

        if (HKTAdRemoteConfig.shouldShowNativeIntro2Ad()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadLastIntroNativeAd called")
            val lastIntroNativeModel = NativeAdModel(
                adUnitId = HKTAdConfig.getNativeIntro4AdUnitId(),
                adName = "intro_2",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )
            lastIntroNativeAdProducer =
                AdManager.instance.produceNativeAd(context, lastIntroNativeModel)
        } else {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadLastIntroNativeAd not call because loadLastIntroNativeAd = ${HKTAdRemoteConfig.shouldShowNativeIntro2Ad()}")
        }
    }

    fun loadInterstitialSplashAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowInterstitialSplashAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterstitialSplashAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialSplashAd = ${HKTAdRemoteConfig.shouldShowInterstitialSplashAd()}")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterstitialSplashAd called")

        var fullNativeAfterSplashInterModel: NativeAdModel? = null
        if (HKTAdRemoteConfig.shouldShowNativeFullScreenAfterSplash()) {
            fullNativeAfterSplashInterModel = NativeAdModel(
                adUnitId = HKTAdConfig.nativeFullAfterSplashInter,
                adName = "splash_end_card",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )
        }

        val interSplashModel = InterstitialAdModel(
            adId = HKTAdConfig.interstitialSplashAdUnitId,
            adName = "splash_inter",
            adMode = "load_only",
            nativeFullScreenAdModel = fullNativeAfterSplashInterModel
        )
        interSplashAdProducer = AdManager.instance.produceInterstitialAd(interSplashModel)
        interSplashAdProducer?.load(context)
    }

    fun loadInterstitialIntroAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowInterstitialIntroAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterstitialIntroAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialIntroAd = ${HKTAdRemoteConfig.shouldShowInterstitialIntroAd()}")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterstitialIntroAd called")

        var fullNativeAfterIntroInterModel: NativeAdModel? = null
        if (HKTAdRemoteConfig.shouldShowNativeFullScreenAfterIntro()) {
            fullNativeAfterIntroInterModel = NativeAdModel(
                adUnitId = HKTAdConfig.nativeFullAfterIntroInter,
                adName = "intro_full",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )

            fileLogger.log(context, tag = "HKTAppAdvertiseManager","HKTEvent shouldShowNativeFullScreenAfterIntro = ${HKTAdRemoteConfig.shouldShowNativeFullScreenAfterIntro()}")
        }

        val interIntroModel = InterstitialAdModel(
            adId = HKTAdConfig.interstitialIntroAdUnitId,
            adName = "intro_inter",
            adMode = "load_only",
            nativeFullScreenAdModel = fullNativeAfterIntroInterModel
        )
        interIntroAdProducer = AdManager.instance.produceInterstitialAd(interIntroModel)
        interIntroAdProducer?.load(context)
    }

    fun loadPermissionNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowNativePermissionAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadPermissionNativeAds not call because isAllowShowAd = $isAllowShowAd and shouldShowNativePermissionAd = ${HKTAdRemoteConfig.shouldShowNativePermissionAd()}")
            return
        }

        permissionNativeAdProducer?.let {
            it.loadAdOnly(context)
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadPermissionNativeAds called")

        val permissionNativeModel = NativeAdModel( adUnitId = HKTAdConfig.nativePermissionAdUnitId,
            adName = "permission",
            resShimmerId = R.layout.layout_native_ad_medium_button_bottom,
            adType = "medium",
            isShowAd = true)
        permissionNativeAdProducer = AdManager.instance.produceNativeAd(context, permissionNativeModel)
    }

    fun loadWelcomeBackNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowNativeWelcomeBackAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadWelcomeBackNativeAds not call because isAllowShowAd = $isAllowShowAd and shouldShowNativeWelcomeBackAd = ${HKTAdRemoteConfig.shouldShowNativeWelcomeBackAd()}")
            return
        }

        welcomeBackNativeAdProducer?.let {
            it.loadAdOnly(context)
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadWelcomeBackNativeAds called")

        val welcomeBackNativeModel = NativeAdModel( adUnitId = HKTAdConfig.nativeWelcomeBackAdUnitId,
            adName = "welcome_back",
            resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
            adType = "medium",
            isShowAd = true)
        welcomeBackNativeAdProducer = AdManager.instance.produceNativeAd(context, welcomeBackNativeModel)
    }

    fun loadInterOpenResumeAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowAppOpenAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterOpenResumeAd not call because isAllowShowAd = $isAllowShowAd and loadInterOpenResumeAd = ${HKTAdRemoteConfig.shouldShowAppOpenAd()}")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadInterOpenResumeAd called")
        if (interOpenResumeAd != null) {
            interOpenResumeAd!!.load(context)
            return
        }

        val interResumeModel = InterstitialAdModel(
            adId = HKTAdConfig.interOpenAdUnitId,
            adName = "inter_resume", adMode = "load_only"
        )
        interOpenResumeAd = AdManager.instance.produceInterstitialAd(interResumeModel)

        interOpenResumeAd?.load(context)
    }

    fun loadHomeInterstitialAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowInterstitialHomeAd().not()) {
            fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadHomeInterstitialAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialHomeAd = ${HKTAdRemoteConfig.shouldShowInterstitialHomeAd()}")
            return
        }

        fileLogger.log(context,"HKTAppAdvertiseManager", "HKTEvent loadHomeInterstitialAd called")
        homeInterstitialAdProducer?.let {
            it.load(context)
            return
        }


        var nativeFullScreenAfterHomeInterModel: NativeAdModel? = null
        if (HKTAdRemoteConfig.shouldShowNativeFullScreenAfterHome()) {
            nativeFullScreenAfterHomeInterModel = NativeAdModel(
                adUnitId = HKTAdConfig.nativeFullAfterHomeInter,
                adName = "home_end_card",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )

            fileLogger.log(context, tag = "HKTAppAdvertiseManager","HKTEvent shouldShowNativeFullScreenAfterHome = ${HKTAdRemoteConfig.shouldShowNativeFullScreenAfterHome()}")
        }

        val interHomeModel = InterstitialAdModel(
            adId = HKTAdConfig.interstitialHomeAdUnitId,
            adMode = "load_only",
            adName = "home",
            nativeFullScreenAdModel = nativeFullScreenAfterHomeInterModel,
            shouldPreloadAfterShown = true
        )
        homeInterstitialAdProducer = AdManager.instance.produceInterstitialAd(interHomeModel)
        homeInterstitialAdProducer?.load(context)
    }

    fun showAdaptiveBanner(context: Context, container: ViewGroup): BannerBaseAdProducer? {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowBannerAd().not()) {
            return null
        }

        val bannerAdModel = BannerAdModel(
            adUnitId = HKTAdConfig.bannerAdUnitId,
            resShimmerId = R.layout.layout_banner_ad_shimmer,
            adType = "adaptive",
            isShowAd = true
        )

        return AdManager.instance.produceBannerAd( context, container = container, bannerAdModel)
    }

    fun showAdaptiveHomeBanner(context: Context, container: ViewGroup): BannerBaseAdProducer? {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowBannerAd().not()) {
            return null
        }

        val bannerAdModel = BannerAdModel(
            adUnitId = HKTAdConfig.homeBannerAdUnitId,
            resShimmerId = R.layout.layout_banner_ad_shimmer,
            adType = "adaptive",
            isShowAd = true
        )

        return AdManager.instance.produceBannerAd( context, container = container, bannerAdModel)
    }

    fun loadIntroFullScreenNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowNativeFullAd().not()) {
            return
        }

        if (fullScreenIntroNativeAdProducer != null) {
            fullScreenIntroNativeAdProducer!!.loadAdOnly(context)
            return
        }

        val fullScreenNativeModel = NativeAdModel( adUnitId = HKTAdConfig.nativeFullAdUnitId,
            adName = "intro_full",
            resShimmerId = R.layout.layout_native_ad_large_button_top_shimmer,
            adType = "medium",
            isShowAd = true,
        )
        fullScreenIntroNativeAdProducer = AdManager.instance.produceNativeAd(context, fullScreenNativeModel)
    }

    fun loadHomeNativeDialogAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowNativeHomeAd().not()) {
            return
        }

        if (homeNativeDialogAd != null) {
            homeNativeDialogAd!!.loadAdOnly(context, homeNativeDialogAd!!.isAdAutoLoaded)
            return
        }

        val homeDialogNativeAdModel = NativeAdModel(
            adUnitId = HKTAdConfig.nativeHomeDialogUnitId,
            adName = "home_dialog",
            resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
            adType = "medium",
            isShowAd = true
        )

        homeNativeDialogAd = AdManager.instance.produceNativeAd(context, homeDialogNativeAdModel)
    }

    fun loadHomeFunctionsNativeAd(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowHomeFunctionsAd().not()) {
            return
        }

        if (homeFunctionsNativeAdProducer != null) {
            homeFunctionsNativeAdProducer!!.loadAdOnly(context, homeFunctionsNativeAdProducer!!.isAdAutoLoaded)
            return
        }

        val homeFunctionsAdModel = NativeAdModel(
            adUnitId = HKTAdConfig.nativeHomeFunctionsUnitId,
            adName = "home_functions",
            resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
            adType = "medium",
            isShowAd = true
        )

        homeFunctionsNativeAdProducer = AdManager.instance.produceNativeAd(context, homeFunctionsAdModel)
    }

    fun loadWallPreviewFullScreenNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowWalletPreviewNativeFullAd().not()) {
            return
        }

        if (previewWalletNativeProducer != null) {
            previewWalletNativeProducer!!.loadAdOnly(context)
            return
        }

        val fullScreenNativeModel = NativeAdModel( adUnitId = HKTAdConfig.nativePreviewWallPaperUnitId,
            adName = "wallet_preview",
            resShimmerId = R.layout.layout_native_ad_large_button_top_shimmer,
            adType = "medium",
            isShowAd = true,
        )
        previewWalletNativeProducer = AdManager.instance.produceNativeAd(context, fullScreenNativeModel)
    }

    fun loadAIPreviewFullScreenNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowAIPreviewNativeFullAd().not()) {
            return
        }

        if (previewAINativeProducer != null) {
            previewAINativeProducer!!.loadAdOnly(context)
            return
        }

        val fullScreenNativeModel = NativeAdModel( adUnitId = HKTAdConfig.nativePreviewAIUnitId,
            adName = "ai_preview",
            resShimmerId = R.layout.layout_native_ad_large_button_top_shimmer,
            adType = "medium",
            isShowAd = true,
        )
        previewAINativeProducer = AdManager.instance.produceNativeAd(context, fullScreenNativeModel)
    }

    fun loadVideoPreviewFullScreenNativeAds(context: Context) {
        if (isAllowShowAd.not() || HKTAdRemoteConfig.shouldShowAIPreviewNativeFullAd().not()) {
            return
        }

        if (previewVideoNativeProducer != null) {
            previewVideoNativeProducer!!.loadAdOnly(context)
            return
        }

        val fullScreenNativeModel = NativeAdModel( adUnitId = HKTAdConfig.videoPreviewAIUnitId,
            adName = "video_preview",
            resShimmerId = R.layout.layout_native_ad_large_button_top_shimmer,
            adType = "medium",
            isShowAd = true,
        )
        previewVideoNativeProducer = AdManager.instance.produceNativeAd(context, fullScreenNativeModel)
    }
}