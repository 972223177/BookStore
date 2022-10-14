package com.ly.utils.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*

/**
 * 是否是模拟器，也可以补上是否有传感器之类的，或者说cpu型号是否有intel、amd这种字眼
 * manifest需要在queries标签中声明intent-action: android.intent.action.DIAL
 */
@Suppress("unused", "QueryPermissionsNeeded")
fun Context.isSimulator(): Boolean {
    val checkProperty = (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("vbox")
            || Build.FINGERPRINT.lowercase(Locale.getDefault()).contains("test-keys")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MODEL.contains("MuMu")
            || Build.MODEL.contains("virtual")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || "google_sdk" == Build.PRODUCT)
    if (checkProperty) return true
    val telephonyManager =
        getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager ?: return false
    val name = telephonyManager.networkOperatorName ?: ""
    val checkOperatorName = name.lowercase(Locale.getDefault()) == "android"
    if (checkOperatorName) return true

    val url = "tel:" + "123456"
    val intent = Intent()
    intent.data = Uri.parse(url)
    intent.action = Intent.ACTION_DIAL
    return intent.resolveActivity(packageManager) == null
}