package com.ghostfinder.ghostdetector.radar.ui.start

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.ViewGroup
import com.ghostfinder.ghostdetector.R
import com.ghostfinder.ghostdetector.databinding.ActivityStartBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.rada.RadaActivity
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
            RadaActivity.Companion.open(this@StartActivity)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            val params = binding.cornerOverlay.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, 80, 0, 0)
            binding.cornerOverlay.layoutParams = params
        }
    }

    override fun onResumeImpl() {

    }
}