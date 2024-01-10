package com.examplesuperapp.payment

import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApiResult
import com.examplesuperapp.payment.services.PaymentsService
import com.examplesuperapp.payment.services.ClientsService.getClientInformation
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import android.content.Intent
import android.app.Activity
import com.genexus.android.core.base.utils.Strings
import com.examplesuperapp.payment.ui.PaymentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentsApi(action: ApiAction?) : ExternalApi(action) {
    private val methodPayWithoutUI = IMethodInvoker { parameters: List<Any> ->
        val amount = parameters[0].toString().toDouble()
        val paymentId = PaymentsService.pay(amount)
        ExternalApiResult.success(paymentId)
    }
    private val methodPayWithUI: IMethodInvokerWithActivityResult = object : IMethodInvokerWithActivityResult {
        override fun invoke(parameters: List<Any>): ExternalApiResult {
            val amount = parameters[0].toString().toDouble()
            startActivityForResult(PaymentActivity.newIntent(context, amount), PAYMENT_REQUEST_CODE)
            return ExternalApiResult.SUCCESS_WAIT
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (resultCode == Activity.RESULT_OK && requestCode == PAYMENT_REQUEST_CODE && result != null) {
                val paymentId = result.getStringExtra(PaymentActivity.EXTRA_PAYMENT_ID)
                return ExternalApiResult.success(paymentId!!)
            }
            return ExternalApiResult.failure("An error occurred processing your payment.")
        }
    }

    //Method that receives a String and returns a Client SDT
    private val methodGetClientInformation = IMethodInvoker { parameters: List<Any> ->
        val clientId = parameters[0].toString()
        CoroutineScope(Dispatchers.IO).launch {
            val clientInformation = getClientInformation(clientId)
            action?.let {
                it.setOutputValue(Expression.Value.newValue(clientInformation))
                ActionExecution.continueCurrent(it.activity, true, it)
            }
        }

        ExternalApiResult.SUCCESS_WAIT
    }

    //Method that receives a Client SDT and returns a PaymentInformation SDT (Collection)
    private val methodGetPaymentInformation = IMethodInvoker { parameters: List<Any?> ->
        val clientInformation = parameters[0] as Entity
        val paymentInformationList = PaymentsService.getPaymentInformationList(clientInformation)
        ExternalApiResult.success(paymentInformationList)
    }

    //Method that receives a PaymentInformation SDT (Collection) and returns a String
    private val methodGetPaymentInfoAffinity = IMethodInvoker { parameters: List<Any> ->
        val paymentInformationList = parameters[0] as EntityList
        if (paymentInformationList.isEmpty())
            return@IMethodInvoker ExternalApiResult.FAILURE

        var paymentInfo: Entity? = null
        //Unnecessary For loop just to prove that an EntityList can be iterated over to pick any element
        for (e in paymentInformationList)
            paymentInfo = e

        val affinity = paymentInfo?.let { PaymentsService.getPaymentInfoAffinity(it) } ?: Strings.EMPTY
        ExternalApiResult.success(affinity)
    }

    companion object {
        const val NAME = "Payments"
        private const val METHOD_PAY_NO_UI = "PayWithoutUI"
        private const val METHOD_PAY_UI = "PayWithUI"
        private const val METHOD_GET_CLIENT_INFO = "GetClientInformation"
        private const val METHOD_PAYMENT_INFORMATION = "GetPaymentInformation"
        private const val METHOD_GET_PAYMENT_AFFINITY = "GetPaymentInfoAffinity"
        private const val PAYMENT_REQUEST_CODE = 1111
    }

    init {
        addMethodHandler(METHOD_PAY_NO_UI, 1, methodPayWithoutUI)
        addMethodHandler(METHOD_PAY_UI, 1, methodPayWithUI)
        addMethodHandler(METHOD_GET_CLIENT_INFO, 1, methodGetClientInformation)
        addMethodHandler(METHOD_PAYMENT_INFORMATION, 1, methodGetPaymentInformation)
        addMethodHandler(METHOD_GET_PAYMENT_AFFINITY, 1, methodGetPaymentInfoAffinity)
    }
}