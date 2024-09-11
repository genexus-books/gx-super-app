package com.genexus.superapps.bankx.payments

import com.genexus.superapps.bankx.payments.services.ClientsService.getClientInformation
import com.genexus.android.core.actions.ApiAction
import com.genexus.android.core.externalapi.ExternalApi
import com.genexus.android.core.externalapi.ExternalApi.IMethodInvoker
import com.genexus.android.core.externalapi.ExternalApiResult
import android.content.Intent
import android.app.Activity
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityFactory
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.utils.Strings
import com.genexus.superapps.bankx.payments.services.PaymentsService
import com.genexus.superapps.bankx.payments.ui.PaymentActivity
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
            val payInformation = parameters[0] as Entity
            val amount = payInformation.optStringProperty(PaymentActivity.EXTRA_AMOUNT).toDouble()
            startActivityForResult(PaymentActivity.newIntent(context, amount), PAYMENT_REQUEST_CODE)
            return ExternalApiResult.SUCCESS_WAIT
        }

        override fun handleActivityResult(requestCode: Int, resultCode: Int, result: Intent?): ExternalApiResult {
            if (resultCode == Activity.RESULT_OK && requestCode == PAYMENT_REQUEST_CODE && result != null) {
                val code = result.getStringExtra(PaymentActivity.EXTRA_CODIGO)
                val message = result.getStringExtra(PaymentActivity.EXTRA_MENSAJE)
                val idAuthorization = result.getStringExtra(PaymentActivity.EXTRA_IDAUTORIZACION)

                val payResult = EntityFactory.newSdt(SDT_PAY_RESULT)
                payResult.setProperty(SDT_PAY_RESULT_CODIGO, code)
                payResult.setProperty(SDT_PAY_RESULT_MENSAJE, message)
                payResult.setProperty(SDT_PAY_RESULT_IDAUTORIZACION, idAuthorization)

                return ExternalApiResult.success(payResult)
            }
            return ExternalApiResult.failure("An error occurred processing your payment.")
        }
    }

    //Method that receives a String and returns a Client SDT
    private val methodGetClientInformation = IMethodInvoker { parameters: List<Any> ->
        CoroutineScope(Dispatchers.IO).launch {
            val clientInformation = getClientInformation()
            action?.let {
                it.setOutputValue(Expression.Value.newValue(clientInformation))
                ActionExecution.continueCurrent(it.activity, true, it)
            }
        }

        ExternalApiResult.SUCCESS_WAIT
    }

    //Method that receives a Client SDT and returns a PaymentInformation SDT (Collection)
    private val methodGetPaymentInformation = IMethodInvoker { parameters: List<Any?> ->
        val paymentInformationList = PaymentsService.getPaymentInformationList()
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
        const val NAME = "Itau"
        private const val METHOD_PAY_UI = "Pay"
        private const val METHOD_GET_CLIENT_INFO = "GetClientINFO"
        private const val METHOD_PAYMENT_INFORMATION = "GetPaymentINFO"

        private const val SDT_PAY_RESULT = "PayResult"
        private const val SDT_PAY_RESULT_CODIGO = "codigo"
        private const val SDT_PAY_RESULT_MENSAJE = "mensaje"
        private const val SDT_PAY_RESULT_IDAUTORIZACION = "idautorizacion"

        //not used
        private const val METHOD_PAY_NO_UI = "PayWithoutUI"
        private const val METHOD_GET_PAYMENT_AFFINITY = "GetPaymentInfoAffinity"
        private const val PAYMENT_REQUEST_CODE = 1111
    }

    init {
        addMethodHandler(METHOD_PAY_NO_UI, 1, methodPayWithoutUI)
        addMethodHandler(METHOD_PAY_UI, 1, methodPayWithUI)
        addMethodHandler(METHOD_GET_CLIENT_INFO, 0, methodGetClientInformation)
        addMethodHandler(METHOD_PAYMENT_INFORMATION, 0, methodGetPaymentInformation)
        addMethodHandler(METHOD_GET_PAYMENT_AFFINITY, 1, methodGetPaymentInfoAffinity)
    }
}