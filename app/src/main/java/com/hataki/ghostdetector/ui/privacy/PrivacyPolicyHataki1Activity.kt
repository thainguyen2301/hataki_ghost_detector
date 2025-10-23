package com.hataki.ghostdetector.ui.privacy

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityPrivacyPolicyHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyHataki1Activity :
    BaseActivity<PrivacyPolicyViewModel, ActivityPrivacyPolicyHataki1Binding>() {

    override fun onResumeImpl() {
    }

    override fun viewModelClass(): Class<PrivacyPolicyViewModel> =
        PrivacyPolicyViewModel::class.java

    override fun onCreateImpl() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.webview.apply {
            webViewClient = WebViewClient()
            loadUrl(viewModel.policyUrl)
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_privacy_policy_hataki_1

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, PrivacyPolicyHataki1Activity::class.java))
        }
    }
}