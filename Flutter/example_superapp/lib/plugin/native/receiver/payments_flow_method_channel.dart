import 'dart:developer';

import 'package:flutter/services.dart';

class PaymentsFlowChannel {

  static const MethodChannel channelPaymentsFlow = MethodChannel('com.genexus.superapp/paymentConfirm');

  static Future<bool> closeActivity() async {
    bool? isOk;
    try {
      isOk = await channelPaymentsFlow.invokeMethod<bool>('closeFlutterActivity');
    } on PlatformException catch (e) {
      log(e.message!);
      isOk = false;
    }
    return isOk ?? false;
  }

  static Future<bool> confirmActivity({String? data}) async {
    bool? isOk;
    try {
      isOk = await channelPaymentsFlow.invokeMethod<bool>('confirmFlutterActivity', { "data" : data });
    } on PlatformException catch (e) {
      log(e.message!);
      isOk = false;
    }
    return isOk ?? false;
  }
}
