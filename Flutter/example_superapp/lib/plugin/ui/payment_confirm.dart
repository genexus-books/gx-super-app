import 'package:example_superapp/plugin/native/receiver/payments_flow_method_channel.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PaymentConfirm extends StatefulWidget {

  @override
  State<PaymentConfirm> createState() => _PaymentConfirmState();
}

class _PaymentConfirmState extends State<PaymentConfirm> {
  String? _data;

  @override
  void initState() {
    super.initState();
    PaymentsFlowChannel.channelPaymentsFlow.setMethodCallHandler((call) async {
      if (call.method == "init") {
        String? newData = call.arguments["data"];
        setState(() {
          _data = newData;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Confirm Payment Screen'),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () {
            // Send a message to the Kotlin side to close this Flutter activity
            PaymentsFlowChannel.closeActivity();
            if (Navigator.canPop(context)) {
              Navigator.pop(context);
            } else {
              SystemNavigator.pop();
            }
          },
        ),
      ),
      body: (_data == null)
          ? const SizedBox.shrink()
          : Column(
              children: [
                Center(
                  child: Text('Data: $_data'),
                ),
                FilledButton(
                    onPressed: () {
                      PaymentsFlowChannel.confirmActivity(data: "OK $_data");
                      if (Navigator.canPop(context)) {
                        Navigator.pop(context);
                      } else {
                        SystemNavigator.pop();
                      }
                    },
                    child: const Text("Confirm")),
              ],
            ),
    );
  }
}
