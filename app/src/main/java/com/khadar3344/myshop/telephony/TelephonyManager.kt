package com.khadar3344.myshop.telephony

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import javax.inject.Inject

class TelephonyManager @Inject constructor(private val context: Context) {
    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private var phoneStateListener: PhoneStateListener? = null

    fun makePhoneCall(phoneNumber: String) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } else {
            Log.e("TelephonyManager", "Call permission not granted")
        }
    }

    fun startCallStateMonitoring() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            phoneStateListener = object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    when (state) {
                        TelephonyManager.CALL_STATE_IDLE -> {
                            Log.d("TelephonyManager", "Call State: IDLE")
                        }
                        TelephonyManager.CALL_STATE_RINGING -> {
                            Log.d("TelephonyManager", "Call State: RINGING")
                        }
                        TelephonyManager.CALL_STATE_OFFHOOK -> {
                            Log.d("TelephonyManager", "Call State: OFFHOOK")
                        }
                    }
                }
            }
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        } else {
            Log.e("TelephonyManager", "Read phone state permission not granted")
        }
    }

    fun stopCallStateMonitoring() {
        phoneStateListener?.let {
            telephonyManager.listen(it, PhoneStateListener.LISTEN_NONE)
            phoneStateListener = null
        }
    }

    fun getDeviceId(): String? {
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                telephonyManager.deviceId
            } catch (e: SecurityException) {
                Log.e("TelephonyManager", "Error getting device ID", e)
                null
            }
        } else {
            Log.e("TelephonyManager", "Read phone state permission not granted")
            null
        }
    }
}
