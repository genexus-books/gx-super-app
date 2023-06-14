import MiniApp from './model/miniapp';
import { NativeModules } from 'react-native';

const {ProvisioningAPI} = NativeModules;



class ExampleSuperapp {
  async getMiniApps(tag: string): Promise<MiniApp[] | null> {
    try {
      const miniAppsJson = await new Promise((resolve, reject) => {
        ProvisioningAPI.handleMethodCall({ method: 'getMiniApps', arguments: { tag } }, (response: any) => {
          console.log('getMiniApps Response:', response);
          resolve(response); // Resolve the promise with the response
        });
      });
      
      return this.fromJson(miniAppsJson);
    } catch (error) {
      console.error('Error getting mini apps:', error);
      return null;
    }
  }

  async getCachedMiniApps(): Promise<MiniApp[] | null> {
    try {
      const miniAppsJson = await new Promise((resolve, reject) => {
        ProvisioningAPI.handleMethodCall({ method: 'getCachedMiniApps' }, (response: any) => {
          console.log('getCachedMiniApps Response:', response);
          resolve(response); // Resolve the promise with the response
        });
      });
      
      return this.fromJson(miniAppsJson);
    
    } catch (error) {
      console.error('Error getting cached mini apps:', error);
      return null;
    }
  }

  fromJson(miniAppsJson: any | null): MiniApp[] | null {
    if (!miniAppsJson || miniAppsJson.length === 0) {
      return null;
    }
  
    const miniApps = JSON.parse(miniAppsJson as string).map((json: any) => new MiniApp(json));
    return miniApps;
  }

  async loadMiniApp(miniApp: MiniApp): Promise<boolean | null> {
    try {
      
      const miniAppJson = JSON.stringify(miniApp.toJson());
      return await new Promise((resolve, reject) => {
        ProvisioningAPI.handleMethodCall({ method: 'load', arguments: { miniAppJson } }, (response: any) => {
          console.log('loadMiniApp Response:', response);
          resolve(response); // Resolve the promise with the response
        });});
    } catch (error) {
      console.error('Error loading mini app:', error);
      return null;
    }
  }

  async remove(miniApp: MiniApp): Promise<boolean | null> {
    try {
      const miniAppJson = JSON.stringify(miniApp.toJson());
      return await ProvisioningAPI.remove(miniAppJson);
    } catch (error) {
      console.error('Error removing mini app:', error);
      return null;
    }
  }
}

export default ExampleSuperapp;
