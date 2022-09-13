package com.android.tne.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.android.tne.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 2022.09.13
 * 스플래시 액티비티
 */

class SplashActivity : AppCompatActivity() {

    //s 이전
    private val PERMISSIONS = arrayOf( android.Manifest.permission.ACCESS_FINE_LOCATION)
    //s 이후 권한
    private val PERMISSIONS_S_ABOVE = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
//        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val REQUEST_ALL_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkBuildVersion()
    }

    private fun checkBuildVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermissions(this, PERMISSIONS_S_ABOVE)) {
                requestPermissions(PERMISSIONS_S_ABOVE, REQUEST_ALL_PERMISSION)
            }else{
                intentNextPage()
            }

        } else {
            if (!hasPermissions(this, PERMISSIONS)) {
                requestPermissions(PERMISSIONS, REQUEST_ALL_PERMISSION)
            }
        }
    }
    fun intentNextPage(){
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 2022.09.13
     * Permission Check
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_ALL_PERMISSION -> {
                // 퍼미션 요청 취소 혹은 결과 배열이 비어있을 때
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_SHORT).show()
                    intentNextPage()
                } else {
                    //permissions array, Request OK Code
                    requestPermissions(permissions, REQUEST_ALL_PERMISSION)
                    Toast.makeText(this, "Permissions must be granted\nPlease Pemission Turn On in the Setting Page", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun hasPermissions(context: Context,permissions: Array<String>) : Boolean {
        //거부항목 존재할 시 false 반환
        for( permission in permissions){
            if(ActivityCompat.checkSelfPermission(context,permission)
                != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }
}