package com.hataki.ghostdetector.ui.privacy

import android.content.Context
import android.content.Intent
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityPrivacyPolicyBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
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
    }

    override fun onResumeImpl() {
    }
}