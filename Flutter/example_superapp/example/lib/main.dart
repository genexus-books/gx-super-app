import 'package:example_superapp_example/ui/main_screen.dart';
import 'package:flutter/material.dart';

void main() => runApp(const MaterialApp(home: MainScreen()));

// void main() => runApp(
//     MaterialApp(
//       initialRoute: '/',
//       routes: {
//         '/': (context) => const MainScreen(),
//         NetworkMiniAppsList.routeName: (context) => const NetworkMiniAppsList(),
//         CachedMiniAppsList.routeName: (context) => const CachedMiniAppsList(),
//       },
//     )
// );