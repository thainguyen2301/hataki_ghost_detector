package com.ghostfinder.ghostdetector.radar.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import androidx.preference.PreferenceManager
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.databinding.ActivitySplashBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.language.LanguageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override fun getLayoutResource(): Int = R.layout.activity_splash

    override fun viewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun onCreateImpl() {
        binding.logoImage.alpha = 0f
        binding.logoImage.animate()
            .alpha(1f)
            .setDuration(1000)
            .setListener(null)
        simulateLoading()
    }

    override fun onResumeImpl() {
    }

    private fun checkNextScreen() {
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(LanguageActivity.Companion.APP_LANG, null)
//        if (lang == null) {
            LanguageActivity.Companion.open(this@SplashActivity)
//        } else {
//            OnboardingActivity.open(this@SplashActivity)
//        }
        finish()
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
                                    checkNextScreen()
                                }
                            })
                    }
                }
            }
            handler.post(loadingTask)
        }
    }
}
