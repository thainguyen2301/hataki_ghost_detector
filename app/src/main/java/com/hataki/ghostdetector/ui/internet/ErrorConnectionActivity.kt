package com.hataki.ghostdetector.ui.internet

import android.content.Context
import android.content.Intent
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityErrorConnectionBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorConnectionActivity :
    BaseActivity<ErrorConnectionViewModel, ActivityErrorConnectionBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, ErrorConnectionActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_error_connection

    override fun viewModelClass(): Class<ErrorConnectionViewModel> =
        ErrorConnectionViewModel::class.java

    override fun onCreateImpl() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnRetry.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onResumeImpl() {
    }
}