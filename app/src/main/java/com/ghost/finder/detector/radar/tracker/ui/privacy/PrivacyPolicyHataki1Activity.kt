package com.ghost.finder.detector.radar.tracker.ui.privacy

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.ghost.finder.detector.radar.tracker.R
import com.ghost.finder.detector.radar.tracker.databinding.ActivityPrivacyPolicyHataki1Binding
import com.ghost.finder.detector.radar.tracker.ui.base.BaseActivity
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