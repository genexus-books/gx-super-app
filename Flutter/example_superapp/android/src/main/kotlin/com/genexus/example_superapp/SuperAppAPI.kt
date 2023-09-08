package com.genexus.example_superapp

import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.services.Services
import com.genexus.example_superapp.api.PaymentsApi
import com.genexus.example_superapp.api.services.ClientsService
import com.genexus.example_superapp.api.services.PaymentsService
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

object SuperAppAPI: ISuperApp, IFlutterCallHandlerSetup {

	private const val CHANNEL_NAME = "example_superapp"
	private lateinit var methodChannel: MethodChannel

	override fun setupChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding) {
		methodChannel = MethodChannel(flutterBinding.binaryMessenger, CHANNEL_NAME).apply {
			setMethodCallHandler(FlutterCallHandler())
		}
	}

	override fun destroyChannel(flutterBinding: FlutterPlugin.FlutterPluginBinding) {
		methodChannel.setMethodCallHandler(null)
	}

	private fun callMethod(name: String, arguments: Map<String, Any>?, resultHandler: MethodChannel.Result? = null) {
		Services.Device.runOnUiThread { // Flutter method invocation must run on Ui thread
			methodChannel.invokeMethod(name, arguments, resultHandler)
		}
	}

	override fun payWithoutUI(amount: Double, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_PAY_NO_UI, hashMapOf("amount" to amount), resultHandler)
	}

	override fun payWithUI(amount: Double, resultHandler: MethodChannel.Result?) {
		callMethod(PaymentsApi.METHOD_PAY_UI, hashMapOf("amount" to amount), resultHandler)
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
