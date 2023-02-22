package com.example.wifi_example

import android.net.wifi.ScanResult

object WifiScanResultMapper {
    fun mapToDartObject(scanResult: ScanResult): Map<String, Any> {
        return mapOf(
            "ssid" to scanResult.SSID,
            "bssid" to scanResult.BSSID,
            "level" to scanResult.level
        )
    }
}
