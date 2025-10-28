package com.ghostfinder.ghostdetector.radar.ui.language

import android.content.Intent

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ghostfinder.ghostdetector.radar.AdRemoteConfig
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.ads.dialog.TranslatingHatakiDialog
import com.ghostfinder.ghostdetector.radar.databinding.ActivityHatakiLanguageStartBinding
import com.ghostfinder.ghostdetector.radar.model.DoneButtonPosition
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.language.LanguageActivity.Companion.APP_LANG
import com.ghostfinder.ghostdetector.radar.ui.onboard.OnboardingActivity
import com.ghostfinder.ghostdetector.radar.utils.LocaleHelper
import com.mobile.hataki_ad_lib.ad_native.NativeAdListener
import com.voicechanger.effect.changevoice.ui.language.HatakiLanguageViewModel
import com.voicechanger.effect.changevoice.ui.language.LanguageHatakiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tap

class HatakiLanguageStartActivity : BaseActivity<HatakiLanguageViewModel, ActivityHatakiLanguageStartBinding>() {
    private val translatingDialog by lazy { TranslatingHatakiDialog(this) }
    private val adapter by lazy { LanguageHatakiAdapter() }
    private val languageScreenConfig = AdRemoteConfig.getLanguageScreenConfig()


    private fun setupDoneButton() {
        // Initially hide both buttons
        binding.ivDoneLeft.isVisible = false
        binding.ivDoneRight.isVisible = false
    }

    private fun setupRecyclerView() {
        binding.rvLanguage.layoutManager = LinearLayoutManager(this)
        binding.rvLanguage.adapter = adapter

        adapter.setOnItemClick { LanguageHatakiItem ->
            viewModel.selectLang(LanguageHatakiItem)
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

    override fun getLayoutResource(): Int = R.layout.activity_hataki_language_start

    override fun viewModelClass(): Class<HatakiLanguageViewModel> = HatakiLanguageViewModel::class.java

    override fun onCreateImpl() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

        binding.rvLanguage.adapter = adapter

        setupRecyclerView()
        setupDoneButton()
        lifecycleScope.launch {
            delay(500L)
            hatakiShowNativeMain()
        }

        val doneClickListener = {
            val language = viewModel.getSelectedCode() ?: ""
            PreferenceManager.getDefaultSharedPreferences(this)
                .edit { putString(APP_LANG, language) }
            LocaleHelper.setLocale(this@HatakiLanguageStartActivity, language)
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        }

        binding.ivDoneLeft.tap { doneClickListener() }
        binding.ivDoneRight.tap { doneClickListener() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    override fun onResumeImpl() {
        // No implementation needed for now
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


        lifecycleScope.launch(Dispatchers.Main) {
            delay(popUpLoadingTime)
            if (translatingDialog.isShowing) {
                translatingDialog.dismiss()
            }

            delay(doneButtonDelayTime)
            // Show the appropriate done button based on config position
            when (languageScreenConfig.doneButtonPosition) {
                DoneButtonPosition.LEFT -> {
                    binding.ivDoneLeft.isVisible = true
                    binding.ivDoneRight.isVisible = false
                }
                DoneButtonPosition.RIGHT -> {
                    binding.ivDoneLeft.isVisible = false
                    binding.ivDoneRight.isVisible = true
                }
            }
        }
    }

    private fun hatakiShowNativeMain() {
        AppAdvertiseManager.mainLanguageNativeProducer?.let { nativeAdProducer ->
            val show = {
                nativeAdProducer.show(this,
                    R.layout.layout_native_ad_medium_button_top, binding.frAdsLangBottom)
            }

            nativeAdProducer.setListener(object : NativeAdListener {
                override fun onAdLoaded(isAutoLoad: Boolean) {
                    super.onAdLoaded(isAutoLoad)
                    show()
                }
            })

            show()
        }

    }
}