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
import com.hataki.ghostdetector.databinding.ActivityRadaBinding
import com.hataki.ghostdetector.ui.base.BaseActivity
import com.hataki.ghostdetector.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Size
import io.github.sceneview.node.ImageNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class RadaActivity() : BaseActivity<RadaViewModel, ActivityRadaBinding>() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, RadaActivity::class.java))
        }

        const val RADA_NUMBER_FORMAT = "%.3f"
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
                        updateRadaNumber(
                            emp1 = accelerometer.first,
                            emp2 = accelerometer.second,
                            evp1 = null,
                            evp2 = null
                        )

                    }
                }

                launch {
                    viewModel.magnetic.collect { magnetic ->
                        updateRadaNumber(emp1 = null, emp2 = null, magnetic.first, magnetic.second)
                        binding.evp1.text = RADA_NUMBER_FORMAT.format(magnetic.first)
                        binding.evp2.text = RADA_NUMBER_FORMAT.format(magnetic.second)
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

    private fun updateUIWhenCameraOnOff() {
        if (binding.btnOnOff.isOn) {
            binding.cameraView.visibility = View.VISIBLE
            binding.cameraAccessories.visibility = View.VISIBLE
            binding.cameraBorder.visibility = View.VISIBLE
            binding.tvStart.visibility = View.VISIBLE
            binding.bgTop.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvStart.visibility = View.GONE
                initialRadaValue()
            }, 5000)

        } else {
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
                viewModel.startDetectCompass()
            } else {
                viewModel.stopDetectCompass()
                binding.radarView.clearTargets()
            }
            updateUIWhenCameraOnOff()
        }
        binding.btnSetting.setOnClickListener {
            SettingActivity.open(this@RadaActivity)
        }
    }

    private fun initialRadaValue() {
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
            spawnNewGhost(i.toFloat(), distance = distance / 10)
        }
    }

    private fun spawnNewGhost(targetAzimuth: Float, distance: Float = 10f) {
        val session = binding.cameraView.session ?: return
        val frame = binding.cameraView.frame ?: return
        if (frame.camera.trackingState != TrackingState.TRACKING) {
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                spawnNewGhost((0..359).random().toFloat())
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

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ghost)
        val ghostNode = ImageNode(
            materialLoader = binding.cameraView.materialLoader,
            bitmap = bitmap,
            size = Size(1.5f, 3.0f)
        )

        anchorNode.addChildNode(ghostNode)
        binding.cameraView.addChildNode(anchorNode)
        moveGhost(anchorNode)
        binding.radarView.startUpdate()
    }

    private fun moveGhost(anchorNode: AnchorNode) {
        lifecycleScope.launch {
            while (true) {
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
        binding.radarView.stopUpdate()
    }

    override fun onResumeImpl() {
    }
}