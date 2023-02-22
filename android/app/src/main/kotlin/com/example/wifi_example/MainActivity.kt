package com.example.wifi_example

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import java.util.*

class MainActivity : FlutterActivity() {
    private val CHANNEL = "wifi_utils"
    private var wifiManager: WifiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "scanWifiNetworks" -> {
                    val scanResults = wifiManager!!.scanResults
                    val wifiScanResults = ArrayList<Map<String, Any>>()

                    for (scanResult in scanResults) {
                        wifiScanResults.add(WifiScanResultMapper.mapToDartObject(scanResult))
                    }

                    result.success(wifiScanResults)
                }
                "connectToWifi" -> {
                    val ssid = call.argument<String>("ssid")
                    val password = call.argument<String>("password")

                    val wifiConfig = WifiConfiguration()
                    wifiConfig.SSID = "\"$ssid\""
                    wifiConfig.preSharedKey = "\"$password\""

                    val networkId = wifiManager!!.addNetwork(wifiConfig)

                    if (networkId != -1) {
                        wifiManager!!.enableNetwork(networkId, true)
                        result.success("Connected to $ssid")
                    } else {
                        result.error("ERROR", "Failed to connect to $ssid", null)
                    }
                }
                else -> result.notImplemented()
            }
        }
    }
}
