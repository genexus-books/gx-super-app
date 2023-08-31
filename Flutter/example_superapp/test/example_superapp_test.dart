import 'package:example_superapp/model/miniapp.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:example_superapp/example_superapp.dart';
import 'package:example_superapp/example_superapp_platform_interface.dart';
import 'package:example_superapp/example_superapp_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockExampleSuperappPlatform
    with MockPlatformInterfaceMixin
    implements ExampleSuperappPlatform {

  @override
  Future<List<MiniApp>?> getMiniApps(String tag) => Future.value([]);

  @override
  Future<List<MiniApp>?> getCachedMiniApps() => Future.value([]);

  @override
  Future<bool?> loadMiniApp(MiniApp miniApp) => Future.value(true);

  @override
  Future<bool?> remove(MiniApp miniApp) => Future.value(true);
}

void main() {
  final ExampleSuperappPlatform initialPlatform = ExampleSuperappPlatform.instance;

  test('$MethodChannelExampleSuperapp is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelExampleSuperapp>());
  });

  test('getPlatformVersion', () async {
    ExampleSuperApp exampleSuperappPlugin = ExampleSuperApp();
    MockExampleSuperappPlatform fakePlatform = MockExampleSuperappPlatform();
    ExampleSuperappPlatform.instance = fakePlatform;
    // expect(await exampleSuperappPlugin.getPlatformVersion(), '42');
  });
}
