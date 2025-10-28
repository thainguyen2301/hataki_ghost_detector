package com.ghostfinder.ghostdetector.radar.ui.rada

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import com.ghostfinder.ghostdetector.radar.R
import com.ghostfinder.ghostdetector.radar.ads.AppAdvertiseManager
import com.ghostfinder.ghostdetector.radar.databinding.ActivityRadaBinding
import com.ghostfinder.ghostdetector.radar.ui.base.BaseActivity
import com.ghostfinder.ghostdetector.radar.ui.rada.system.MediaPlayerManager
import com.ghostfinder.ghostdetector.radar.ui.rada.system.SoundPoolManager
import com.ghostfinder.ghostdetector.radar.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.get
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class RadaActivity() : BaseActivity<RadaViewModel, ActivityRadaBinding>() {
    private lateinit var mediaPlayerManager: MediaPlayerManager
    private lateinit var soundPoolManager: SoundPoolManager
    private var handler = Handler(Looper.getMainLooper())

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RadaActivity::class.java))
        }

        const val RADA_NUMBER_FORMAT = "%.3f"
    }

    override fun getLayoutResource(): Int = R.layout.activity_rada

    override fun viewModelClass(): Class<RadaViewModel> = RadaViewModel::class.java

    override fun onCreateImpl() {
        mediaPlayerManager = MediaPlayerManager()
        soundPoolManager = SoundPoolManager(this)
        setOnClickListener()
        observerData()
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

        lifecycleScope.launch {
            delay(500L)
            AppAdvertiseManager.showCollapsibleHomeBanner(this@RadaActivity, binding.frAdBottom)
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
                    viewModel.accelerometer.collect { accelerometer ->
                        if (binding.btnOnOff.isOn) {
                            updateRadaNumber(
                                emp1 = accelerometer.first,
                                emp2 = accelerometer.second,
                                evp1 = null,
                                evp2 = null
                            )
                        }
                    }
                }

                launch {
                    viewModel.magnetic.collect { magnetic ->
                        if (binding.btnOnOff.isOn) {
                            updateRadaNumber(
                                emp1 = null,
                                emp2 = null,
                                magnetic.first,
                                magnetic.second
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateRadaNumber(emp1: Float?, emp2: Float?, evp1: Float?, evp2: Float?) {
        try {
            emp1?.let {
                binding.emp1.text = RADA_NUMBER_FORMAT.format(it)
            }

            emp2?.let {
                binding.emp2.text = RADA_NUMBER_FORMAT.format(it)
            }
            evp1?.let {
                binding.evp1.text = RADA_NUMBER_FORMAT.format(it)
            }

            evp2?.let {
                binding.evp2.text = RADA_NUMBER_FORMAT.format(it)
            }
        } catch (e: Exception) {
            Log.d(RadaActivity::class.java.name, "${e.message}")
        }
    }

    private val startGhostRunnable = Runnable {
        binding.tvStart.visibility = View.GONE
        initialRadaValue()
    }

    private fun updateUIWhenCameraOnOff() {
        if (binding.btnOnOff.isOn) {
            binding.cameraView.visibility = View.VISIBLE
            binding.cameraAccessories.visibility = View.VISIBLE
            binding.cameraBorder.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.bgTop.visibility = View.GONE
            mediaPlayerManager.playBackgroundMusic(this)
            handler.postDelayed(startGhostRunnable, 5000)

        } else {
            handler.removeCallbacks(startGhostRunnable)
            mediaPlayerManager.stopBackgroundMusic()
            binding.cameraView.visibility = View.GONE
            binding.cameraAccessories.visibility = View.GONE
            binding.cameraBorder.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
            binding.bgTop.visibility = View.VISIBLE
            updateRadaNumber(0f, 0f, 0f, 0f)
        }
    }

    private fun setOnClickListener() {
        binding.btnOnOff.setOnToggleListener {
            if (binding.btnOnOff.isOn) {

            } else {
                binding.radarView.clearTargets()
            }
            updateUIWhenCameraOnOff()
        }
        binding.btnSetting.setOnClickListener {
            SettingActivity.Companion.open(this@RadaActivity)
        }
    }

    private fun initialRadaValue() {
        val ghostLength = (2..3).random()
        var ghostPoints = emptyList<Int>()
        for (i in 1..ghostLength) {
            var ghostAngle: Int
            do {
                ghostAngle = (0..359).random()
            } while (ghostPoints.any { existing -> abs(existing - ghostAngle) < 30 })
            ghostPoints = ghostPoints.plus(ghostAngle)
        }
        ghostPoints.forEach { i ->
            val distance = (50..99).random().toFloat()
            binding.radarView.addTarget(i.toFloat(), distance / 100)
            soundPoolManager.playBeep()
            spawnNewGhost(i.toFloat(), distance = distance / 10)
        }
    }

    private fun spawnNewGhost(targetAzimuth: Float, distance: Float = 10f) {
        val session = binding.cameraView.session ?: return
        val frame = binding.cameraView.frame ?: return
        if (frame.camera.trackingState != TrackingState.TRACKING) {
            handler.postDelayed({
                spawnNewGhost(targetAzimuth, distance)
            }, 2000)
            return
        }
        val cameraPose = frame.camera.pose

        val rad = Math.toRadians(targetAzimuth.toDouble()).toFloat()
        val camPos = cameraPose.translation
        val dx = (distance * sin(rad))
        val dz = (distance * cos(rad))

        val ghostPos = floatArrayOf(
            camPos[0] + dx,
            camPos[1],
            camPos[2] + dz
        )
        val ghostPose = Pose(ghostPos, floatArrayOf(0f, 0f, 0f, 1f))
        val anchor = session.createAnchor(ghostPose)

        val anchorNode = AnchorNode(binding.cameraView.engine, anchor)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ghost4)
        val ghostNode = ImageNode(
            materialLoader = binding.cameraView.materialLoader,
            bitmap = bitmap,
            size = Size(1.5f, 3.0f)
        )
        ghostNode.rotation = Rotation(
            x = 0f,
            y = 0f,
            z = 90f
        )
        anchorNode.addChildNode(ghostNode)
        binding.cameraView.addChildNode(anchorNode)
        ghostNode.onFrame = { _ ->
            val camPose = frame.camera.pose
            val camT = camPose.translation

            val dxLook = camT[0] - ghostNode.worldPosition.x
            val dzLook = camT[2] - ghostNode.worldPosition.z
            val yaw = Math.toDegrees(atan2(dxLook, dzLook).toDouble()).toFloat()

            ghostNode.worldRotation = Rotation(0f, yaw, 0f)
        }
        moveGhost(anchorNode)
        binding.radarView.startUpdate()
    }

    private fun moveGhost(anchorNode: AnchorNode) {
        var isMoving = true
        lifecycleScope.launch {
            while (isMoving) {
                delay(50)
                val cameraPos = binding.cameraView.cameraNode.worldPosition
                val ghostPos = anchorNode.worldPosition
                val dx = cameraPos.x - ghostPos.x
                val dy = cameraPos.y - ghostPos.y
                val dz = cameraPos.z - ghostPos.z
                val length = sqrt(dx * dx + dy * dy + dz * dz)
                val step = 0.02f
                val nx = dx / length
                val ny = dy / length
                val nz = dz / length
                anchorNode.position = Position(
                    ghostPos.x + nx * step,
                    ghostPos.y + ny * step,
                    ghostPos.z + nz * step
                )
                if (length < 0.5f) {
                    isMoving = false
                    delay(2000)
                    binding.radarView.stopUpdate()
                    binding.radarView.clearTargets()
                    binding.cameraView.removeChildNode(anchorNode)
                    initialRadaValue()
                    break
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerManager.onDestroy()
        soundPoolManager.onDestroy()
        binding.radarView.stopUpdate()
    }

    override fun onResumeImpl() {
        viewModel.startDetectCompass()
    }

    override fun onStop() {
        binding.btnOnOff.setOn(false)
        handler.removeCallbacks(startGhostRunnable)
        mediaPlayerManager.stopBackgroundMusic()
        binding.radarView.clearTargets()
        super.onStop()
        viewModel.stopDetectCompass()
    }
}