package com.ghostfinder.ghostdetector.radar.ui.privacy

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.databinding.ActivityPrivacyPolicyBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyActivity : BaseActivity<PrivacyPolicyViewModel, ActivityPrivacyPolicyBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, PrivacyPolicyActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_privacy_policy

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

    override fun onResumeImpl() {
    }
}