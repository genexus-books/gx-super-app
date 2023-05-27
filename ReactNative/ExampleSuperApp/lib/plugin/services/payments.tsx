import ClientsService from "./client";

class PaymentsService {
    static sdtPaymentInformation = "PaymentInformation";
    static fieldBrand = "brand";
    static fieldAffinity = "affinity";
    static fieldType = "type";
  
    static pay(amount: number) {
      return `paymentIdForAmount${amount}`;
    }
  
    static getPaymentInformation(clientInformation: any) {
      const list = [];
      const clientId = clientInformation[ClientsService.fieldId] || "";
      for (let i = 1; i <= 3; i++) {
        const map = {
          [PaymentsService.fieldBrand]: `Brand ${i} for ${clientId}`,
          [PaymentsService.fieldAffinity]: `Affinity ${i} for ${clientId}`,
          [PaymentsService.fieldType]: `Type ${i} for ${clientId}`,
        };
        list.push(map);
      }
  
      return list;
    }
  
    static getPaymentInformationAffinity(paymentInformation: any) {
      return paymentInformation[PaymentsService.fieldAffinity] || "";
    }
  }
  
  export default PaymentsService;