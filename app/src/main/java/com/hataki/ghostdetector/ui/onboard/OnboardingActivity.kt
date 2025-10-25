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
import com.hataki.ghostdetector.databinding.ActivityOnboardingBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.PermissionHelper
import com.hataki.ghostdetector.ui.common.dp
import com.hataki.ghostdetector.ui.permission.RequestPermissionActivity
import com.hataki.ghostdetector.ui.start.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<OnboardingViewModel, ActivityOnboardingBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, OnboardingActivity::class.java))
        }
    }

    private lateinit var adapter: OnboardingAdapter

    override fun getLayoutResource(): Int = R.layout.activity_onboarding

    override fun viewModelClass(): Class<OnboardingViewModel> = OnboardingViewModel::class.java

    override fun onCreateImpl() {
        initViewPager()
    }

    override fun onResumeImpl() {
    }


    private fun onCompleteOnboarding() {
        if (PermissionHelper.isAllPermissionGranted(this@OnboardingActivity)) {
            StartActivity.open(this@OnboardingActivity)
        } else {
            RequestPermissionActivity.open(this@OnboardingActivity)
        }
        finish()
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
                binding.viewPager.currentItem = currentItem + 1
            } else {
                onCompleteOnboarding()
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
}
