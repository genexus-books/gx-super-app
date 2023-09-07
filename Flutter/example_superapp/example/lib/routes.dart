import 'package:example_superapp_example/ui/cache/cached_screen.dart';
import 'package:example_superapp_example/ui/network/network_screen.dart';
import 'package:flutter/cupertino.dart';

class TabItem {
  String label;
  String route;
  IconData icon;
  Widget widget;
  TabItem(this.label, this.route, this.icon, this.widget);
}

final List<TabItem> tabItems = [
  TabItem(NetworkMiniAppsList.label, NetworkMiniAppsList.routeName, NetworkMiniAppsList.icon, const NetworkMiniAppsList()),
  TabItem(CachedMiniAppsList.label, CachedMiniAppsList.routeName, CachedMiniAppsList.icon, const CachedMiniAppsList()),
];
