package com.hataki.ghostdetector.ui.rada

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityRadaBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RadaActivity() : BaseActivity<RadaViewModel, ActivityRadaBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RadaActivity::class.java))
        }
    }
    override fun getLayoutResource(): Int = R.layout.activity_rada

    override fun viewModelClass(): Class<RadaViewModel> = RadaViewModel::class.java

    override fun onCreateImpl() {
        setOnClickListener()
        observerData()
    }
    private fun observerData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.azimuth.collect { azimuth ->
                        Log.d("cuongpq azimuth", "$azimuth")
                        binding.radarView.setAzimuth(azimuth)
                    }
                }

                launch {
                    viewModel.accelerometer.collect { accelerometer ->
                        binding.emp1.text = "%.3f".format(accelerometer.first)
                        binding.emp2.text = "%.3f".format(accelerometer.second)
                    }
                }

                launch {
                    viewModel.magnetic.collect { magnetic ->
                        binding.evp1.text = "%.3f".format(magnetic.first)
                        binding.evp2.text = "%.3f".format(magnetic.second)
                    }
                }
            }
        }
    }

    private fun setOnClickListener() {
        binding.btnOnOff.setOnToggleListener {
            if (binding.btnOnOff.isOn) {
//                viewModel.startDetectSensor()
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
//                    viewModel.startDetectEVP()
                }
                binding.radarView.addTarget(90f, 0.2f)   // điểm ở hướng trên
                binding.radarView.addTarget(200f, 0.6f)  // điểm bên trái dưới
                binding.radarView.addTarget(330f,0.8f)  // điểm bên phải trên
                viewModel.startDetectCompass()
            } else {
//                viewModel.stopDetectSensor()
//                viewModel.stopDetectEVP()
                viewModel.stopDetectCompass()
            }
        }
    }

    override fun onResumeImpl() {
    }
}