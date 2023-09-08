import { NativeModules, Platform } from 'react-native';

const { SuperAppApi } = NativeModules;

class SuperAppAPI {
  
    static payNoUi(amount: number): Promise<string> {
        if (Platform.OS === 'android') {
          return SuperAppApi.payNoUi(amount);
        } else {
          return Promise.reject('payNoUi(amount: number) has not been implemented.');
        }
      }
    
      static payWithUi(amount: number): Promise<string> {
        if (Platform.OS === 'android') {
          return SuperAppApi.payWithUi(amount);
        } else {
          return Promise.reject('payWithUi(amount: number) has not been implemented.');
        }
      }
    
      static getClientInformation(clientId: string): Promise<Record<string, string>> {
        if (Platform.OS === 'android') {
          return SuperAppApi.getClientInformation(clientId);
        } else {
          return Promise.reject('getClientInformation(clientId: string) has not been implemented.');
        }
      }
    
      static getPaymentInformation(client: Record<string, string>): Promise<Record<string, string>[]> {
        if (Platform.OS === 'android') {
          return SuperAppApi.getPaymentInformation(client);
        } else {
          return Promise.reject('getPaymentInformation(client: Record<string, string>) has not been implemented.');
        }
      }
    
      static getPaymentInfoAffinity(paymentInformation: Record<string, string>): Promise<string> {
        if (Platform.OS === 'android') {
          return SuperAppApi.getPaymentInfoAffinity(paymentInformation);
        } else {
          return Promise.reject('getPaymentInfoAffinity(paymentInformation: Record<string, string>) has not been implemented.');
        }
      }

  
}

export default SuperAppAPI;
