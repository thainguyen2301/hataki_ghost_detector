package com.hataki.ghostdetector.ui.rada

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.data.model.GhostSprite
import com.hataki.ghostdetector.databinding.ActivityRadaBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.rada.system.MediaPlayerManager
import com.hataki.ghostdetector.ui.rada.system.SoundPoolManager
import com.hataki.ghostdetector.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class RadaActivity : BaseActivity<RadaViewModel, ActivityRadaBinding>() {

    private lateinit var mediaPlayerManager: MediaPlayerManager
    private lateinit var soundPoolManager: SoundPoolManager
    private val handler = Handler(Looper.getMainLooper())

    companion object {
        fun open(context: Context) =
            context.startActivity(Intent(context, RadaActivity::class.java))

        private const val RADA_NUMBER_FORMAT = "%.3f"
    }

    private val overlayGhosts = mutableListOf<GhostSprite>()
    private var ghostIdCounter = 0L
    override fun getLayoutResource(): Int = R.layout.activity_rada
    override fun viewModelClass(): Class<RadaViewModel> = RadaViewModel::class.java

    override fun onCreateImpl() {
        mediaPlayerManager = MediaPlayerManager()
        soundPoolManager = SoundPoolManager(this)

        setupUI()
        observerData()
        setupEvent()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.azimuth.collect { az ->
                        binding.overlayView.deviceAzimuthDeg = az
                    }
                }
            }
        }
    }

    private fun initialRadaValue() {
        overlayGhosts.clear()

        val ghostLength = (2..3).random()
        var ghostPoints = emptyList<Int>()

        for (i in 1..ghostLength) {
            var ghostAngle: Int
            do {
                ghostAngle = (0..359).random()
            } while (ghostPoints.any { abs(it - ghostAngle) < 30 })
            ghostPoints = ghostPoints + ghostAngle
        }

        ghostPoints.forEach { angle ->
            val distance = (50..99).random().toFloat() / 100f
            val bmp = BitmapFactory.decodeResource(resources, R.drawable.ghost4)
            val id = ++ghostIdCounter
            val sprite =
                GhostSprite(id = id, angleDeg = angle.toFloat(), distance = distance, bitmap = bmp)
            overlayGhosts.add(sprite)
            binding.overlayView.addGhost(sprite)
            soundPoolManager.playBeep()
            moveGhostSprite(sprite)
        }
    }

    private fun moveGhostSprite(sprite: GhostSprite) {
        lifecycleScope.launch {
            val step = 0.01f
            while (isActive && sprite.distance > 0f) {
                delay(50)
                sprite.distance = (sprite.distance - step).coerceAtLeast(0f)
                binding.overlayView.invalidate()

                if (sprite.distance <= 0.05f) {
                    delay(1200)
                    binding.overlayView.removeGhostById(sprite.id)
                    overlayGhosts.removeAll { it.id == sprite.id }
                    binding.radarView.stopUpdate()
                    binding.radarView.clearTargets()
                    initialRadaValue()
                    break
                }
            }
        }
    }

    private fun setupUI() {
        val btnSettingsParams = binding.btnSetting.layoutParams as ViewGroup.MarginLayoutParams
        binding.btnSetting.layoutParams = btnSettingsParams

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            btnSettingsParams.setMargins(0, 106, 48, 0)
            val cameraBorderParams =
                binding.cameraBorder.layoutParams as ViewGroup.MarginLayoutParams
            cameraBorderParams.setMargins(0, 60, 0, 0)
            binding.cameraBorder.layoutParams = cameraBorderParams
        } else {
            btnSettingsParams.setMargins(0, 16, 16, 0)
        }
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
                    viewModel.accelerometer.collect { acc ->
                        if (binding.btnOnOff.isOn) {
                            updateRadaNumber(acc.first, acc.second, null, null)
                        }
                    }
                }

                launch {
                    viewModel.magnetic.collect { mag ->
                        if (binding.btnOnOff.isOn) {
                            updateRadaNumber(null, null, mag.first, mag.second)
                        }
                    }
                }
            }
        }
    }

    private fun updateRadaNumber(emp1: Float?, emp2: Float?, evp1: Float?, evp2: Float?) {
        try {
            emp1?.let { binding.emp1.text = RADA_NUMBER_FORMAT.format(it) }
            emp2?.let { binding.emp2.text = RADA_NUMBER_FORMAT.format(it) }
            evp1?.let { binding.evp1.text = RADA_NUMBER_FORMAT.format(it) }
            evp2?.let { binding.evp2.text = RADA_NUMBER_FORMAT.format(it) }
        } catch (_: Exception) {
        }
    }

    private val startGhostRunnable = Runnable {
        binding.tvStart.visibility = View.GONE
        initialRadaValue()
    }

    private fun setupEvent() {
        binding.btnOnOff.setOnToggleListener {
            if (!binding.btnOnOff.isOn) {
                binding.radarView.clearTargets()
            }
            updateUIWhenCameraOnOff()
        }

        binding.btnSetting.setOnClickListener {
            SettingActivity.open(this)
        }
    }

    private fun updateUIWhenCameraOnOff() {
        if (binding.btnOnOff.isOn) {
            startCamera()
            binding.overlayView.visibility = View.VISIBLE
            binding.cameraView.visibility = View.VISIBLE
            binding.cameraAccessories.visibility = View.VISIBLE
            binding.cameraBorder.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.bgTop.visibility = View.GONE

            mediaPlayerManager.playBackgroundMusic(this)
            handler.postDelayed(startGhostRunnable, 5000)
        } else {
            stopCamera()
            handler.removeCallbacks(startGhostRunnable)
            mediaPlayerManager.stopBackgroundMusic()
            binding.overlayView.clearGhosts()
            binding.overlayView.visibility = View.GONE
            binding.cameraView.visibility = View.GONE
            binding.cameraAccessories.visibility = View.GONE
            binding.cameraBorder.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
            binding.bgTop.visibility = View.VISIBLE
            updateRadaNumber(0f, 0f, 0f, 0f)
        }
    }

    override fun onResumeImpl() = viewModel.startDetectCompass()

    override fun onStop() {
        binding.btnOnOff.setOn(false)
        handler.removeCallbacks(startGhostRunnable)
        mediaPlayerManager.stopBackgroundMusic()
        binding.radarView.clearTargets()
        viewModel.stopDetectCompass()
        super.onStop()
    }

    override fun onDestroy() {
        mediaPlayerManager.onDestroy()
        soundPoolManager.onDestroy()
        binding.radarView.stopUpdate()
        super.onDestroy()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(binding.cameraView.surfaceProvider)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)

        }, ContextCompat.getMainExecutor(this))
    }

    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(this))
    }
}
