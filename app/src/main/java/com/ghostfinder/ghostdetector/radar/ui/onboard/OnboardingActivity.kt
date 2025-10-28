package com.ghostfinder.ghostdetector.radar.ui.onboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ghostfinder.ghostdetector.radar.AdRemoteConfig
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.ads.native_full.HatakiNativeFullActivity
import com.ghostfinder.ghostdetector.radar.data.model.OnboardingItem
import com.ghostfinder.ghostdetector.radar.databinding.ActivityOnboardingBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.common.PermissionHelper
import com.ghostfinder.ghostdetector.radar.ui.common.dp
import com.ghostfinder.ghostdetector.radar.ui.permission.RequestPermissionActivity
import com.ghostfinder.ghostdetector.radar.ui.start.StartActivity
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, OnboardingActivity::class.java))
        }
    }

    private val secondActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            isSecondActivityCompleted = true
            // Move to slide 3 (index 2)
            binding.viewPager.setCurrentItem(2, true)
        }
    }
    private lateinit var adapter: OnboardingAdapter
    private var isSecondActivityCompleted = false
    override fun getLayoutResource(): Int = R.layout.activity_onboarding

    override fun viewModelClass(): Class<OnboardingViewModel> = OnboardingViewModel::class.java

    override fun onCreateImpl() {
        initViewPager()
        lifecycleScope.launch {
            setupAds()
        }
    }

    override fun onResumeImpl() {
    }



    private fun onCompleteOnboarding() {

        val showNextActivity = {
            RequestPermissionActivity.Companion.open(this)
            finishAffinity()
        }

        val interIntroAdProducer = AppAdvertiseManager.interIntroAdProducer ?: run {
            showNextActivity()
            return
        }

        interIntroAdProducer.setListener(object : InterstitialAdListener {
            override fun onNextAction() {
                super.onNextAction()
                showNextActivity()
            }

            override fun onAdLoadFailed(isAutoPreload: Boolean) {
                super.onAdLoadFailed(isAutoPreload)
                if (isAutoPreload.not()) {
                    showNextActivity()
                }
            }

            override fun onAdFailedToShow() {
                super.onAdFailedToShow()
                showNextActivity()
            }

            override fun requireActivityForLoadAndShowAd(): Activity? {
                return this@OnboardingActivity
            }
        })

        interIntroAdProducer.show(this, lifecycle)
    }

    private fun setupIndicators(count: Int) {
        val container = binding.indicatorContainer
        container.removeAllViews()

        repeat(count) {
            val dot = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    8.dp,
                    8.dp
                ).apply {
                    marginStart = 4.dp
                    marginEnd = 4.dp
                }
                background = ContextCompat.getDrawable(context, R.drawable.bg_tab_selector)
                isSelected = false
            }
            container.addView(dot)
        }
        if (container.isNotEmpty()) {
            container.getChildAt(0).apply {
                layoutParams = LinearLayout.LayoutParams(
                    20.dp,
                    8.dp
                ).apply {
                    marginStart = 4.dp
                    marginEnd = 4.dp
                }
                isSelected = true
            }
        }
    }

    private fun setupAds() {
        AppAdvertiseManager.loadFirstIntroNativeAd(this)
        AppAdvertiseManager.firstIntroNativeAdProducer?.setListener(object : NativeAdListener {
            override fun onAdLoaded(isAutoLoad: Boolean) {
                super.onAdLoaded(isAutoLoad)
                if (binding.viewPager.currentItem != 0) {
                    AppAdvertiseManager.firstIntroNativeAdProducer?.show(
                        this@OnboardingActivity,
                        R.layout.layout_native_ad_small_button_bottom,
                        binding.frAdBottom
                    )
                }
            }
        })
        AppAdvertiseManager.loadIntroFullScreenNativeAds(this)
        AppAdvertiseManager.loadPermissionNativeAds(this)
        AppAdvertiseManager.loadInterstitialIntroAd(this)
    }

    private fun initViewPager() {
        val items = listOf(
            OnboardingItem(
                title = resources.getString(R.string.onboarding_title_1),
                image = R.drawable.onboarding1,
            ),
            OnboardingItem(
                title = resources.getString(R.string.onboarding_title_2),
                image = R.drawable.onboarding2,
            ),
            OnboardingItem(
                title = resources.getString(R.string.onboarding_title_3),
                image = R.drawable.onboarding3,
            ),
            OnboardingItem(
                title = resources.getString(R.string.onboarding_title_4),
                image = R.drawable.onboarding4,
            )
        )
        adapter = OnboardingAdapter(items)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val container = binding.indicatorContainer
                for (i in 0 until container.childCount) {
                    if (i == position) {
                        container.getChildAt(i).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                20.dp,
                                8.dp
                            ).apply {
                                marginStart = 4.dp
                                marginEnd = 4.dp
                            }
                        }
                    } else {
                        container.getChildAt(i).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                8.dp,
                                8.dp
                            ).apply {
                                marginStart = 4.dp
                                marginEnd = 4.dp
                            }
                        }
                    }
                    container.getChildAt(i).isSelected = (i == position)
                }
            }
        })
        setupIndicators(items.size)
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < items.size - 1) {
                val currentPosition = binding.viewPager.currentItem
                if (currentPosition == 1 && !isSecondActivityCompleted && isValidToShowFullNativeAd()) {
                    // Launch SecondActivity from slide 2
                    AppAdvertiseManager.currentNativeFullScreen = AppAdvertiseManager.fullScreenIntroNativeAdProducer
                    val intent = Intent(this@OnboardingActivity, HatakiNativeFullActivity::class.java)
                    secondActivityResultLauncher.launch(intent)
                } else {
                    // Move to next slide
                    binding.viewPager.setCurrentItem(currentPosition + 1, true)
                }
            } else {
                onCompleteOnboarding()
            }
        }

        binding.largeNextButton.setOnClickListener {
            binding.viewPager.currentItem = 1
        }


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == items.size - 1) resources.getString(R.string.get_started) else resources.getString(
                        R.string.next
                    )

                if (position == 0) {
                    // does not show anything
                    binding.frAdBottom.isVisible = false
                    binding.largeNextButton.isVisible = true
                    binding.btnNext.isVisible = false
                } else {
                    binding.frAdBottom.isVisible = true
                    binding.largeNextButton.isVisible = false
                    binding.btnNext.isVisible = true
                    AppAdvertiseManager.firstIntroNativeAdProducer?.show(
                        this@OnboardingActivity,
                        R.layout.layout_native_ad_small_button_bottom,
                        binding.frAdBottom
                    )
                }
            }
        })
        binding.largeNextButton.isVisible = true
        binding.btnNext.isVisible = false
    }

    private fun isValidToShowFullNativeAd(): Boolean {
        return AdRemoteConfig.shouldShowNativeFullAd() && AdRemoteConfig.shouldShowAllAds() && AppAdvertiseManager.fullScreenIntroNativeAdProducer?.isValidToShow == true
    }
}
