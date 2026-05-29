import 'package:flutter/services.dart';

class NativeBridge {
  static const MethodChannel _channel =
  MethodChannel('vault/native');

  static Future<String?> processFile(String path) async {
    final result = await _channel.invokeMethod(
      'processFile',
      {
        'path': path,
      },
    );

    return result;
  }

  static Future<String?> reconstructFile(String vaultPath) async {
    final result = await _channel.invokeMethod(
      'reconstructFile',
      {
        'path': vaultPath,
      },
    );

    return result;
  }
}