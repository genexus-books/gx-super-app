import 'package:example_superapp/model/miniapp.dart';
import 'example_superapp_platform_interface.dart';

class ExampleSuperApp {
  Future<List<MiniApp>?> getMiniApps(String tag) {
    return ExampleSuperappPlatform.instance.getMiniApps(tag);
  }

  Future<List<MiniApp>?> getCachedMiniApps() {
    return ExampleSuperappPlatform.instance.getCachedMiniApps();
  }

  Future<bool?> loadMiniApp(MiniApp miniApp) {
    return ExampleSuperappPlatform.instance.loadMiniApp(miniApp);
  }

  Future<bool?> remove(MiniApp miniApp) {
    return ExampleSuperappPlatform.instance.remove(miniApp);
  }
}
