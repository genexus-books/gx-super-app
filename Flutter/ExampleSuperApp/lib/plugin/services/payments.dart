import 'package:example_superapp/plugin/services/client.dart';

class PaymentsService {
  static const sdtPaymentInformation = "PaymentInformation";
  static const fieldBrand = "brand";
  static const fieldAffinity = "affinity";
  static const fieldType = "type";

  static String pay(int amount) {
    return "paymentIdForAmount$amount";
  }

  static List<Map<String, String>> getPaymentInformation(Map<String, String> clientInformation) {
    List<Map<String, String>> list = [];
    String clientId = clientInformation[ClientsService.fieldId] ?? "";
    for (var i = 1; i <= 3; i++) {
      var map = {
        fieldBrand: "Brand $i for $clientId",
        fieldAffinity: "Affinity $i for $clientId",
        fieldType: "Type $i for $clientId"
      };
      list.add(map);
    }

    return list;
  }

  static String getPaymentInformationAffinity(Map<String, String> paymentInformation) {
    return paymentInformation[fieldAffinity] ?? "";
  }
}
