package com.ghostfinder.ghostdetector.radar.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.ghostfinder.ghostdetector.radar.AdRemoteConfig
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.ads.base.BaseRequestFullNativeActivity
import com.ghostfinder.ghostdetector.radar.ads.model.SplashFullAdType
import com.ghostfinder.ghostdetector.radar.databinding.ActivitySplashBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.language.HatakiLanguageStartActivity
import com.ghostfinder.ghostdetector.radar.ui.language.LanguageActivity
import com.mobile.hataki_ad_lib.ad_app_open.AppOpenAdListener
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_manager.AdManager
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

@AndroidEntryPoint
class SplashActivity : BaseRequestFullNativeActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun getLayoutResource(): Int = R.layout.activity_splash
    private var isHandled: Boolean = false

    override fun viewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun onCreateImpl() {
        binding.logoImage.alpha = 0f
        binding.logoImage.animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
        simulateLoading()
        lifecycleScope.launch {
            performRemoteConfigInitialization()
        }
    }

    override fun onResumeImpl() {
    }

    private fun checkNextScreen() {
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(LanguageActivity.Companion.APP_LANG, null)
//        if (lang == null) {
            showLanguageScreen()
//        } else {
//            OnboardingActivity.open(this@SplashActivity)
//        }
        finish()
    }
    private fun showLanguageScreen() {
        startActivity(Intent(this, HatakiLanguageStartActivity::class.java))
        finishAffinity()
    }

    private fun simulateLoading() {
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
//                                    checkNextScreen()
                                }
                            })
                    }
                }
            }
            handler.post(loadingTask)
        }
    }

    private fun showOpenResumeAdIfNeed(callback: () -> Unit) {

        lifecycleScope.launch(Dispatchers.Main) {
//            delay(500L)
            val splashOpenResumeAdProducer = AppAdvertiseManager.loadSplashOpenResumeAd(this@SplashActivity)
            splashOpenResumeAdProducer?.let {
                it.setListener(listener = object : AppOpenAdListener {
                    override fun onAdFailedToLoad() {
                        super.onAdFailedToLoad()
                        callback()
                    }

                    override fun onAdFailedToShow() {
                        super.onAdFailedToShow()
                        callback()
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        splashOpenResumeAdProducer.show(this@SplashActivity)
                    }

                    override fun onAdDismissed() {
                        super.onAdDismissed()
                        callback()
                    }
                })

                it.load(this@SplashActivity)
            } ?: run {
                callback()
            }
        }
    }

    private fun showInterAdIfNeeded(callback: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
//            delay(500L)
            val interSplashAdProducer = AppAdvertiseManager.produceInterstitialSplashAd(this@SplashActivity)
            interSplashAdProducer?.let {
                it.setListener(object : InterstitialAdListener {
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
                        interSplashAdProducer.show(this@SplashActivity, lifecycle = lifecycle)
                    }

                    override fun showNativeFullAd(ad: NativeBaseAdProducer) {
                        super.showNativeFullAd(ad)
                        showNativeFullScreenAd(ad)
                    }
                })

                it.load(this@SplashActivity)
            } ?: run {
                callback()

            }
        }
    }

    suspend fun performRemoteConfigInitialization() {
        // Launch the remote config initialization in a separate coroutine scope
        val job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = AdRemoteConfig.initializeRemoteConfig(AppAdvertiseManager.fileLogger, application)
                if (success) {
                    Log.d(TAG, "Remote config initialization completed successfully")
                } else {
                    Log.e(TAG, "Remote config initialization failed")
                }

                AdManager.instance.initAdsWithConsent(this@SplashActivity, testDeviceIds = listOf("BCDBD2B7E45CEFEDE711AC3BACFAF33C"), onReady = {
//                    AdConfig.load(sharedPreferencesRepository.getIsFirstOpenApp())
                    AppAdvertiseManager.initialize(application)
                    AppAdvertiseManager.loadSplashNativeAds(this@SplashActivity)
                    configAds()
                    AppAdvertiseManager.loadMainLanguageNative(this@SplashActivity)
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
//            sharedPreferencesRepository.setIsFirstOpenApp(false)
        }
    }

    override fun onNextFromNativeFullScreenAd() {
        super.onNextFromNativeFullScreenAd()
        checkNextScreen()
    }

    private fun configAds() {
        AppAdvertiseManager.splashNativeProducer?.let { nativeAdProducer ->
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
                this@SplashActivity,
                R.layout.layout_native_ad_medium_button_top,
                binding.frAdBottom
            )
        } ?: run {
            handleNextAction()
        }
    }

    private fun nextActivity() {
        showAdIfNeeded {
            lifecycleScope.launch {
                delay(500L)
                checkNextScreen()
            }
        }
    }

    private fun showAdIfNeeded(callback: () -> Unit) {
        val type = AdRemoteConfig.getRandomAdType(this)
        when(type) {
            SplashFullAdType.INTERSTITIAL -> showInterAdIfNeeded { callback() }
            SplashFullAdType.OPEN_RESUME -> showOpenResumeAdIfNeed { callback() }
        }
    }
}
