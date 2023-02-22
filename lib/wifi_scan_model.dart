class WifiScanResult {
  final String ssid;
  final String bssid;
  final int level;

  WifiScanResult(
      {required this.ssid, required this.bssid, required this.level});

  factory WifiScanResult.fromMap(Map<dynamic, dynamic> map) {
    return WifiScanResult(
      ssid: map['ssid'],
      bssid: map['bssid'],
      level: map['level'],
    );
  }
}
