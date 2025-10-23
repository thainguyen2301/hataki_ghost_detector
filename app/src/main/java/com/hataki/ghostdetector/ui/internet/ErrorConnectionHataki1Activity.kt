package com.hataki.ghostdetector.ui.internet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityErrorConnectionHataki1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorConnectionHataki1Activity : AppCompatActivity() {
    private lateinit var bindingHataki1: ActivityErrorConnectionHataki1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingHataki1 =
            DataBindingUtil.setContentView(this, R.layout.activity_error_connection_hataki_1)
        bindingHataki1.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        bindingHataki1.btnRetry.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        fun open(context: Context) {
            Log.d("cuongpq", "startActivity ErrorConnectionActivity")
            val intent = Intent(context, ErrorConnectionHataki1Activity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
        }
    }
}