package com.genexus.example_superapp

import com.genexus.android.core.superapps.MiniApp
import io.flutter.plugin.common.MethodChannel

interface IProvisioning {
	fun getMiniApps(tag: String, result: MethodChannel.Result)
	fun getCachedMiniApps(result: MethodChannel.Result)
	fun load(miniApp: MiniApp, result: MethodChannel.Result)
	fun remove(id: String, version: Int, result: MethodChannel.Result)
}
