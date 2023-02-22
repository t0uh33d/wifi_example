import 'package:flutter/material.dart';
import 'package:wifi_example/wifi_platform_channel.dart';
import 'package:wifi_example/wifi_scan_model.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
        actions: [
          ElevatedButton.icon(
              onPressed: () {
                setState(() {});
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Scan wifi'))
        ],
      ),
      body: SingleChildScrollView(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            FutureBuilder(
              future: WifiUtils.scanWifiNetworks(),
              builder: (context, snapshot) {
                if (!snapshot.hasData) {
                  return const Center(child: CircularProgressIndicator());
                }
                List<WifiScanResult> wifiRes =
                    List<WifiScanResult>.from(snapshot.data as List);
                return ListView.builder(
                  padding: const EdgeInsets.all(16),
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemCount: wifiRes.length,
                  itemBuilder: (context, index) {
                    return ListTile(
                      onTap: () {
                        WifiUtils.connectToNetwork(
                          ssid: wifiRes[index].ssid,
                          password: 'Wap@codewv09',
                        );
                      },
                      leading: const Icon(Icons.wifi),
                      title: Text(
                        wifiRes[index].ssid,
                      ),
                      subtitle: Text(
                        'bssid : ${wifiRes[index].bssid}',
                        textScaleFactor: 0.9,
                      ),
                    );
                  },
                );
              },
            )
          ],
        ),
      ),
    );
  }
}
