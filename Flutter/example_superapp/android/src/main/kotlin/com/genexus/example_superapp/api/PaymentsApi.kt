package com.genexus.example_superapp.api

import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult
import com.genexus.example_superapp.SuperAppAPI
import com.genexus.example_superapp.api.services.ClientsService
import com.genexus.example_superapp.api.services.PaymentsService
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.plugin.common.MethodChannel

@Suppress("UNCHECKED_CAST")
class PaymentsApi(action: ApiAction?) : ExternalApi(action) {

	private val methodPayWithoutUI = object : IMethodInvoker {
		override fun invoke(parameters: List<Any>): ExternalApiResult {
			val amount = parameters[0].toString().toDouble()
			SuperAppAPI.payWithoutUI(amount, resultHandler)
			return ExternalApiResult.SUCCESS_WAIT
		}

		val resultHandler = object : MethodChannel.Result {
			override fun success(result: Any?) {
				val paymentId = result as String
				getAction()?.let {
					it.setOutputValue(Expression.Value.newString(paymentId))
					ActionExecution.continueCurrent(activity, true, it)
				}
			}

			override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {}
			override fun notImplemented() {}
		}
	}

	private val methodPayWithUI: IMethodInvoker = object : IMethodInvoker {
		override fun invoke(parameters: List<Any>): ExternalApiResult {
			val amount = parameters[0].toString().toDouble()

//			val intentCachedEngine = FlutterActivity
//				.withCachedEngine("plugin_engine")
//				.backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
//				.build(context)
//
//			val intentNewEngine = FlutterActivity
//				.withNewEngine()
//				.initialRoute("/payments")
//				.dartEntrypointArgs(mutableListOf(amount.toString()))
//				.backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
//				.build(context)
//
//			startActivityForResult(intentNewEngine, 1231)

			SuperAppAPI.payWithUI(amount, resultHandler)
			return ExternalApiResult.SUCCESS_WAIT
		}

		val resultHandler = object : MethodChannel.Result {
			override fun success(result: Any?) {
				val paymentId = result as String?
				if (paymentId.isNullOrEmpty()) {
//					"An error occurred processing your payment."
					ActionExecution.cancelCurrent(getAction())
					return
				}

				getAction()?.let {
					it.setOutputValue(Expression.Value.newString(paymentId))
					ActionExecution.continueCurrent(activity, true, it)
				}
			}

			override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {}
			override fun notImplemented() {}
		}
	}

	//Method that receives a String and returns a Client SDT
	private val methodGetClientInformation = object : IMethodInvoker {
		override fun invoke(parameters: List<Any>): ExternalApiResult {
			val clientId = parameters[0].toString()
			SuperAppAPI.getClientInformation(clientId, resultHandler)
			return ExternalApiResult.SUCCESS_WAIT
		}

		val resultHandler = object : MethodChannel.Result {
			override fun success(result: Any?) {
			    val clientInformation = ClientsService.toEntity(result as HashMap<String, String>)
			    getAction()?.let {
				    it.setOutputValue(Expression.Value.newSdt(clientInformation))
				    ActionExecution.continueCurrent(it.activity, true, it)
			    }
			}

			override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {}
			override fun notImplemented() {}
		}
	}

	//Method that receives a Client SDT and returns a PaymentInformation SDT (Collection)
	private val methodGetPaymentInformation = object : IMethodInvoker {
		override fun invoke(parameters: List<Any>): ExternalApiResult {
			val clientInformation = ClientsService.toMap(parameters[0] as Entity)
			SuperAppAPI.getPaymentInformation(clientInformation, resultHandler)
			return ExternalApiResult.SUCCESS_WAIT
		}

		val resultHandler = object : MethodChannel.Result {
			override fun success(result: Any?) {
				val paymentInformation = PaymentsService.toEntityList(result as List<HashMap<String, String>>)
				getAction()?.let {
					it.setOutputValue(Expression.Value.newValue(paymentInformation))
					ActionExecution.continueCurrent(it.activity, true, it)
				}
			}

			override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {}
			override fun notImplemented() {}
		}
	}

	//Method that receives a PaymentInformation SDT (Collection) and returns a String
	private val methodGetPaymentInfoAffinity = object : IMethodInvoker {
		override fun invoke(parameters: List<Any>): ExternalApiResult {
			val paymentInformationList = parameters[0] as EntityList
			if (paymentInformationList.isEmpty())
				return ExternalApiResult.FAILURE

			//Unnecessary For loop just to prove that an EntityList can be iterated over to pick any element
			var paymentInfo: Entity? = null
			for (e in paymentInformationList)
				paymentInfo = e

			SuperAppAPI.getPaymentInfoAffinity(paymentInfo!!, resultHandler)
			return ExternalApiResult.SUCCESS_WAIT
		}

		val resultHandler = object : MethodChannel.Result {
			override fun success(result: Any?) {
				val affinity = result as String
				getAction()?.let {
					it.setOutputValue(Expression.Value.newString(affinity))
					ActionExecution.continueCurrent(it.activity, true, it)
				}
			}

			override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {}
			override fun notImplemented() {}
		}
	}

	companion object {
		const val NAME = "Payments"
		const val METHOD_PAY_NO_UI = "PayWithoutUI"
		const val METHOD_PAY_UI = "PayWithUI"
		const val METHOD_GET_CLIENT_INFO = "GetClientInformation"
		const val METHOD_PAYMENT_INFORMATION = "GetPaymentInformation"
		const val METHOD_GET_PAYMENT_AFFINITY = "GetPaymentInfoAffinity"
	}

	init {
		addMethodHandler(METHOD_PAY_NO_UI, 1, methodPayWithoutUI)
		addMethodHandler(METHOD_PAY_UI, 1, methodPayWithUI)
		addMethodHandler(METHOD_GET_CLIENT_INFO, 1, methodGetClientInformation)
		addMethodHandler(METHOD_PAYMENT_INFORMATION, 1, methodGetPaymentInformation)
		addMethodHandler(METHOD_GET_PAYMENT_AFFINITY, 1, methodGetPaymentInfoAffinity)
	}
}