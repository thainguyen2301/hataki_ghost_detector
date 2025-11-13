package com.ghost.finder.detector.radar.tracker.ui.language

import android.content.Intent
import android.util.Log
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.ads.HKTAdRemoteConfig
import com.ghost.finder.detector.radar.tracker.ads.HKTAppAdvertiseManager
import com.ghost.finder.detector.radar.tracker.ads.model.HKTDoneButtonPosition
import com.ghost.finder.detector.radar.tracker.data.model.LanguageItem
import com.ghost.finder.detector.radar.tracker.databinding.ActivityHatakiLanguageStartBinding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.dialog.TranslatingDialog
import com.ghost.finder.detector.radar.tracker.ui.onboard.OnboardingHataki1Activity
import com.ghost.finder.detector.radar.tracker.ui.rada.RadaHataki1Activity
import com.ghost.finder.detector.radar.tracker.utils.LocaleHelper
import com.ghost.finder.detector.radar.tracker.utils.tap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HatakiLanguageStartActivity : BaseActivity<HatakiLanguageViewModel, ActivityHatakiLanguageStartBinding>() {
    private val translatingDialog by lazy { TranslatingDialog(this) }
    private val adapter by lazy { LanguageHatakiAdapter() }
    private val languageScreenConfig = HKTAdRemoteConfig.getLanguageScreenConfig()
    

    override fun onResume() {
        super.onResume()
        hatakiShowNativeMain()
    }

    override fun getLayoutResource(): Int = R.layout.activity_hataki_language_start

    override fun viewModelClass(): Class<HatakiLanguageViewModel> = HatakiLanguageViewModel::class.java

    override fun onCreateImpl() {

        binding.rvLanguage.adapter = adapter
        HKTAppAdvertiseManager.loadFirstIntroNativeAd(this)
        setupRecyclerView()
        setupObservers()
        setupDoneButton()
        
        val doneClickListener = {
            viewModel.getSelectedCode()?.let { lang ->
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { putString(APP_LANG, lang) }
                LocaleHelper.setLocale(this@HatakiLanguageStartActivity, lang)
                val isFromSetting = intent.getBooleanExtra("isFromSetting", false)
                if (isFromSetting) {
                    val intent = Intent(this, RadaHataki1Activity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    OnboardingHataki1Activity.Companion.open(this@HatakiLanguageStartActivity)
                }
            }
        }

        binding.ivDoneLeft.tap { doneClickListener() }
        binding.ivDoneRight.tap { doneClickListener() }
        
    }

    override fun onResumeImpl() {
    }
    

    private fun setupDoneButton() {
        // Initially hide both buttons
        binding.ivDoneLeft.isVisible = false
        binding.ivDoneRight.isVisible = false
    }

    private fun setupRecyclerView() {
        binding.rvLanguage.layoutManager = LinearLayoutManager(this)
        binding.rvLanguage.adapter = adapter

        adapter.setOnItemClick { languageItem ->
            viewModel.selectLang(languageItem)
            when (languageItem) {
                is LanguageItem.Parent -> {
                    hatakiShowNativeAfter()
                    setupDoneButton()
                    HKTAppAdvertiseManager.loadDropLanguageNativeAds(this)
                }

                is LanguageItem.ParentWithoutChild -> {
                    hatakiShowNativeAfter()
                }

                else ->  hatakiShowNativeDrop()
            }
        }
    }

    override fun setupObservers() {
        viewModel.language.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.selectedRegion.observe(this) { selectedId ->
            val code = viewModel.getSelectedCode()
            val regionName = viewModel.getSelectedRegionName()
            if (selectedId.isNotBlank()) {
                showTranslatingDialog()
            }
            Log.d("LanguageActivity", "Selected: code=$code, region=$regionName (id=$selectedId)")
        }

        viewModel.getLanguageList(screenOpenCount = 1)
    }

    private fun showTranslatingDialog() {
        // Show translating popup only if config allows
        if (languageScreenConfig.isShowTranslatingPopUp) {
            if (!translatingDialog.isShowing) {
                translatingDialog.show()
            }
        }

        // Calculate total delay: translating loading time + done button show delay
        val popUpLoadingTime = languageScreenConfig.translatingLoadingSecond * 1000L
        val doneButtonDelayTime = languageScreenConfig.doneButtonShowAfterSecond * 1000L


        lifecycleScope.launch (Dispatchers.Main) {
            delay(popUpLoadingTime)
            if (translatingDialog.isShowing) {
                translatingDialog.dismiss()
            }

            delay(doneButtonDelayTime)
            // Show the appropriate done button based on config position
            when (languageScreenConfig.doneButtonPosition) {
                HKTDoneButtonPosition.LEFT -> {
                    binding.ivDoneLeft.isVisible = true
                    binding.ivDoneRight.isVisible = false
                }
                HKTDoneButtonPosition.RIGHT -> {
                    binding.ivDoneLeft.isVisible = false
                    binding.ivDoneRight.isVisible = true
                }
            }
        }
    }

    private fun hatakiShowNativeAfter() {
        HKTAppAdvertiseManager.selectedLanguageNativeProducer?.show(this,
            R.layout.layout_native_ad_medium_button_top, binding.frAdsLangBottom)
    }

    private fun hatakiShowNativeDrop() {
        HKTAppAdvertiseManager.selectedChildLanguageNativeProducer?.show(this,R.layout.layout_native_ad_medium_button_top, binding.frAdsLangBottom)
    }

    private fun hatakiShowNativeMain() {
        HKTAppAdvertiseManager.mainLanguageNativeProducer?.show(this,R.layout.layout_native_ad_medium_button_top, binding.frAdsLangBottom)
    }

    companion object {
        const val APP_LANG = "app_lang"
    }

}