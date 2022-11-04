import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'example_superapp_platform_interface.dart';
import 'model/miniapp.dart';

class MethodChannelExampleSuperapp extends ExampleSuperappPlatform {

  @visibleForTesting
  final methodChannel = const MethodChannel('example_superapp');

  @override
  Future<List<MiniApp>?> getMiniApps(String tag) async {
    final miniAppsJson = await methodChannel.invokeMethod<String>('getMiniApps', {"tag": tag});
    return fromJson(miniAppsJson);
  }

  @override
  Future<List<MiniApp>?> getCachedMiniApps() async {
    final miniAppsJson = await methodChannel.invokeMethod<String>('getCachedMiniApps');
    return fromJson(miniAppsJson);
  }

  List<MiniApp>? fromJson(String? miniAppsJson) {
    if (miniAppsJson == null || miniAppsJson.isEmpty) {
      return null;
    }

    var miniApps = (json.decode(miniAppsJson) as List)
        .map((e) => MiniApp.fromJson(e))
        .toList();

    return miniApps;
  }

  @override
  Future<bool?> loadMiniApp(MiniApp miniApp) async {
    var miniAppJson = jsonEncode(miniApp.toJson());
    return await methodChannel.invokeMethod<bool>('load', {"miniAppJson": miniAppJson});
  }

  @override
  Future<bool?> remove(MiniApp miniApp) async {
    var miniAppJson = jsonEncode(miniApp.toJson());
    return await methodChannel.invokeMethod<bool>('remove', {"miniAppJson": miniAppJson});
  }
}
