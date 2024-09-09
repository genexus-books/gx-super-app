package com.genexus.example_superapp

import android.app.Activity
import android.content.Intent
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.example_superapp.api.FlutterPaymentActivity
import com.genexus.example_superapp.api.PaymentsApi
import com.genexus.example_superapp.api.services.ClientsService
import com.genexus.example_superapp.api.services.PaymentsService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

object SuperAppAPI: ISuperApp, IFlutterCallHandlerSetup {

	const val REQUEST_CODE_PAYMENT = 2552
	private const val CHANNEL_NON_UI_METHODS = "com.genexus.superapp/SuperAppAPI"
	private const val CHANNEL_PROVISIONING = "com.genexus.superapp/Provisioning"
	private lateinit var nonUIMethodsChannel: MethodChannel
	private lateinit var methodChannelProvisioning: MethodChannel

	override fun setupChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding) {
		nonUIMethodsChannel = MethodChannel(flutterBinding.binaryMessenger, CHANNEL_NON_UI_METHODS)
		methodChannelProvisioning = MethodChannel(flutterBinding.binaryMessenger, CHANNEL_PROVISIONING).apply {
			setMethodCallHandler(FlutterCallHandler())
		}
	}

	override fun destroyChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding) {
		methodChannelProvisioning.setMethodCallHandler(null)
	}

	private fun callMethod(name: String, arguments: Map<String, Any>?, resultHandler: MethodChannel.Result? = null) {
		Services.Device.runOnUiThread { // Flutter method invocation must run on Ui thread
			nonUIMethodsChannel.invokeMethod(name, arguments, resultHandler)
		}
	}

	private fun callUIMethod(route: Class<out Activity>, arguments: Map<String, Any>?, from: Activity) {
		val intent = Intent(from.baseContext, route).apply {
			if (arguments != null)
				for (arg in arguments.entries)
					putExtra(arg.key, arg.value.toString())
		}

		from.startActivityForResult(intent, REQUEST_CODE_PAYMENT)

//		Services.Device.runOnUiThread {
//			val intent = FlutterActivity
//				.withCachedEngine(FlutterEngineManager.ENGINE_PAYMENT_SCREEN)
//				.build(from)
//
//			from.startActivity(intent)
//		}
	}

	override fun payWithoutUI(amount: Double, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_PAY_NO_UI, hashMapOf("amount" to amount), resultHandler)
	}

	override fun payWithUI(amount: Double, from: Activity) {
		callUIMethod(FlutterPaymentActivity::class.java, hashMapOf("amount" to amount), from)
	}

	override fun getClientInformation(clientId: String, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_GET_CLIENT_INFO, hashMapOf("clientId" to clientId), resultHandler)
	}

	override fun getPaymentInformation(clientInformation: Entity, resultHandler: MethodChannel.Result?) {
		val map = ClientsService.toMap(clientInformation)
		getPaymentInformation(map, resultHandler)
	}

	override fun getPaymentInformation(clientInformation: HashMap<String, String>, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_PAYMENT_INFORMATION, hashMapOf("clientInfo" to clientInformation), resultHandler)
	}

	override fun getPaymentInfoAffinity(paymentInformation: Entity, resultHandler: MethodChannel.Result?) {
		val map = PaymentsService.toMap(paymentInformation)
		getPaymentInfoAffinity(map, resultHandler)
	}

	override fun getPaymentInfoAffinity(paymentInformation: HashMap<String, String>, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_GET_PAYMENT_AFFINITY, mapOf("paymentInfo" to paymentInformation), resultHandler)
	}
}
