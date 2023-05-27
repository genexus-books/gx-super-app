import { NativeModules } from 'react-native';
import MiniApp from './model/miniapp';
import ExampleSuperappPlatform from './example_superapp_platform_interface';

const { ExampleSuperapp } = NativeModules;

class MethodChannelExampleSuperapp extends ExampleSuperappPlatform {
  
  async getMiniApps(tag: string): Promise<MiniApp[] | null> {
    try {
      const miniAppsJson = await ExampleSuperapp.getMiniApps(tag);
      return this.fromJson(miniAppsJson);
    } catch (error) {
      console.error('Error getting mini apps:', error);
      return null;
    }
  }

  async getCachedMiniApps(): Promise<MiniApp[] | null> {
    try {
      const miniAppsJson = await ExampleSuperapp.getCachedMiniApps();
      return this.fromJson(miniAppsJson);
    } catch (error) {
      console.error('Error getting cached mini apps:', error);
      return null;
    }
  }

  private fromJson(miniAppsJson: string | null): MiniApp[] | null {
    if (!miniAppsJson || miniAppsJson.length === 0) {
      return null;
    }

    const miniApps = JSON.parse(miniAppsJson).map((e: any) => MiniApp.fromJson(e));
    return miniApps;
  }

  async loadMiniApp(miniApp: MiniApp): Promise<boolean | undefined> {
    try {
      const miniAppJson = JSON.stringify(miniApp.toJson());
      const result = await ExampleSuperapp.loadMiniApp(miniAppJson);
      return result;
    } catch (error) {
      console.error('Error loading mini app:', error);
      return undefined;
    }
  }

  async remove(miniApp: MiniApp): Promise<boolean | undefined> {
    try {
      const miniAppJson = JSON.stringify(miniApp.toJson());
      const result = await ExampleSuperapp.remove(miniAppJson);
      return result;
    } catch (error) {
      console.error('Error removing mini app:', error);
      return undefined;
    }
  }
}

export default MethodChannelExampleSuperapp;