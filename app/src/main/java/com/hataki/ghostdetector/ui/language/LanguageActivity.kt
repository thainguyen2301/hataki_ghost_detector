package com.hataki.ghostdetector.ui.language

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityLanguageBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.common.LanguageItemView
import dagger.hilt.android.AndroidEntryPoint

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
        initListLanguage()
    }

    private fun setOnClickListener() {
        binding.btnBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
        binding.btnSave.setOnClickListener {
            // FIXME: Impl in the future
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initListLanguage() {
        val languages = listOf(
            Triple("Hindi", R.drawable.ic_india, false),
            Triple("French", R.drawable.ic_france, false),
            Triple("Spanish", R.drawable.ic_spain, false),
            Triple("Portuguese", R.drawable.ic_portuguese, false),
            Triple("Indonesian", R.drawable.ic_indonesian, false),
            Triple("German", R.drawable.ic_german, false),
            Triple("English", R.drawable.ic_english, true)
        )

        languages.forEach { (name, flag, selected) ->
            val item = LanguageItemView(this)
            item.setLanguage(name, flag, selected)
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
            // FIXME: Change app language
        }
    }

    override fun onResumeImpl() {
    }
}