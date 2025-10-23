package com.hataki.ghostdetector.ui.start

import android.content.Context
import android.content.Intent
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityStartHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.rada.RadaHataki1Activity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartHataki1Activity : BaseActivity<StartViewModel, ActivityStartHataki1Binding>() {

    override fun getLayoutResource(): Int = R.layout.activity_start_hataki_1

    override fun onCreateImpl() {
        binding.btnAccept.setOnClickListener {
            RadaHataki1Activity.open(this@StartHataki1Activity)
        }
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, StartHataki1Activity::class.java))
        }
    }

    override fun viewModelClass(): Class<StartViewModel> = StartViewModel::class.java

    override fun onResumeImpl() {

    }
}