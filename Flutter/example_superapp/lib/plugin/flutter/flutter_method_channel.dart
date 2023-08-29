import 'package:example_superapp/plugin/flutter/flutter_method_handler_interface.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class FlutterMethodChannel {
  static const channelName = 'example_superapp';

  static const methodPayNoUi = "PayWithoutUI";
  static const methodPayWithUi = "PayWithUI";
  static const methodGetClientInformation = "GetClientInformation";
  static const methodGetPaymentInformation = "GetPaymentInformation";
  static const methodGetPaymentInfoAffinity = "GetPaymentInfoAffinity";

  late MethodChannel channel;
  late BuildContext context;
  SuperAppApiInterface api = SuperAppApiInterface.instance;

  static final FlutterMethodChannel instance = FlutterMethodChannel._init();
  FlutterMethodChannel._init();

  void configureChannel(BuildContext buildContext) {
    context = buildContext;
    channel = const MethodChannel(channelName);
    channel.setMethodCallHandler(_methodHandler);
  }

  Future<dynamic> _methodHandler(MethodCall call) async {
    switch (call.method) {
      case methodPayNoUi:
        double amount = call.arguments["amount"];
        String paymentId = api.payNoUi(amount);
        return Future.value(paymentId);
      case methodPayWithUi:
        double amount = call.arguments["amount"];
        Future<String> paymentId = api.payWithUi(amount, context);
        return paymentId;
      case methodGetClientInformation:
        String clientId = call.arguments["clientId"];
        Map<String, String> result = api.getClientInformation(clientId);
        return Future.value(result);
      case methodGetPaymentInformation:
        Map<dynamic, dynamic> clientInfo = call.arguments["clientInfo"];
        List<Map<String, String>> result = api.getPaymentInformation(clientInfo.cast());
        return Future.value(result);
      case methodGetPaymentInfoAffinity:
        Map<dynamic, dynamic> paymentInfo = call.arguments["paymentInfo"];
        String result = api.getPaymentInfoAffinity(paymentInfo.cast());
        return Future.value(result);
      default:
        return Future.error("No such method '${call.method}'");
    }
  }
}
