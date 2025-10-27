package com.hataki.ghostdetector.ui.internet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityErrorConnectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorConnectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorConnectionBinding

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, ErrorConnectionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_error_connection)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.btnRetry.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}