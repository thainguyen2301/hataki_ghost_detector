package com.ghost.finder.detector.radar.tracker.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.databinding.ActivitySplashHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.language.LanguageHataki1Activity
import com.ghost.finder.detector.radar.tracker.ui.onboard.OnboardingHataki1Activity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashHataki1Activity : BaseActivity<SplashViewModel, ActivitySplashHataki1Binding>() {

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
