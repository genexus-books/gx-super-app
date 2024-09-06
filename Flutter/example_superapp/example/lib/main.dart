import 'package:example_superapp_example/ui/main_screen.dart';
import 'package:example_superapp/plugin/ui/payment_confirm.dart';
import 'package:flutter/material.dart';

void main() => runApp(const MaterialApp(home: MainScreen()));

@pragma("vm:entry-point")
void showPayWithUI() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(PaymentApp());
}

class PaymentApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: PaymentConfirm(),
    );
  }
}