import { Platform } from 'react-native';
import SuperAppApiInterface from './rn_method_handler_interface';
import ClientsService from '../services/client';
import PaymentsService from '../services/payments';
import SelectionScreen from '../ui/selection_screen';

class SuperAppAPI extends SuperAppApiInterface {
  payNoUi(amount: number) {
    return PaymentsService.pay(amount);
  }

  async payWithUi(amount: number, context: any) {
    return await context.push(
      Platform.OS === 'ios'
        ? SelectionScreen
        : <SelectionScreen totalAmount={amount} navigation={undefined} />
    );
  }

  getClientInformation(clientId: string) {
    return ClientsService.getClient(clientId);
  }

  getPaymentInformation(client: any) {
    return PaymentsService.getPaymentInformation(client);
  }

  getPaymentInfoAffinity(paymentInformation: any) {
    return PaymentsService.getPaymentInformationAffinity(paymentInformation);
  }
}

export default SuperAppAPI;