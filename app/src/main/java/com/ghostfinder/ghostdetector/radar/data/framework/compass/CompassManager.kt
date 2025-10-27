package com.ghostfinder.ghostdetector.radar.data.framework.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.round

class CompassManager(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val _azimuthFlow = MutableStateFlow(0f)
    val azimuthFlow: StateFlow<Float> = _azimuthFlow.asStateFlow()

    private val _accelerometerFlow = MutableStateFlow(Triple(0f, 0f, 0f))
    val accelerometerFlow: StateFlow<Triple<Float, Float, Float>> = _accelerometerFlow

    private val _magneticFlow = MutableStateFlow(Triple(0f, 0f, 0f))
    val magneticFlow: StateFlow<Triple<Float, Float, Float>> = _magneticFlow

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val accel = FloatArray(3)
    private val magnet = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    fun start() {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        scope.coroutineContext.cancel()
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val (x, y, z) = event.values
                System.arraycopy(event.values, 0, accel, 0, 3)
                _accelerometerFlow.tryEmit(Triple(x, y, z))

            }

            Sensor.TYPE_MAGNETIC_FIELD -> {
                val (x, y, z) = event.values
                System.arraycopy(event.values, 0, magnet, 0, 3)
                _magneticFlow.tryEmit(Triple(x, y, z))
            }
        }
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accel, magnet)) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            val normalized = ((azimuth + 360) % 360)
            _azimuthFlow.value = round(normalized * 10) / 10f
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
