import 'package:example_superapp/plugin/flutter/flutter_method_handler_interface.dart';
import 'package:example_superapp/plugin/services/client.dart';
import 'package:example_superapp/plugin/services/payments.dart';
import 'package:flutter/material.dart';

import '../ui/selection_screen.dart';

class SuperAppAPI implements SuperAppApiInterface {
  @override
  String payNoUi(double amount) {
    return PaymentsService.pay(amount);
  }

  @override
  Future<String> payWithUi(double amount, BuildContext context) async {
    return await Navigator.of(context).push(MaterialPageRoute(
        builder: (context) => SelectionScreen(totalAmount: amount)));
  }

  @override
  Map<String, String> getClientInformation(String clientId) {
    return ClientsService.getClient(clientId);
  }

  @override
  List<Map<String, String>> getPaymentInformation(Map<String, String> client) {
    return PaymentsService.getPaymentInformation(client);
  }

  @override
  String getPaymentInfoAffinity(Map<String, String> paymentInformation) {
    return PaymentsService.getPaymentInformationAffinity(paymentInformation);
  }

  @override
  String getSessionInformation() {
    return '''
      {"type": "JWT",
      "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.
      eyJjbGllbnRfaWQiOiJZekV6TUdkb01ISm5PSEJpT0cxaWJEaHlOVEE9IiwicmVzcG9uc2Vf
      dHlwZSI6ImNvZGUiLCJzY29wZSI6ImludHJvc2NwZWN0X3Rva2VucywgcmV2b2tlX3Rva2Vu
      cyIsImlzcyI6ImJqaElSak0xY1hwYWEyMXpkV3RJU25wNmVqbE1iazQ0YlRsTlpqazNkWEU9
      Iiwic3ViIjoiWXpFek1HZG9NSEpuT0hCaU9HMWliRGh5TlRBPSIsImF1ZCI6Imh0dHBzOi8v
      bG9jYWxob3N0Ojg0NDMve3RpZH0ve2FpZH0vb2F1dGgyL2F1dGhvcml6ZSIsImp0aSI6IjE1
      MTYyMzkwMjIiLCJleHAiOiIyMDIxLTA1LTE3VDA3OjA5OjQ4LjAwMCswNTQ1In0.
      IxvaN4ER-PlPgLYzfRhk_JiY4VAow3GNjaK5rYCINFsEPa7VaYnRsaCmQVq8CTgddihEPPXe
      t2laH8_c3WqxY4AeZO5eljwSCobCHzxYdOoFKbpNXIm7dqHg_5xpQz-YBJMiDM1ILOEsER8A
      DyF4NC2sN0K_0t6xZLSAQIRrHvpGOrtYr5E-SllTWHWPmqCkX2BUZxoYNK2FWgQZpuUOD55H
      fsvFXNVQa_5TFRDibi9LsT7Sd_az0iGB0TfAb0v3ZR0qnmgyp5pTeIeU5UqhtbgU9RnUCVmG
      IK-SZYNvrlXgv9hiKAZGhLgeI8hO40utfT2YTYHgD2Aiufqo3RIbJA"}
      ''';
  }
}
