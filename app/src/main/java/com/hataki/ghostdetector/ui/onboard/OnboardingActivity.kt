package com.hataki.ghostdetector.ui.onboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.OnboardingItem
import com.hataki.ghostdetector.data.model.common.UIState
import com.hataki.ghostdetector.data.model.common.getOrNull
import com.hataki.ghostdetector.databinding.ActivityOnboardingBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.PermissionHelper
import com.hataki.ghostdetector.ui.permission.RequestPermissionActivity
import com.hataki.ghostdetector.ui.start.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        observerValue()
        initViewPager()
    }

    override fun onResumeImpl() {
    }


    private fun observerValue() {
        lifecycleScope.launch {
            viewModel.saveIsOnBoardingState.collect { isSuccess ->
                if (isSuccess is UIState.Success && isSuccess.getOrNull() == true) {
                    if (PermissionHelper.isAllPermissionGranted(this@OnboardingActivity)) {
                        StartActivity.open(this@OnboardingActivity)
                    } else {
                        RequestPermissionActivity.open(this@OnboardingActivity)
                    }
                    finish()
                }
            }
        }
    }

    private fun initViewPager() {
        val items = listOf(
            OnboardingItem(
                resources.getString(R.string.onboarding_title_1),
                resources.getString(R.string.onboarding_desc_1)
            ),
            OnboardingItem(
                resources.getString(R.string.onboarding_title_2),
                resources.getString(R.string.onboarding_desc_2)
            ),
            OnboardingItem(
                resources.getString(R.string.onboarding_title_3),
                resources.getString(R.string.onboarding_desc_3)
            ),
            OnboardingItem(
                resources.getString(R.string.onboarding_title_4),
                resources.getString(R.string.onboarding_desc_4)
            )
        )
        adapter = OnboardingAdapter(items)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabIndicator, binding.viewPager) { tab, _ ->
            val view = LayoutInflater.from(this).inflate(R.layout.tab_item, null)
            tab.customView = view
        }.attach()
        binding.tabIndicator.setSelectedTabIndicator(
            ContextCompat.getDrawable(
                this,
                R.drawable.bg_tab_selected
            )
        )
        binding.tabIndicator.getTabAt(0)?.select()
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < items.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                viewModel.saveIsOnBoarding()
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
