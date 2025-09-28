package com.hataki.ghostdetector.ui.start

import android.content.Context
import android.content.Intent
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityStartBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.rada.RadaActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : BaseActivity<StartViewModel, ActivityStartBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, StartActivity::class.java))
        }
    }

    override fun getLayoutResource(): Int = R.layout.activity_start

    override fun viewModelClass(): Class<StartViewModel> = StartViewModel::class.java

    override fun onCreateImpl() {
        binding.btnAccept.setOnClickListener {
            RadaActivity.open(this@StartActivity)
        }
    }

    override fun onResumeImpl() {

    }
}