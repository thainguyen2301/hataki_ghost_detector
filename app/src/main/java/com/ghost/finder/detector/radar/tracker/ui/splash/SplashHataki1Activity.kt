package com.ghost.finder.detector.radar.tracker.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAdConfig
import com.ghost.finder.detector.radar.tracker.ads.HKTAdRemoteConfig
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.ads.base.BaseRequestFullNativeActivity
import com.ghost.finder.detector.radar.tracker.databinding.ActivitySplashHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.language.LanguageHataki1Activity
import com.ghost.finder.detector.radar.tracker.ui.onboard.OnboardingHataki1Activity
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_manager.AdManager
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@AndroidEntryPoint
class SplashHataki1Activity : BaseRequestFullNativeActivity<SplashViewModel, ActivitySplashHataki1Binding>() {

    private var isHandled: Boolean = false

    private fun checkNextScreenHataki1() {
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(LanguageHataki1Activity.Companion.APP_LANG, null)
        if (lang == null) {
            LanguageHataki1Activity.Companion.open(this@SplashHataki1Activity)
        } else {
            OnboardingHataki1Activity.Companion.open(this@SplashHataki1Activity)
        }
        finish()
    }

    override fun viewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    private fun simulateLoadingHataki1() {
        val messages = this.resources.getStringArray(R.array.loading_messages).toList()
        val handler = Handler(Looper.getMainLooper())
        var currentStep = 0
        val totalSteps = messages.size

        binding.progressContainer.post {
            val containerWidth = binding.progressContainer.width
            val thumbWidth = binding.progressThumb.width
            val thumbAnimator = ObjectAnimator.ofFloat(
                binding.progressThumb,
                "translationX",
                -thumbWidth.toFloat(),
                containerWidth.toFloat()
            )
            thumbAnimator.duration = 2000
            thumbAnimator.repeatCount = ValueAnimator.INFINITE
            thumbAnimator.repeatMode = ValueAnimator.RESTART
            thumbAnimator.start()

            val loadingTask = object : Runnable {
                override fun run() {
                    if (currentStep < totalSteps) {
                        binding.statusText.text = messages[currentStep]
                        currentStep++
                        handler.postDelayed(this, 1000)
                    } else {
                        thumbAnimator.cancel()
                        binding.logoImage.animate()
                            .alpha(0f)
                            .setDuration(800)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
//                                    checkNextScreenHataki1()
                                }
                            })
                    }
                }
            }
            handler.post(loadingTask)
        }
    }

    override fun onCreateImpl() {
        binding.logoImage.alpha = 0f
        binding.logoImage.animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
        simulateLoadingHataki1()

        lifecycleScope.launch(Dispatchers.IO) {
            performRemoteConfigInitialization()
        }

    }

    override fun getLayoutResource(): Int = R.layout.activity_splash_hataki_1

    override fun onResumeImpl() {
    }


    suspend fun performRemoteConfigInitialization() {
        // Launch the remote config initialization in a separate coroutine scope
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = HKTAdRemoteConfig.initializeRemoteConfig(HKTAppAdvertiseManager.fileLogger, application)
                if (success) {
                    Log.d(TAG, "Remote config initialization completed successfully")
                } else {
                    Log.e(TAG, "Remote config initialization failed")
                }

                val getIsFirstOpenApp = PreferenceManager.getDefaultSharedPreferences(this@SplashHataki1Activity)
                    .getBoolean(LanguageHataki1Activity.Companion.IS_FIRST_OPEN_APP, false)

                AdManager.instance.initAdsWithConsent(this@SplashHataki1Activity, testDeviceIds = listOf("BCDBD2B7E45CEFEDE711AC3BACFAF33C"), onReady = {
                    HKTAdConfig.load(getIsFirstOpenApp)
                    HKTAppAdvertiseManager.initialize(application)
                    HKTAppAdvertiseManager.loadSplashNativeAds(this@SplashHataki1Activity)
                    configAds()
                    HKTAppAdvertiseManager.loadInterstitialSplashAd(this@SplashHataki1Activity)
                    HKTAppAdvertiseManager.loadMainLanguageNative(this@SplashHataki1Activity)
                }, onError = {
                    handleNextAction()
                })
            } catch (e: Exception) {
                Log.e(TAG, "Error during remote config initialization: ${e.message}")
            }
        }
//        job.join()

        // Wait for up to 10 seconds for the job to complete
        val result = withTimeoutOrNull(15_000) {
            job.join() // Wait for the job to finish
            true // Return true if the job completes within timeout
        }

        if (result == null) {
            Log.w(TAG, "Timeout occurred, but remote config initialization continues in the background")
            handleNextAction()
        } else {
            Log.d(TAG, "Remote config initialization completed within timeout")
        }
    }

    private fun handleNextAction() {
        if (!isHandled) {
            isHandled = true
            nextActivity()
//            PreferenceManager.getDefaultSharedPreferences(this@SplashHataki1Activity).apply {
//                pu
//            }(this, LanguageHataki1Activity.IS_FIRST_OPEN_APP, false)

        }
    }

    override fun onNextFromNativeFullScreenAd() {
        super.onNextFromNativeFullScreenAd()
        HKTAppAdvertiseManager.loadSelectLanguageNativeAd(this)
        showLanguageScreen()
    }

    private fun configAds() {
        HKTAppAdvertiseManager.splashNativeProducer?.let { nativeAdProducer ->
            nativeAdProducer.setListener(object : NativeAdListener {
                override fun onAdFailedToLoad() {
                    super.onAdFailedToLoad()
                    handleNextAction()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    handleNextAction()
                }
            })
            nativeAdProducer.show(
                this@SplashHataki1Activity,
                R.layout.layout_native_ad_medium_button_top,
                binding.nativeAdViewContainer
            )
        } ?: run {
            handleNextAction()
        }
    }

    fun nextActivity() {
        showInterAdIfNeeded {
            HKTAppAdvertiseManager.loadSelectLanguageNativeAd(this@SplashHataki1Activity)
            lifecycleScope.launch {
                delay(500L)
                showLanguageScreen()
            }
        }
    }

    private fun showLanguageScreen() {
//        startActivity(Intent(this, ::class.java))
//        finishAffinity()
    }

    private fun showInterAdIfNeeded(callback: () -> Unit) {
        val interSplashAdProducer = HKTAppAdvertiseManager.interSplashAdProducer ?: run {
            callback()
            return
        }
        interSplashAdProducer.setListener(object : InterstitialAdListener {
            override fun onNextAction() {
                callback()
            }

            override fun onAdFailedToShow() {
                super.onAdFailedToShow()
                callback()
            }

            override fun onAdLoadFailed(isAutoPreload: Boolean) {
                super.onAdLoadFailed(isAutoPreload)
                callback()
            }

            override fun onAdLoaded(isAutoPreload: Boolean) {
                super.onAdLoaded(isAutoPreload)
                interSplashAdProducer.show(this@SplashHataki1Activity, lifecycle = lifecycle)
            }

            override fun showNativeFullAd(ad: NativeBaseAdProducer) {
                super.showNativeFullAd(ad)
                showNativeFullScreenAd(ad)
            }
        })

        interSplashAdProducer.show(this, lifecycle = lifecycle)
    }

}
