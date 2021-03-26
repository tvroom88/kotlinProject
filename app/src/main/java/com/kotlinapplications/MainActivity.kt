package com.kotlinapplications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlinapplications.Bmi.BmiActivity
import com.kotlinapplications.GpsMap.GoogleMapsActivity
import com.kotlinapplications.MyGallery.MyGallery
import com.kotlinapplications.MyWebBrowser.MyWebActivity
import com.kotlinapplications.StopWatch.StopWatch
import com.kotlinapplications.TiltSensor.TiltSensor
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bmiButton.setOnClickListener {
            startActivity<BmiActivity>()
        }

        timerButton.setOnClickListener{
            startActivity<StopWatch>()
        }

        myWebButton.setOnClickListener{
            startActivity<MyWebActivity>()
        }

        tiltSensorButton.setOnClickListener{
            startActivity<TiltSensor>()
        }

        galleryButton.setOnClickListener{
            startActivity<MyGallery>()
        }

        gpsButton.setOnClickListener{
            startActivity<GoogleMapsActivity>()
        }
    }
}