import 'package:example_superapp/plugin/flutter/flutter_method_channel.dart';
import 'package:flutter/material.dart';

import '../routes.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  State<StatefulWidget> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  final List<Widget> _pages = { for (var v in tabItems) v.widget }.toList();
  int _selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    FlutterMethodChannel.instance.configureChannel(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Example SuperApp'),
      ),
      body: IndexedStack(
        index: _selectedIndex,
        children: _pages,
      ),
      bottomNavigationBar: BottomNavigationBar(
          selectedFontSize: 15,
          selectedIconTheme: const IconThemeData(color: Colors.blueAccent, size: 30),
          selectedItemColor: Colors.blueAccent,
          selectedLabelStyle: const TextStyle(fontWeight: FontWeight.bold),
          items: getBottomTabs(tabItems),
          currentIndex: _selectedIndex,
          onTap: _onItemTapped
      ),
    );
  }

  List<BottomNavigationBarItem> getBottomTabs(List<TabItem> tabs) {
    return tabs.map(
          (item) =>
          BottomNavigationBarItem(
            icon: Icon(item.icon),
            label: item.label,
          ),
    ).toList();
  }

  void _onItemTapped(int index) {
    setState(() {
      // Navigator.of(context).pushNamed(tabItems[index].route);
      _selectedIndex = index;
    });
  }
}