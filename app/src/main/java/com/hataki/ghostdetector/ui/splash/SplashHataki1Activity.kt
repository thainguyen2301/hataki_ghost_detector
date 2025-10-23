package com.hataki.ghostdetector.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivitySplashHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.language.LanguageHataki1Activity
import com.hataki.ghostdetector.ui.language.LanguageHataki1Activity.Companion.APP_LANG
import com.hataki.ghostdetector.ui.onboard.OnboardingHataki1Activity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashHataki1Activity : BaseActivity<SplashViewModel, ActivitySplashHataki1Binding>() {

    private fun checkNextScreenHataki1() {
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(APP_LANG, null)
        if (lang == null) {
            LanguageHataki1Activity.open(this@SplashHataki1Activity)
        } else {
            OnboardingHataki1Activity.open(this@SplashHataki1Activity)
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
                                    checkNextScreenHataki1()
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
    }

    override fun getLayoutResource(): Int = R.layout.activity_splash_hataki_1

    override fun onResumeImpl() {
    }

}
