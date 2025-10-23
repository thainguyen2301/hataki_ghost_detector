package com.hataki.ghostdetector.ui.onboard

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import androidx.viewpager2.widget.ViewPager2
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.OnboardingItem
import com.hataki.ghostdetector.databinding.ActivityOnboardingHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.PermissionHelper
import com.hataki.ghostdetector.ui.common.dp
import com.hataki.ghostdetector.ui.permission.RequestPermissionHataki1Activity
import com.hataki.ghostdetector.ui.start.StartHataki1Activity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingHataki1Activity :
    BaseActivity<OnboardingViewModel, ActivityOnboardingHataki1Binding>() {

    override fun viewModelClass(): Class<OnboardingViewModel> = OnboardingViewModel::class.java

    override fun getLayoutResource(): Int = R.layout.activity_onboarding_hataki_1

    private fun onCompleteOnboardingHataki1() {
        if (PermissionHelper.isAllPermissionGranted(this@OnboardingHataki1Activity)) {
            StartHataki1Activity.open(this@OnboardingHataki1Activity)
        } else {
            RequestPermissionHataki1Activity.open(this@OnboardingHataki1Activity)
        }
        finish()
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
    }

    override fun onResumeImpl() {
    }

    private fun initViewPagerHataki1() {
        val items = listOf(
            OnboardingItem(
                resources.getString(R.string.onboarding_title_1),
                resources.getString(R.string.onboarding_desc_1)
            ), OnboardingItem(
                resources.getString(R.string.onboarding_title_2),
                resources.getString(R.string.onboarding_desc_2)
            ), OnboardingItem(
                resources.getString(R.string.onboarding_title_3),
                resources.getString(R.string.onboarding_desc_3)
            ), OnboardingItem(
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
            val currentItem = binding.viewPager.currentItem
            if (currentItem < items.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                onCompleteOnboardingHataki1()
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == items.size - 1) resources.getString(R.string.get_started) else resources.getString(
                        R.string.next
                    )
            }
        })
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, OnboardingHataki1Activity::class.java))
        }
    }
}
