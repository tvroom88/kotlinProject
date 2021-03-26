package com.kotlinapplications.TiltSensor

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.kotlinapplications.R

class TiltSensor : AppCompatActivity(), SensorEventListener {

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private lateinit var tiltView: TiltView

    override fun onCreate(savedInstanceState: Bundle?) {

        // 화면이 꺼지지 않게 하기
        window. addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        // 화면이 가로 모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_tilt_sensor)

        tiltView = TiltView(this)
        setContentView(tiltView)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            Log.d("TiltSonsor", "onSensorChanged: x : " + "${event.values[0]} y : ${event.values[1]} z : ${event.values[2]}")
        }

        tiltView.onSensorEvent(event!!)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
//SensorEvent.values[0] x
//SensorEvent.values[1] y
//SensorEvent.values[2] z