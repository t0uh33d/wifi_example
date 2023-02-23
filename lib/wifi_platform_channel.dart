import 'dart:developer';

import 'package:flutter/services.dart';

import 'wifi_scan_model.dart';

class WifiUtils {
  static const MethodChannel _channel = MethodChannel('wifi_utils');

  static Future<List<WifiScanResult>> scanWifiNetworks() async {
    try {
      List<dynamic> results = await _channel.invokeMethod('scanWifiNetworks');
      log(results.toString());
      return results.map((result) => WifiScanResult.fromMap(result)).toList();
    } catch (e) {
      print('Error scanning Wi-Fi networks: $e');
      return [];
    }
  }

  static void connectToNetwork(
      {required String ssid, required String password}) async {
    try {
      bool results = await _channel.invokeMethod('connectToWifi', {
        "ssid": ssid,
        "password": password,
      });
      log(results.toString());
    } catch (e) {
      print('Error connecting Wi-Fi networks: $e');
    }
  }
}
