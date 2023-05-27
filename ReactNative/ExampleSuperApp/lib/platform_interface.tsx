export default class PlatformInterface {
    private token: Object;
  
    constructor(token: Object) {
      this.token = token;
    }
  
    static verifyToken(instance: any, token: Object) {
      if (instance.token !== token) {
        throw new Error('Invalid platform interface token.');
      }
    }
  }