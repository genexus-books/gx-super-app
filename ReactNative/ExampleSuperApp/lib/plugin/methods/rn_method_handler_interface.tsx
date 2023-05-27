import { Platform } from 'react-native';
import SuperAppAPI from './rn_method_handler';

class SuperAppApiInterface  {
  //For extends PlatformInterface replacement
  //constructor() {
    //super();
  //}

  static _token = {};
  private static _instance: SuperAppApiInterface = new SuperAppApiInterface();


  static get instance() {
    return SuperAppApiInterface._instance;
  }

  
  static set instance(instance) {
    
    //TODO  verify token in react native??
    //PlatformInterface.verifyToken(instance, SuperAppApiInterface._token);
    SuperAppApiInterface._instance = instance;
  }

  payNoUi(amount: number) {
    throw new Error('payNoUi(amount: number) has not been implemented.');
  }

  async payWithUi(amount: number, context: any) {
    throw new Error('payWithUi(amount: number) has not been implemented.');
  }

  getClientInformation(clientId: string) {
    throw new Error('getClientInformation(clientId: string) has not been implemented.');
  }

  getPaymentInformation(client: any) {
    throw new Error('getPaymentInformation(client: any) has not been implemented.');
  }

  getPaymentInfoAffinity(paymentInformation: any) {
    throw new Error('getPaymentInfoAffinity(paymentInformation: any) has not been implemented.');
  }
}

export default SuperAppApiInterface;