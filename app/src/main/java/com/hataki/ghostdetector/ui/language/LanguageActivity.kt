package com.hataki.ghostdetector.ui.language

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.Quadruple
import com.hataki.ghostdetector.data.model.common.UIState
import com.hataki.ghostdetector.databinding.ActivityLanguageBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.LanguageItemView
import com.hataki.ghostdetector.utils.DialogHelper
import com.hataki.ghostdetector.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageActivity : BaseActivity<LanguageViewModel, ActivityLanguageBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, LanguageActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_language

    override fun viewModelClass(): Class<LanguageViewModel> = LanguageViewModel::class.java

    override fun onCreateImpl() {
        setOnClickListener()
        viewModel.getCurrentLanguage()
        lifecycleScope.launch {
            viewModel.currentLanguageState.collect { state ->
                if (state is UIState.Success) {
                    initListLanguage(state.data)
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            DialogHelper.showTranslatingDialog(this@LanguageActivity)
            viewModel.selectedLanguage?.let {
                // FIXME: Need change in the future
                viewModel.saveLanguage()
                // FIXME: Need change in the future
                LocaleHelper.setLocale(this@LanguageActivity, "hi")
                Handler(Looper.getMainLooper()).postDelayed({
                    restartApp()
                }, 5000)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        // FIXME: Need change in the future
        val localeUpdatedContext = newBase?.let { LocaleHelper.setLocale(newBase, "hi") } ?: newBase
        super.attachBaseContext(localeUpdatedContext)
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