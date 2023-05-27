import ExampleSuperappPlatform from './example_superapp_platform_interface';
import MiniApp from './model/miniapp';

class ExampleSuperApp {
  static getMiniApps(tag: string) {
    console.log("getMiniApps");
    return ExampleSuperappPlatform.instance.getMiniApps(tag);
  }

  static getCachedMiniApps() {
    console.log("getCachedMiniApps");
    return ExampleSuperappPlatform.instance.getCachedMiniApps();
  }

  static loadMiniApp(miniApp: MiniApp) {
    console.log("loadMiniApp");
    return ExampleSuperappPlatform.instance.loadMiniApp(miniApp);
  }

  static remove(miniApp: MiniApp) {
    console.log("remove");
    return ExampleSuperappPlatform.instance.remove(miniApp);
  }
}

export default ExampleSuperApp;