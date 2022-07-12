package com.vrt.knaufwidget

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    companion object {
        const val CALLBACK_ID = 42
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission(CALLBACK_ID, Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        val res = UtilityTest().readCalendarEvent(this)
        println("TESTTESTEST $res")


    }


    private fun checkPermission(callbackId: Int, vararg permissionsId: String) {
        var permissions = true
        for (p in permissionsId) {
            permissions =
                permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED
        }
        if (!permissions) ActivityCompat.requestPermissions(this, permissionsId, callbackId)
    }


    private val appID = """4779bfe3-69a7-4335-a003-7b28ae1bcd71"""

}