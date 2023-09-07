import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:example_superapp/example_superapp_method_channel.dart';

void main() {
  MethodChannelExampleSuperapp platform = MethodChannelExampleSuperapp();
  const MethodChannel channel = MethodChannel('example_superapp');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    // expect(await platform.getPlatformVersion(), '42');
  });
}
