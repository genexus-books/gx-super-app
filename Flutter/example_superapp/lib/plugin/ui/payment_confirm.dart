import 'package:example_superapp/plugin/native/receiver/payments_flow_method_channel.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PaymentConfirm extends StatefulWidget {
  @override
  State<PaymentConfirm> createState() => _PaymentConfirmState();
}

class _PaymentConfirmState extends State<PaymentConfirm> {
  String? _data;
  String? _session;

  @override
  void initState() {
    super.initState();
    PaymentsFlowChannel.channelPaymentsFlow.setMethodCallHandler((call) async {
      if (call.method == "init") {
        _getSessionInformation();
        String? newData = call.arguments["data"];
        setState(() {
          _data = newData;
        });
      }
    });
  }

  void _getSessionInformation() async {
    _session = await PaymentsFlowChannel.getSessionInformation();
    if (_session != null) {
      print("Session: $_session!");
    }
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
                const SizedBox(
                  height: 50,
                ),
                Center(
                  child: Text('Amount to be paid: $_data'),
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
