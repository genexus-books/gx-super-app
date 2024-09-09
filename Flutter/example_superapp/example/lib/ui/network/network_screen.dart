import 'package:example_superapp/plugin/native/invoker/example_superapp.dart';
import 'package:example_superapp/model/miniapp.dart';
import 'package:example_superapp_example/ui/miniapps_skeleton.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../constants.dart';

class NetworkMiniAppsList extends StatefulWidget {

  static const String label = "Network";
  static const String routeName = "/networkList";
  static const IconData icon = Icons.list_alt;

  const NetworkMiniAppsList({super.key});

  @override
  State<NetworkMiniAppsList> createState() => _NetworkListState();
}

class _NetworkListState extends State<NetworkMiniAppsList> {

  final ExampleSuperApp _plugin = ExampleSuperApp();
  List<MiniApp> _miniApps = <MiniApp>[];
  bool _isLoading = true;

  @override
  Widget build(BuildContext context) {
    return _isLoading ?
    ListView.separated(
      padding: const EdgeInsets.only(top: 5),
      itemCount: 5,
      itemBuilder: (context, index) => const MiniAppsSkeleton(),
      separatorBuilder: (context, index) =>
      const SizedBox(height: defaultPadding),
    )
        : ListView.builder(
        padding: const EdgeInsets.only(top: 5),
        itemCount: _miniApps.length,
        itemBuilder: (context, index) {
          return Card(
              child: ListTile(
                onTap: () {
                  _plugin.loadMiniApp(_miniApps[index]);
                },
                title: Text(_miniApps[index].name),
                subtitle: Text(_miniApps[index].description),
                leading: CircleAvatar(
                    backgroundImage: NetworkImage(_miniApps[index].iconUrl)
                ),
                // trailing: Icon(icons[index])
              )
          );
        });
  }

  @override
  void initState() {
    initMiniAppsList();
    super.initState();
  }

  Future<void> initMiniAppsList() async {
    List<MiniApp>? newMiniApps;
    try {
      newMiniApps = await _plugin.getMiniApps("");
    } on PlatformException {
      newMiniApps = null;
    }

    if (!mounted) {
      return;
    }

    setState(() {
      _isLoading = false;
      if (newMiniApps != null) {
        _miniApps = newMiniApps;
      }
    });
  }
}
