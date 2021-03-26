package com.kotlinapplications.GpsMap

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.kotlinapplications.R
import org.jetbrains.anko.*
import java.util.jar.Manifest

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: MyLocationCallBack


    //PolyLine 옵션 -> 지도에 갔던길을 표시해주기 위한 것인듯.
    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)

    private val REQUEST_ACCESS_LOCATION = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //세로 모드 화면 고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_google_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // SupportMapFragment를 가져와서 지도가 준비되면 알림을 받습니다
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //location 초기화
        locationInit()
    }

    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        locationCallback = MyLocationCallBack()

        locationRequest = LocationRequest()

        //GPS 우선
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

//    이 메소드는 앱에서 위치 업데이트 수신간격을 밀리초단위로 설정한다.
        locationRequest.interval = 10000
        // 다른 앱에 의해 빨라 질수는 있어도 그거에 영향 안받게 최소 설정
        locationRequest.fastestInterval = 5000
    }

    override fun onResume() {
        super.onResume()
        // 권한 요청 ⑨
        permissionCheck(cancel = {
            // 위치 정보가 필요한 이유 다이얼로그 표시 ⑩
            showPermissionInfoDialog()
        }, ok = {
            // 현재 위치를 주기적으로 요청 (권한이 필요한 부분) ⑪
            addLocationListener()
        })
    }

    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)

            val location = p0?.lastLocation

            location?.run {
                //14 level로 확대하고 현재 위치로 카메라 이
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Log.d("GoogleMapsActivity", "위도: $latitude, 경도: $longitude")

                //PolyLine에 좌표를 추가
                polylineOptions.add(latLng)
                mMap.addPolyline(polylineOptions)
            }
        }
    }

    private fun permissionCheck(cancel: () -> Unit, ok: () -> Unit) {
        // 위치 권한이 있는지 검사
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되지 않음
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 이전에 권한을 한 번 거부한 적이 있는 경우에 실행할 함수
                cancel()
            } else {
                // 권한 요청
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_LOCATION)
            }
        } else {
            // 권한을 수락 했을 때 실행할 함수
            ok()
        }
    }


    private fun showPermissionInfoDialog() {
        alert("현재 위치 정보를 얻기 위해서는 위치 권한이 필요합니다", "권한이 필요한 이유") {
            yesButton {
                // 권한 요청
                ActivityCompat.requestPermissions(this@GoogleMapsActivity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_ACCESS_LOCATION)
            }
            noButton { }
        }.show()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            REQUEST_ACCESS_LOCATION -> {
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //권한이 허용됨
                    addLocationListener()
                } else {
                    //권한 거부
                   toast("권한 거부 됨")
                }
                return
            }
        }
    }

    override fun onPause() {
        super.onPause()

        removeLocationListener()
    }

    //현재 위치 요청을 삭제
    private fun removeLocationListener() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}