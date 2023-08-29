package com.genexus.example_superapp

import com.genexus.android.core.base.model.Entity
import io.flutter.plugin.common.MethodChannel

interface ISuperApp {
	fun payWithoutUI(amount: Double, resultHandler: MethodChannel.Result? = null)
	fun payWithUI(amount: Double, resultHandler: MethodChannel.Result? = null)
	fun getClientInformation(clientId: String, resultHandler: MethodChannel.Result? = null)
	fun getPaymentInformation(clientInformation: Entity, resultHandler: MethodChannel.Result? = null)
	fun getPaymentInformation(clientInformation: HashMap<String, String>, resultHandler: MethodChannel.Result? = null)
	fun getPaymentInfoAffinity(paymentInformation: Entity, resultHandler: MethodChannel.Result? = null)
	fun getPaymentInfoAffinity(paymentInformation: HashMap<String, String>, resultHandler: MethodChannel.Result? = null)
}
