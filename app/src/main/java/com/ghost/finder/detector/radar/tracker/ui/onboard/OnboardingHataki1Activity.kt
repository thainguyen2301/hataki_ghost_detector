package com.ghost.finder.detector.radar.tracker.ui.onboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAdRemoteConfig
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.ads.base.BaseRequestFullNativeActivity
import com.ghost.finder.detector.radar.tracker.ads.native_full.HatakiNativeFullActivity
import com.ghost.finder.detector.radar.tracker.data.model.OnboardingItem
import com.ghost.finder.detector.radar.tracker.databinding.ActivityOnboardingHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.common.dp
import com.ghost.finder.detector.radar.tracker.ui.permission.RequestPermissionHataki1Activity
import com.mobile.hataki_ad_lib.ad_interstitial.InterstitialAdListener
import com.mobile.hataki_ad_lib.ad_native.NativeBaseAdProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingHataki1Activity :
    BaseRequestFullNativeActivity<OnboardingViewModel, ActivityOnboardingHataki1Binding>() {

    override fun viewModelClass(): Class<OnboardingViewModel> = OnboardingViewModel::class.java

    override fun getLayoutResource(): Int = R.layout.activity_onboarding_hataki_1
    private var isSecondActivityCompleted = false

    private val secondActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            isSecondActivityCompleted = true
            // Move to slide 3 (index 2)
            binding.viewPager.setCurrentItem(2, true)
        }
    }

    private fun setupIndicatorsHataki1(count: Int) {
        val container = binding.indicatorContainer
        container.removeAllViews()

        repeat(count) {
            val dot = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    8.dp, 8.dp
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
                    20.dp, 8.dp
                ).apply {
                    marginStart = 4.dp
                    marginEnd = 4.dp
                }
                isSelected = true
            }
        }
    }

    private lateinit var adapterHataki1: OnboardingAdapter

    override fun onCreateImpl() {
        initViewPagerHataki1()
        lifecycleScope.launch(Dispatchers.Main) {
            // Ad
            HKTAppAdvertiseManager.loadLastIntroNativeAd(context = this@OnboardingHataki1Activity)
            HKTAppAdvertiseManager.loadIntroFullScreenNativeAds(this@OnboardingHataki1Activity)
            // Prepare for next permission screen
            HKTAppAdvertiseManager.loadPermissionNativeAds(this@OnboardingHataki1Activity)
            HKTAppAdvertiseManager.loadInterstitialIntroAd(this@OnboardingHataki1Activity)
            initHatakiNativeAds()
        }
    }

    override fun onResumeImpl() {
    }

    private fun isLastHatakiSlide(): Boolean {
        val currentPosition = binding.viewPager.currentItem
        return (currentPosition + 1 == 4)
    }

    private fun initViewPagerHataki1() {
        val items = listOf(
            OnboardingItem(
                R.drawable.img_intro_1,
                resources.getString(R.string.onboarding_title_1),
                resources.getString(R.string.onboarding_desc_1)
            ), OnboardingItem(
                R.drawable.img_intro_2,
                resources.getString(R.string.onboarding_title_2),
                resources.getString(R.string.onboarding_desc_2)
            ), OnboardingItem(
                R.drawable.img_intro_3,
                resources.getString(R.string.onboarding_title_3),
                resources.getString(R.string.onboarding_desc_3)
            ), OnboardingItem(
                R.drawable.img_intro_4,
                resources.getString(R.string.onboarding_title_4),
                resources.getString(R.string.onboarding_desc_4)
            )
        )
        adapterHataki1 = OnboardingAdapter(items)
        binding.viewPager.adapter = adapterHataki1
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val container = binding.indicatorContainer
                for (i in 0 until container.childCount) {
                    if (i == position) {
                        container.getChildAt(i).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                20.dp, 8.dp
                            ).apply {
                                marginStart = 4.dp
                                marginEnd = 4.dp
                            }
                        }
                    } else {
                        container.getChildAt(i).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                8.dp, 8.dp
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
        setupIndicatorsHataki1(items.size)
        binding.btnNext.setOnClickListener {
            if (!isLastHatakiSlide()) {
                val currentPosition = binding.viewPager.currentItem
                if (currentPosition == 1 && !isSecondActivityCompleted && isValidToShowFullScreenAd() ) {
                    // Launch SecondActivity from slide 2
                    HKTAppAdvertiseManager.currentNativeFullScreen = HKTAppAdvertiseManager.fullScreenIntroNativeAdProducer
                    val intent = Intent(this, HatakiNativeFullActivity::class.java)
                    secondActivityResultLauncher.launch(intent)
                } else {
                    // Move to next slide
                    binding.viewPager.setCurrentItem(currentPosition + 1, true)
                }
            } else {
                nextHatakiActivityProcess()
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == items.size - 1) resources.getString(R.string.get_started) else resources.getString(
                        R.string.next
                    )

                binding.viewPager.isUserInputEnabled = !(position == 1 && !isSecondActivityCompleted)
                // On slide 2, prepare to launch SecondActivity on swipe or button
                // Prevent swiping to slide 3

                if (binding.viewPager.currentItem == 3) {
                    HKTAppAdvertiseManager.lastIntroNativeAdProducer?.show(this@OnboardingHataki1Activity, R.layout.layout_native_ad_small_button_bottom, binding.frAdBottom)
                    binding.frAdBottom.visibility = View.VISIBLE
                } else if (binding.viewPager.currentItem != 0) {
                    binding.frAdBottom.visibility = View.GONE
                } else {
                    binding.frAdBottom.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun isValidToShowFullScreenAd(): Boolean {
        return HKTAdRemoteConfig.shouldShowNativeFullAd() && HKTAdRemoteConfig.shouldShowAllAds() && HKTAppAdvertiseManager.fullScreenIntroNativeAdProducer != null && HKTAppAdvertiseManager.fullScreenIntroNativeAdProducer?.isValidToShow == true
    }

    private fun initHatakiNativeAds() {
        Log.d(TAG, "Event request load first intro native ad")
        val firstNativeAdProducer = HKTAppAdvertiseManager.firstIntroNativeAdProducer ?: return
        firstNativeAdProducer.show(this, R.layout.layout_native_ad_small_button_bottom, binding.frAdBottom)
        binding.frAdBottom.visibility = View.VISIBLE
    }

    private fun nextHatakiActivityProcess() {
        val interIntroAdProducer = HKTAppAdvertiseManager.interIntroAdProducer ?: run {
            goToPermissionScreen()
            return
        }

        interIntroAdProducer.setListener(object : InterstitialAdListener {
            override fun onNextAction() {
                super.onNextAction()
                goToPermissionScreen()
            }

            override fun showNativeFullAd(ad: NativeBaseAdProducer) {
                super.showNativeFullAd(ad)

                showNativeFullScreenAd(ad)
            }

            override fun onAdFailedToShow() {
                super.onAdFailedToShow()
                goToPermissionScreen()
            }

            override fun onAdLoadFailed(isAutoPreload: Boolean) {
                super.onAdLoadFailed(isAutoPreload)
                if (isAutoPreload.not()) {
                    goToPermissionScreen()
                }
            }

            override fun requireActivityForLoadAndShowAd(): Activity? {
                return this@OnboardingHataki1Activity
            }
        })

        interIntroAdProducer.show(this, lifecycle)
    }

    override fun onNextFromNativeFullScreenAd() {
        super.onNextFromNativeFullScreenAd()
        goToPermissionScreen()
    }

    private fun goToPermissionScreen() {
        val nextScreenLibIntent = Intent(this@OnboardingHataki1Activity,
            RequestPermissionHataki1Activity::class.java)
        startActivity(nextScreenLibIntent)
        finishAffinity()
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, OnboardingHataki1Activity::class.java))
        }
    }
}
