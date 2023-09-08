package com.genexus.example_superapp

import io.flutter.embedding.engine.plugins.FlutterPlugin

interface IFlutterCallHandlerSetup {
	fun setupChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding)
	fun destroyChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding)
}
