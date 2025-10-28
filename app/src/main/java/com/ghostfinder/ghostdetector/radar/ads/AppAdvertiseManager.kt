package com.ghostfinder.ghostdetector.radar.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.ghostfinder.ghostdetector.radar.AdRemoteConfig
import com.ghostfinder.ghostdetector.radar.BuildConfig
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.logger.FileLogger
import com.ghostfinder.ghostdetector.radar.ads.logger.FirebaseEventTracker
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenAdListener
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenAdModel
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenBaseAdProducer
import com.mobile.hataki_ad_lib.ad_banner.BannerAdModel
import com.mobile.hataki_ad_lib.ad_banner.BannerBaseAdProducer
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdModel
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdProducer
import com.mobile.hataki_ad_lib.ad_manager.AdManager
import com.mobile.hataki_ad_lib.ad_native.NativeAdModel
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import com.mobile.hataki_ad_lib.adjust_tracking.AdjustEnvironment
import com.mobile.hataki_ad_lib.adjust_tracking.AdjustTracking

object AppAdvertiseManager {

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

    var interIntroAdProducer: InterstitialAdProducer? = null
        private set

    var permissionNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var welcomeBackNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var homeInterstitialAdProducer: InterstitialAdProducer? = null

    var interOpenResumeAd: InterstitialAdProducer? = null
        private set

    var appOpenResumeProducer: AppOpenBaseAdProducer? = null
        private set

    val fileLogger = FileLogger()
    private val firebaseTracker = FirebaseEventTracker()

    var currentNativeFullScreen: NativeBaseAdProducer? = null

    var successNativeAdProducer: NativeBaseAdProducer? = null
        private set

    var homeNativeDialogAd: NativeBaseAdProducer? = null

    private var isAllowShowAd = false
    fun initialize(context: Context) {
        isAllowShowAd = AdRemoteConfig.shouldShowAllAds()
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
            fileLogger.log(context,"AppAdvertiseManager", "Event loadMainLanguageNative not call because isAllowShowAd = false")
            return
        }

        if (mainLanguageNativeProducer != null) {
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadMainLanguageNative called")

        if (AdRemoteConfig.shouldShowMainLanguageNativeAd()) {
            val mainNativeModel = NativeAdModel(
                adUnitId = AdConfig.getNativeLanguageMainAdUnitId(),
                adName = "language_main",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true,
                reloadTimeInterval = AdRemoteConfig.adConfiguration?.nativeReloadTime
            )
            mainLanguageNativeProducer =
                AdManager.instance.produceNativeAd(context, mainNativeModel)
        } else {
            fileLogger.log(context,"AppAdvertiseManager", "Event shouldShowMainLanguageNativeAd not call because shouldShowMainLanguageNativeAd = false")
        }
    }

    fun loadSelectLanguageNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadSelectLanguageNativeAd not call because isAllowShowAd = false")
            return
        }

        if (selectedLanguageNativeProducer != null) {
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadSelectLanguageNativeAd called")

        if (AdRemoteConfig.shouldShowSelectedLanguageNativeAd()) {
            val selectedNativeModel = NativeAdModel(
                adUnitId = AdConfig.getNativeLanguageSelectedAdUnitId(),
                adName = "language_selected",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true
            )
            selectedLanguageNativeProducer =
                AdManager.instance.produceNativeAd(context, selectedNativeModel)
        } else {
            fileLogger.log(context,"AppAdvertiseManager", "Event shouldShowSelectedLanguageNativeAd not call because shouldShowSelectedLanguageNativeAd = false")
        }

    }
    fun loadDropLanguageNativeAds(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadDropLanguageNativeAds not call because isAllowShowAd = false")
            return
        }

        if (selectedChildLanguageNativeProducer != null) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadDropLanguageNativeAds not call because there is another loading process")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadDropLanguageNativeAds called")

        if (AdRemoteConfig.shouldShowChildLanguageNativeAd()) {
            val selectedChildNativeModel = NativeAdModel( adUnitId = AdConfig.getNativeLanguageDropAdUnitId(),
                adName = "language_drop",
                resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
                adType = "medium",
                isShowAd = true)
            selectedChildLanguageNativeProducer = AdManager.instance.produceNativeAd( context, selectedChildNativeModel)
        } else {
            fileLogger.log(context,"AppAdvertiseManager", "Event not call drop native ads shouldShowChildLanguageNativeAd = false")
        }
    }

    fun loadSplashNativeAds(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativeSplashAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event not loadSplashNativeAds because isAllowShowAd = $isAllowShowAd and shouldShowNativeSplashAd = ${AdRemoteConfig.shouldShowNativeSplashAd()}")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event not call load splash native")

        val splashNativeModel = NativeAdModel(adUnitId = AdConfig.nativeSplashAdUnitId,
            adName = "splash",
            resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
            adType = "medium",
            isShowAd = true
        )
        splashNativeProducer = AdManager.instance.produceNativeAd(context, splashNativeModel)
    }

    fun loadFirstIntroNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadFirstIntroNativeAd not call because isAllowShowAd = $isAllowShowAd ")
            return
        }

        if (firstIntroNativeAdProducer != null) {
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadFirstIntroNativeAd called")
        if (AdRemoteConfig.shouldShowNativeIntro1Ad()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadFirstIntroNativeAd called")
            val firstIntroNativeModel = NativeAdModel(
                adUnitId = AdConfig.getNativeIntro1AdUnitId(),
                adName = "intro_1",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true,
                reloadTimeInterval = AdRemoteConfig.adConfiguration?.nativeReloadTime
            )
            firstIntroNativeAdProducer =
                AdManager.instance.produceNativeAd(context, firstIntroNativeModel)
        } else {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadFirstIntroNativeAd not call because loadFirstIntroNativeAd = ${AdRemoteConfig.shouldShowNativeIntro1Ad()}")
        }
    }

    fun loadLastIntroNativeAd(context: Context) {
        if (isAllowShowAd.not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadLastIntroNativeAd not call because isAllowShowAd = $isAllowShowAd ")
            return
        }

        if (lastIntroNativeAdProducer != null) {
            return
        }

        if (AdRemoteConfig.shouldShowNativeIntro2Ad()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadLastIntroNativeAd called")
            val lastIntroNativeModel = NativeAdModel(
                adUnitId = AdConfig.getNativeIntro4AdUnitId(),
                adName = "intro_2",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )
            lastIntroNativeAdProducer =
                AdManager.instance.produceNativeAd(context, lastIntroNativeModel)
        } else {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadLastIntroNativeAd not call because loadLastIntroNativeAd = ${AdRemoteConfig.shouldShowNativeIntro2Ad()}")
        }
    }

    fun produceInterstitialSplashAd(context: Context): InterstitialAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowInterstitialSplashAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadInterstitialSplashAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialSplashAd = ${AdRemoteConfig.shouldShowInterstitialSplashAd()}")
            return null
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadInterstitialSplashAd called")

        val interSplashModel = InterstitialAdModel(
            adId = AdConfig.interstitialSplashAdUnitId,
            adName = "splash_inter",
            adMode = "load_and_show",
//            nativeFullScreenAdModel = fullNativeAfterSplashInterModel
        )
        return AdManager.instance.produceInterstitialAd(interSplashModel)
    }

    fun loadInterstitialIntroAd(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowInterstitialIntroAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadInterstitialIntroAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialIntroAd = ${AdRemoteConfig.shouldShowInterstitialIntroAd()}")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadInterstitialIntroAd called")

        var fullNativeAfterIntroInterModel: NativeAdModel? = null
        if (AdRemoteConfig.shouldShowNativeFullScreenAfterIntro()) {
            fullNativeAfterIntroInterModel = NativeAdModel(
                adUnitId = AdConfig.nativeFullAfterIntroInter,
                adName = "intro_full",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )

            fileLogger.log(context, tag = "AppAdvertiseManager","Event shouldShowNativeFullScreenAfterIntro = ${AdRemoteConfig.shouldShowNativeFullScreenAfterIntro()}")
        }

        val interIntroModel = InterstitialAdModel(
            adId = AdConfig.interstitialIntroAdUnitId,
            adName = "intro_inter",
            adMode = "load_only",
            nativeFullScreenAdModel = fullNativeAfterIntroInterModel
        )
        interIntroAdProducer = AdManager.instance.produceInterstitialAd(interIntroModel)
        interIntroAdProducer?.load(context)
    }

    fun loadPermissionNativeAds(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativePermissionAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadPermissionNativeAds not call because isAllowShowAd = $isAllowShowAd and shouldShowNativePermissionAd = ${AdRemoteConfig.shouldShowNativePermissionAd()}")
            return
        }

        permissionNativeAdProducer?.let {
            it.loadAdOnly(context)
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadPermissionNativeAds called")

        val permissionNativeModel = NativeAdModel( adUnitId = AdConfig.nativePermissionAdUnitId,
            adName = "permission",
            resShimmerId = R.layout.layout_native_ad_medium_button_top,
            adType = "medium",
            isShowAd = true,
            reloadTimeInterval = AdRemoteConfig.adConfiguration?.nativeReloadTime)
        permissionNativeAdProducer = AdManager.instance.produceNativeAd(context, permissionNativeModel)
    }

    fun loadWelcomeBackNativeAds(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativeWelcomeBackAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadWelcomeBackNativeAds not call because isAllowShowAd = $isAllowShowAd and shouldShowNativeWelcomeBackAd = ${AdRemoteConfig.shouldShowNativeWelcomeBackAd()}")
            return
        }

        welcomeBackNativeAdProducer?.let {
            it.loadAdOnly(context)
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadWelcomeBackNativeAds called")

        val welcomeBackNativeModel = NativeAdModel( adUnitId = AdConfig.nativeWelcomeBackAdUnitId,
            adName = "welcome_back",
            resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
            adType = "medium",
            isShowAd = true)
        welcomeBackNativeAdProducer = AdManager.instance.produceNativeAd(context, welcomeBackNativeModel)
    }

    fun loadInterOpenResumeAd(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowAppOpenAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadInterOpenResumeAd not call because isAllowShowAd = $isAllowShowAd and loadInterOpenResumeAd = ${AdRemoteConfig.shouldShowAppOpenAd()}")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadInterOpenResumeAd called")
        if (interOpenResumeAd != null) {
            interOpenResumeAd!!.load(context)
            return
        }

        val interResumeModel = InterstitialAdModel(
            adId = AdConfig.interOpenAdUnitId,
            adName = "inter_resume", adMode = "load_only"
        )
        interOpenResumeAd = AdManager.instance.produceInterstitialAd(interResumeModel)

        interOpenResumeAd?.load(context)
    }

    fun loadHomeInterstitialAd(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowInterstitialHomeAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadHomeInterstitialAd not call because isAllowShowAd = $isAllowShowAd and shouldShowInterstitialHomeAd = ${AdRemoteConfig.shouldShowInterstitialHomeAd()}")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadHomeInterstitialAd called")
        homeInterstitialAdProducer?.let {
            it.load(context)
            return
        }


        var nativeFullScreenAfterHomeInterModel: NativeAdModel? = null
        if (AdRemoteConfig.shouldShowNativeFullScreenAfterHome()) {
            nativeFullScreenAfterHomeInterModel = NativeAdModel(
                adUnitId = AdConfig.nativeFullAfterHomeInter,
                adName = "home_end_card",
                resShimmerId = R.layout.layout_native_ad_small_button_bottom_shimmer,
                adType = "medium",
                isShowAd = true
            )

            fileLogger.log(context, tag = "AppAdvertiseManager","Event shouldShowNativeFullScreenAfterHome = ${AdRemoteConfig.shouldShowNativeFullScreenAfterHome()}")
        }

        val interHomeModel = InterstitialAdModel(
            adId = AdConfig.interstitialHomeAdUnitId,
            adMode = "load_only",
            adName = "home",
            nativeFullScreenAdModel = nativeFullScreenAfterHomeInterModel,
            shouldPreloadAfterShown = true
        )
        homeInterstitialAdProducer = AdManager.instance.produceInterstitialAd(interHomeModel)
        homeInterstitialAdProducer?.load(context)
    }

    fun showAdaptiveBanner(context: Context, container: ViewGroup): BannerBaseAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowBannerAd().not()) {
            return null
        }

        val bannerAdModel = BannerAdModel(
            adUnitId = AdConfig.bannerAdUnitId,
            resShimmerId = R.layout.layout_banner_ad_shimmer,
            adType = "adaptive",
            isShowAd = true
        )

        return AdManager.instance.produceBannerAd( context, container = container, bannerAdModel)
    }

    fun showCollapsibleHomeBanner(context: Context, container: ViewGroup): BannerBaseAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowBannerAd().not()) {
            return null
        }

        val bannerAdModel = BannerAdModel(
            adUnitId = AdConfig.homeBannerAdUnitId,
            resShimmerId = R.layout.layout_banner_ad_shimmer,
            adType = "collapsible_bottom",
            isShowAd = true
        )

        return AdManager.instance.produceBannerAd( context, container = container, bannerAdModel)
    }

    fun loadIntroFullScreenNativeAds(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativeFullAd().not()) {
            return
        }

        if (fullScreenIntroNativeAdProducer != null) {
            fullScreenIntroNativeAdProducer!!.loadAdOnly(context)
            return
        }

        val fullScreenNativeModel = NativeAdModel( adUnitId = AdConfig.nativeFullAdUnitId,
            adName = "intro_full",
            resShimmerId = R.layout.layout_native_ad_large_button_top_shimmer,
            adType = "medium",
            isShowAd = true,
        )
        fullScreenIntroNativeAdProducer = AdManager.instance.produceNativeAd(context, fullScreenNativeModel)
    }

    fun loadHomeNativeDialogAd(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativeHomeAd().not()) {
            return
        }

        if (homeNativeDialogAd != null) {
            homeNativeDialogAd!!.loadAdOnly(context, homeNativeDialogAd!!.isAdAutoLoaded)
            return
        }

        val homeDialogNativeAdModel = NativeAdModel(
            adUnitId = AdConfig.nativeHomeDialogUnitId,
            adName = "home_dialog",
            resShimmerId = R.layout.layout_native_ad_medium_button_top_shimmer,
            adType = "medium",
            isShowAd = true
        )

        homeNativeDialogAd = AdManager.instance.produceNativeAd(context, homeDialogNativeAdModel)
    }

    fun loadAndShowResumeAds(onActivity: Activity) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowAppOpenAd().not()) {
            fileLogger.log(onActivity,"AppAdvertiseManager", "Event loadAndShowResumeAds not call because isAllowShowAd = ${isAllowShowAd} and shouldShowAppOpenAd = ${AdRemoteConfig.shouldShowAppOpenAd()}")
            return
        }

        fileLogger.log(onActivity,"AppAdvertiseManager", "Event loadAndShowResumeAds called")
//        if (appOpenResumeProducer != null) {
//            appOpenResumeProducer!!.show(onActivity)
//            return
//        }

        val appOpenResumeModel = AppOpenAdModel(
            adUnitId = AdConfig.appOpenAdUnitId,
            isShowAd = true,
        )
        appOpenResumeProducer = AdManager.instance.produceAppOpenAd(onActivity, appOpenResumeModel)
        appOpenResumeProducer!!.setListener(listener = object : AppOpenAdListener {
            override fun onAdLoaded() {
                appOpenResumeProducer!!.show(onActivity)
            }
        })

        appOpenResumeProducer!!.load(onActivity)
    }

    fun loadSplashOpenResumeAd(context: Context): AppOpenBaseAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowSplashAppOpenAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadSplashOpenResumeAd not call because isAllowShowAd = ${isAllowShowAd} and loadSplashOpenResumeAd = ${AdRemoteConfig.shouldShowSplashAppOpenAd()}")
            return null
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadAndShowResumeAds called")

        val appOpenResumeModel = AppOpenAdModel(
            adUnitId = AdConfig.splashOpenResumeUnitId,
            adName = "splash",
            isShowAd = true,
        )
        return AdManager.instance.produceAppOpenAd(context, appOpenResumeModel)
    }

    fun produceSuccessInterAdProducer(context: Context): InterstitialAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowInterstitialSuccessAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event produceSuccessInterAdProducer not call because isAllowShowAd = ${isAllowShowAd} and produceSuccessInterAdProducer = ${AdRemoteConfig.shouldShowInterstitialSuccessAd()}")
            return null
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event shouldShowInterstitialSuccessAd called")

        val interHomeModel = InterstitialAdModel(
            adId = AdConfig.interstitialHomeAdUnitId,
            adMode = "load_and_show",
            adName = "success",
            shouldPreloadAfterShown = true
        )
        return AdManager.instance.produceInterstitialAd(interHomeModel)
    }

    fun loadSuccessNativeAds(context: Context) {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowNativeSuccessAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event loadSuccessNativeAds not call because isAllowShowAd = ${isAllowShowAd} and shouldShowNativeSuccessAd = ${AdRemoteConfig.shouldShowNativeSuccessAd()}")
            return
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event loadSuccessNativeAds called")

        if (successNativeAdProducer != null) {
            successNativeAdProducer!!.loadAdOnly(context)
            return
        }

        val successNativeModel = NativeAdModel( adUnitId = AdConfig.nativeSuccessAdUnitId,
            adName = "success",
            resShimmerId = R.layout.layout_native_ad_medium_button_bottom,
            adType = "medium",
            isShowAd = true,
            reloadTimeInterval = 10000L
        )
        successNativeAdProducer = AdManager.instance.produceNativeAd(context, successNativeModel)
    }

    fun produceInterBackAdProducer(context: Context): InterstitialAdProducer? {
        if (isAllowShowAd.not() || AdRemoteConfig.shouldShowInterBackAd().not()) {
            fileLogger.log(context,"AppAdvertiseManager", "Event produceInterBackAdProducer not call because isAllowShowAd = ${isAllowShowAd} and produceInterBackAdProducer = ${AdRemoteConfig.shouldShowInterBackAd()}")
            return null
        }

        fileLogger.log(context,"AppAdvertiseManager", "Event produceInterBackAdProducer called")

        val interBackModel = InterstitialAdModel(
            adId = AdConfig.interstitialBackAdUnitId,
            adMode = "load_and_show",
            adName = "success",
            shouldPreloadAfterShown = true
        )
        return AdManager.instance.produceInterstitialAd(interBackModel)
    }
}