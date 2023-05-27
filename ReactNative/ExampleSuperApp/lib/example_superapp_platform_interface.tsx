import MiniApp from "./model/miniapp";
import MethodChannelExampleSuperapp from './example_superapp_platform_method_channel'
import PlatformInterface from './platform_interface'

abstract class ExampleSuperappPlatform extends PlatformInterface {
  private static _token: Object = {};
  private static _instance: ExampleSuperappPlatform = new MethodChannelExampleSuperapp();

  constructor() {
    super(ExampleSuperappPlatform._token);
  }

  static get instance(): ExampleSuperappPlatform {
    return ExampleSuperappPlatform._instance;
  }

  static set instance(instance: ExampleSuperappPlatform) {
    PlatformInterface.verifyToken(instance, ExampleSuperappPlatform._token);
    ExampleSuperappPlatform._instance = instance;
  }

  getMiniApps(tag: string): void {
    throw new Error(`getMiniApps(${tag}) has not been implemented.`);
  }

  getCachedMiniApps(): void {
    throw new Error('getCachedMiniApps() has not been implemented.');
  }

  loadMiniApp(miniApp: MiniApp): void {
    throw new Error(`loadMiniApp(${miniApp.description}) has not been implemented.`);
  }

  remove(miniApp: MiniApp): void {
    throw new Error(`remove(${miniApp.description}) has not been implemented.`);
  }
}

export default ExampleSuperappPlatform;
