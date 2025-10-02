package com.hataki.ghostdetector.ui.language

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.Quadruple
import com.hataki.ghostdetector.databinding.ActivityLanguageBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.LanguageItemView
import com.hataki.ghostdetector.utils.DialogHelper
import com.hataki.ghostdetector.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : BaseActivity<LanguageViewModel, ActivityLanguageBinding>() {
    companion object {
        const val APP_LANG = "app_lang"
        fun open(context: Context) {
            context.startActivity(Intent(context, LanguageActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_language

    override fun viewModelClass(): Class<LanguageViewModel> = LanguageViewModel::class.java

    override fun onCreateImpl() {
        setOnClickListener()
        val lang = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(APP_LANG, "en") ?: "en"
        initListLanguage(lang)
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            DialogHelper.showTranslatingDialog(this@LanguageActivity)
            viewModel.selectedLanguage?.let { lang ->
                PreferenceManager.getDefaultSharedPreferences(this)
                    .edit { putString(APP_LANG, lang) }
                LocaleHelper.setLocale(this@LanguageActivity, lang)
                Handler(Looper.getMainLooper()).postDelayed({
                    restartApp()
                }, 3000)
            }
        }
    }

    private fun initListLanguage(currentLanguage: String?) {
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
                setSelectedLanguage(item)
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

    private fun setSelectedLanguage(selectedItem: LanguageItemView) {
        for (i in 0 until binding.languageContainer.childCount) {
            val child = binding.languageContainer.getChildAt(i) as LanguageItemView
            child.isSelected = (child == selectedItem)
            viewModel.selectedLanguage = selectedItem.flag
        }
    }

    override fun onResumeImpl() {
    }

    fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        Runtime.getRuntime().exit(0) // kill process để đảm bảo restart sạch
    }
}