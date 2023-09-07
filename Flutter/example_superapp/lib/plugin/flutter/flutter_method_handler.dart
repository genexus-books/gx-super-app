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
    return await Navigator.of(context).push(
        MaterialPageRoute(builder: (context) => SelectionScreen(totalAmount: amount))
    );
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
}
