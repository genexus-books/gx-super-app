import 'package:flutter/cupertino.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_method_handler.dart';

abstract class SuperAppApiInterface extends PlatformInterface {

  SuperAppApiInterface() : super(token: _token);

  static final Object _token = Object();
  static SuperAppApiInterface _instance = SuperAppAPI();
  static SuperAppApiInterface get instance => _instance;

  static set instance(SuperAppApiInterface instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  String payNoUi(double amount) {
    throw UnimplementedError('payNoUi(amount: int) has not been implemented.');
  }

  Future<String> payWithUi(double amount, BuildContext context) {
    throw UnimplementedError('payWithUi(amount: int) has not been implemented.');
  }

  Map<String, String> getClientInformation(String clientId) {
    throw UnimplementedError('getClientInformation(clientId: String) has not been implemented.');
  }

  List<Map<String, String>> getPaymentInformation(Map<String, String> client) {
    throw UnimplementedError('getPaymentInformation(client: Map) has not been implemented.');
  }

  String getPaymentInfoAffinity(Map<String, String> paymentInformation) {
    throw UnimplementedError('getPaymentInfoAffinity(paymentInformation: Map) has not been implemented.');
  }
}
