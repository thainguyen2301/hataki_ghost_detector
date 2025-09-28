package com.hataki.ghostdetector.ui.rada

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityRadaBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.math.Position
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import kotlinx.coroutines.delay
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

    private fun updateUIWhenCameraOnOff() {
        if (binding.btnOnOff.isOn) {
            binding.cameraView.visibility = View.VISIBLE
            binding.cameraAccessories.visibility = View.VISIBLE
            binding.cameraBorder.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.bgTop.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvStart.visibility = View.GONE
            }, 5000)
            addGhost()
        } else {
            binding.cameraView.visibility = View.GONE
            binding.cameraAccessories.visibility = View.GONE
            binding.cameraBorder.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
            binding.bgTop.visibility = View.VISIBLE
        }
    }

    private fun setOnClickListener() {
        binding.btnOnOff.setOnToggleListener {
            if (binding.btnOnOff.isOn) {
                binding.radarView.addTarget(90f, 0.2f)
                binding.radarView.addTarget(200f, 0.6f)
                binding.radarView.addTarget(330f, 0.8f)
                viewModel.startDetectCompass()
            } else {
                viewModel.stopDetectCompass()
            }
            updateUIWhenCameraOnOff()
        }
    }

    private fun addGhost() {
        try {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ghost)
            val ghostNode = ImageNode(
                materialLoader = binding.cameraView.materialLoader,
                bitmap = bitmap,
                size = Size(1.5f, 3.0f),
            ).apply {
                position = Position(0f, 0f, -5f)
            }

            binding.cameraView.addChildNode(ghostNode)
            lifecycleScope.launch {
                while (true) {
                    delay(50)
                    ghostNode.position = Position(
                        ghostNode.position.x,
                        ghostNode.position.y,
                        ghostNode.position.z + 0.02f
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(RadaActivity::class.simpleName, "${e.message}")
        }
    }

    override fun onResumeImpl() {
    }
}