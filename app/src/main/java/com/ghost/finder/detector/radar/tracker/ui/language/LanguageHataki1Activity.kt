package com.ghost.finder.detector.radar.tracker.ui.language

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.data.model.Quadruple
import com.ghost.finder.detector.radar.tracker.databinding.ActivityLanguageHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
import com.ghost.finder.detector.radar.tracker.ui.common.LanguageItemView
import com.ghost.finder.detector.radar.tracker.ui.onboard.OnboardingHataki1Activity
import com.ghost.finder.detector.radar.tracker.ui.rada.RadaHataki1Activity
import com.ghost.finder.detector.radar.tracker.utils.DialogHelper
import com.ghost.finder.detector.radar.tracker.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageHataki1Activity : BaseActivity<LanguageViewModel, ActivityLanguageHataki1Binding>() {
    private fun setOnClickListenerHataki1() {
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(APP_LANG, null)
        if (lang == null) {
            binding.btnBack.visibility = View.GONE
        }else {
            binding.btnBack.visibility = View.VISIBLE
        }
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            DialogHelper.showTranslatingDialog(this@LanguageHataki1Activity)
            viewModel.selectedLanguage?.let { lang ->
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { putString(APP_LANG, lang) }
                LocaleHelper.setLocale(this@LanguageHataki1Activity, lang)
                val isFromSetting = intent.getBooleanExtra("isFromSetting", false)
                if (isFromSetting) {
                    val intent = Intent(this, RadaHataki1Activity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                } else {
                    OnboardingHataki1Activity.Companion.open(this@LanguageHataki1Activity)
                }

//                Handler(Looper.getMainLooper()).postDelayed({
//                    restartAppHataki1()
//                }, 3000)
            }
        }
    }

    private fun initListLanguageHataki1(currentLanguage: String?) {
        val languages = listOf(
            Quadruple("Hindi", R.drawable.ic_india, false, "hi"),
            Quadruple("French", R.drawable.ic_france, false, "fr"),
            Quadruple("Spanish", R.drawable.ic_spain, false, "es"),
            Quadruple("Portuguese", R.drawable.ic_portuguese, false, "pt"),
            Quadruple("Indonesian", R.drawable.ic_indonesian, false, "in"),
            Quadruple("German", R.drawable.ic_german, false, "de"),
            Quadruple("English", R.drawable.ic_english, true, "en-rUS")
        )

        languages.forEach { (name, iconRes, selected, flag) ->
            val item = LanguageItemView(this)
            val isSelected = currentLanguage?.let { it == flag } ?: selected
            item.setLanguage(name, iconRes, isSelected, flag)
            item.setOnClickListener {
                setSelectedLanguageHataki1(item)
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 8
            item.layoutParams = params
            binding.languageContainer.addView(item)
        }
    }

    private fun setSelectedLanguageHataki1(selectedItem: LanguageItemView) {
        for (i in 0 until binding.languageContainer.childCount) {
            val child = binding.languageContainer.getChildAt(i) as LanguageItemView
            child.isSelected = (child == selectedItem)
            viewModel.selectedLanguage = selectedItem.flag
        }
    }

    override fun onResumeImpl() {
    }

    fun restartAppHataki1() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        Runtime.getRuntime().exit(0) // kill process để đảm bảo restart sạch
    }

    override fun getLayoutResource(): Int = R.layout.activity_language_hataki_1

    override fun viewModelClass(): Class<LanguageViewModel> = LanguageViewModel::class.java

    override fun onCreateImpl() {
        setOnClickListenerHataki1()
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(APP_LANG, "en") ?: "en"
        initListLanguageHataki1(lang)
    }

    companion object {
        const val APP_LANG = "app_lang"
        fun open(context: Context, isFromSetting: Boolean = false) {
            context.startActivity(Intent(context, LanguageHataki1Activity::class.java).apply {
                putExtra("isFromSetting", isFromSetting)
            })
        }
    }
}