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
import com.google.ar.core.Pose
import com.google.ar.core.TrackingState
import com.hataki.ghostdetector.R
import com.hataki.ghostdetector.databinding.ActivityRadaHataki1Binding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.rada.system.MediaPlayerManager
import com.hataki.ghostdetector.ui.rada.system.SoundPoolManager
import com.hataki.ghostdetector.ui.setting.SettingHataki1Activity
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class RadaHataki1Activity() : BaseActivity<RadaViewModel, ActivityRadaHataki1Binding>() {
    private lateinit var mediaPlayerManagerHataki1: MediaPlayerManager
    private var handlerHataki1 = Handler(Looper.getMainLooper())
    private lateinit var soundPoolManagerHataki1: SoundPoolManager

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerManagerHataki1.onDestroy()
        soundPoolManagerHataki1.onDestroy()
        binding.radarView.stopUpdate()
    }

    override fun onResumeImpl() {
        viewModel.startDetectCompass()
    }

    override fun onStop() {
        binding.btnOnOff.setOn(false)
        handlerHataki1.removeCallbacks(startGhostRunnableHataki1)
        mediaPlayerManagerHataki1.stopBackgroundMusic()
        binding.radarView.clearTargets()
        super.onStop()
        viewModel.stopDetectCompass()
    }

    private fun initialRadaValueHataki1() {
        val ghostLength = (1..3).random()
        var ghostPoints = emptyList<Int>()
        for (i in 1..ghostLength) {
            var ghostAngle: Int
            do {
                ghostAngle = (0..359).random()
            } while (ghostPoints.any { existing -> kotlin.math.abs(existing - ghostAngle) < 30 })
            ghostPoints = ghostPoints.plus(ghostAngle)
        }
        ghostPoints.forEach { i ->
            val distance = (50..99).random().toFloat()
            binding.radarView.addTarget(i.toFloat(), distance / 100)
            soundPoolManagerHataki1.playBeep()
            spawnNewGhostHataki1(i.toFloat(), distance = distance / 10)
        }
    }

    private fun spawnNewGhostHataki1(targetAzimuth: Float, distance: Float = 10f) {
        val session = binding.cameraView.session ?: return
        val frame = binding.cameraView.frame ?: return
        if (frame.camera.trackingState != TrackingState.TRACKING) {
            handlerHataki1.postDelayed({
                spawnNewGhostHataki1(targetAzimuth, distance)
            }, 2000)
            return
        }
        val cameraPose = frame.camera.pose

        val rad = Math.toRadians(targetAzimuth.toDouble())
        val dx = (distance * sin(rad)).toFloat()
        val dz = (distance * cos(rad)).toFloat()

        val ghostPose = cameraPose.compose(Pose.makeTranslation(dx, 0f, -dz))
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
        moveGhostHataki1(anchorNode)
        binding.radarView.startUpdate()
    }

    private fun moveGhostHataki1(anchorNode: AnchorNode) {
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
                    initialRadaValueHataki1()
                    break
                }
            }
        }
    }

    private fun observerDataHataki1() {
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
                            updateRadaNumberHataki1(
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
                            updateRadaNumberHataki1(
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

    private fun updateUIWhenCameraOnOffHataki1() {
        if (binding.btnOnOff.isOn) {
            binding.cameraView.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.bgTop.visibility = View.GONE
            mediaPlayerManagerHataki1.playBackgroundMusic(this)
            handlerHataki1.postDelayed(startGhostRunnableHataki1, 5000)

        } else {
            handlerHataki1.removeCallbacks(startGhostRunnableHataki1)
            mediaPlayerManagerHataki1.stopBackgroundMusic()
            binding.cameraView.visibility = View.GONE
            binding.tvStart.visibility = View.GONE
            binding.bgTop.visibility = View.VISIBLE
            updateRadaNumberHataki1(0f, 0f, 0f, 0f)
        }
    }

    private fun setOnClickListenerHataki1() {
        binding.btnOnOff.setOnToggleListener {
            if (binding.btnOnOff.isOn) {

            } else {
                binding.radarView.clearTargets()
            }
            updateUIWhenCameraOnOffHataki1()
        }
        binding.btnSetting.setOnClickListener {
            SettingHataki1Activity.open(this@RadaHataki1Activity)
        }
    }

    private fun updateRadaNumberHataki1(emp1: Float?, emp2: Float?, evp1: Float?, evp2: Float?) {
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
            Log.d(RadaHataki1Activity::class.java.name, "${e.message}")
        }
    }

    private val startGhostRunnableHataki1 = Runnable {
        binding.tvStart.visibility = View.GONE
        initialRadaValueHataki1()
    }

    override fun getLayoutResource(): Int = R.layout.activity_rada_hataki_1

    override fun viewModelClass(): Class<RadaViewModel> = RadaViewModel::class.java

    override fun onCreateImpl() {
        mediaPlayerManagerHataki1 = MediaPlayerManager()
        soundPoolManagerHataki1 = SoundPoolManager(this)
        setOnClickListenerHataki1()
        observerDataHataki1()
    }

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RadaHataki1Activity::class.java))
        }

        const val RADA_NUMBER_FORMAT = "%.3f"
    }
}