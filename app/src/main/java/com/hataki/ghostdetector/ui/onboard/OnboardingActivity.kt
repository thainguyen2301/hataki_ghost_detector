package com.hataki.ghostdetector.ui.onboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.OnboardingItem
import com.hataki.ghostdetector.databinding.ActivityOnboardingBinding
import com.hataki.ghostdetector.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewPager()
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
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnNext.text =
                    if (position == items.size - 1) resources.getString(R.string.get_started) else resources.getString(R.string.next)
            }
        })
    }
}
