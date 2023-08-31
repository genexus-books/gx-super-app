package com.genexus.example_superapp

import android.app.Application
import com.genexus.android.core.application.ApplicationHelper
import com.genexus.android.core.base.services.IEntityProvider
import com.genexus.android.core.base.services.LogLevel
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.providers.EntityDataProvider
import com.genexus.android.core.services.EntityService
import com.genexus.example_superapp.api.PaymentsModule
import com.genexus.example_superapp.application.AppEntityDataProvider
import com.genexus.example_superapp.application.AppEntityService
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.plugins.FlutterPlugin

class ExampleSuperappPlugin: FlutterPlugin {

	private var application: ApplicationHelper? = null

	override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
		val context = flutterPluginBinding.applicationContext as Application
		initApplication(context)
		SuperAppAPI.setupChannel(flutterPluginBinding)
//		FlutterEngine(context).apply {
//			navigationChannel.setInitialRoute("/payments")
//			dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
//			FlutterEngineCache.getInstance().put("plugin_engine", this)
//		}
	}

	override fun onDetachedFromEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
		SuperAppAPI.destroyChannel(flutterPluginBinding)
	}

	private fun initApplication(context: Application) {
		application = ApplicationHelper(context, entityProvider)
		application?.apply {
			//These modules are provided by GeneXus and required by the MiniApps in order to work properly
			registerModule(com.genexus.android.core.externalobjects.CoreExternalObjectsModule())
			registerModule(com.genexus.android.core.usercontrols.CoreUserControlsModule())
			registerModule(com.genexus.android.controls.grids.smart.SmartGridModule())
			registerModule(com.genexus.android.superapps.SuperAppsModule())

			//This is a custom module provided by the native SuperApp, exposing its public API to the MiniApps
			registerModule(PaymentsModule())

			onCreate()

			//Turn MiniApps logging on
			Services.Log.setLevel(LogLevel.DEBUG)
		}
	}

	private val entityProvider = object : IEntityProvider {
		override fun getEntityServiceClass(): Class<out EntityService> {
			return AppEntityService::class.java
		}

		override fun getProvider(): EntityDataProvider {
			return AppEntityDataProvider()
		}
	}
}
