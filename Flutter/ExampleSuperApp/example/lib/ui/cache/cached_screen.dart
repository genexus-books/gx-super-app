import 'package:example_superapp/example_superapp.dart';
import 'package:example_superapp/model/miniapp.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CachedMiniAppsList extends StatefulWidget {

  static const String label = "Saved";
  static const String routeName = "/cachedList";
  static const IconData icon = Icons.favorite;

  const CachedMiniAppsList({super.key});

  @override
  State<CachedMiniAppsList> createState() => _CachedListState();
}

class _CachedListState extends State<CachedMiniAppsList> {

  final ExampleSuperApp _plugin = ExampleSuperApp();
  List<MiniApp> _miniApps = <MiniApp>[];

  @override
  Widget build(BuildContext context) {
    return _miniApps.isEmpty ?
    const Center(child: Text("You haven't downloaded any MiniApps yet!")) :
    ListView.builder(
        itemCount: _miniApps.length,
        itemBuilder: (context, index) {
          final miniApp = _miniApps[index];
          return Dismissible(
              key: Key(miniApp.id),
              onDismissed: (direction) {
                setState(() {
                  var miniApp = _miniApps[index];
                  _plugin.remove(miniApp);
                  _miniApps.removeAt(index);
                });

                ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(
                        content: Text('${miniApp.name} removed from cache')
                    )
                );
              },
              background: Container(color: Colors.red),
              child: Card(
                  child: ListTile(
                      onTap: () {
                        _plugin.loadMiniApp(miniApp);
                      },
                      title: Text(miniApp.name),
                      subtitle: Text("${miniApp.id}:${miniApp.version}"),
                      leading: CircleAvatar(
                          child: hasValue(_miniApps[index].iconUrl) ?
                          Image.network(_miniApps[index].iconUrl) :
                          const Icon(Icons.info)
                      )
                  )
              )
          );
        });
  }

  bool hasValue(String? value) {
    return value != null && value.isNotEmpty;
  }

  @override
  void initState() {
    initMiniAppsList();
    super.initState();
  }

  Future<void> initMiniAppsList() async {
    List<MiniApp>? newMiniApps;
    try {
      newMiniApps = await _plugin.getCachedMiniApps();
    } on PlatformException {
      newMiniApps = null;
    }

    if (!mounted || newMiniApps == null) {
      return;
    }

    setState(() {
      _miniApps = newMiniApps!;
    });
  }
}