import 'package:example_superapp/model/miniapp.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'example_superapp_method_channel.dart';

abstract class ExampleSuperappPlatform extends PlatformInterface {

  ExampleSuperappPlatform() : super(token: _token);

  static final Object _token = Object();
  static ExampleSuperappPlatform _instance = MethodChannelExampleSuperapp();
  static ExampleSuperappPlatform get instance => _instance;

  static set instance(ExampleSuperappPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<List<MiniApp>?> getMiniApps(String tag) {
    throw UnimplementedError('getMiniApps(tag: String) has not been implemented.');
  }

  Future<List<MiniApp>?> getCachedMiniApps() {
    throw UnimplementedError('getCachedMiniApps() has not been implemented.');
  }

  Future<bool?> loadMiniApp(MiniApp miniApp) {
    throw UnimplementedError('load(miniApp: MiniApp) has not been implemented.');
  }

  Future<bool?> remove(MiniApp miniApp) {
    throw UnimplementedError('remove(miniApp: MiniApp) has not been implemented.');
  }
}
