package com.example.wifi_example

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.*

class MainActivity : FlutterActivity() {

    private val CHANNEL = "wifi_utils"
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler(
                object : MethodCallHandler {
                    override fun onMethodCall(call: MethodCall, result: Result) {
                        if (call.method == "scanWifiNetworks") {
                            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                            val scanResults = wifiManager.scanResults
                            val wifiScanResults = ArrayList<Map<String, Any>>()

                            for (scanResult: ScanResult in scanResults) {
                                wifiScanResults.add(WifiScanResultMapper.mapToDartObject(scanResult))
                            }

                            result.success(wifiScanResults)
                        } else if (call.method == "connectToWifi") {
                            val ssid = call.argument<String>("ssid")
                            val password = call.argument<String>("password")

                            if (ssid != null && password != null) {
                                connectToWifi(ssid, password)
                                result.success(true)
                            } else {
                                Log.d(TAG, "Missing required arguments for connectToWifi method")
                                result.error("MISSING_ARGUMENTS", "Missing required arguments for connectToWifi method", null)
                            }
                        } else {
                            result.notImplemented()
                        }
                    }
                }
        )
    }

    private fun connectToWifi(ssid: String, password: String) {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Check if Wi-Fi is enabled
        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }

        // Set up the Wi-Fi network configuration
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = String.format("\"%s\"", ssid)
        wifiConfig.preSharedKey = String.format("\"%s\"", password)

        // Add the network configuration to the device's list of configured networks
        val networkId = wifiManager.addNetwork(wifiConfig)
        if (networkId == -1) {
            Log.d(TAG, "Failed to add network configuration")
            return
        }

        // Enable the network
        val connected = wifiManager.enableNetwork(networkId, true)
        if (connected) {
            Log.d(TAG, "Connected to Wi-Fi network $ssid")
        } else {
            Log.d(TAG, "Failed to connect to Wi-Fi network $ssid")
        }
    }
}
