package com.genexus.superapps.bankx.payments.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.genexus.superapps.bankx.payments.services.PaymentsService
import com.genexus.superapps.bankx.payments.ui.theme.BankingSuperAppTheme

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val amount = intent.extras?.getDouble(EXTRA_AMOUNT) ?: 0.0
        setContent {
            BankingSuperAppTheme {
                PaymentSheet({ canceled() }) {
                    BottomSheet(amount, { succeeded(amount) }, { canceled() })
                }
            }
        }
    }

    private fun canceled() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun succeeded(amount: Double) {
        val result = Intent().apply {
            putExtra(EXTRA_CODIGO, 0)
            putExtra(EXTRA_MENSAJE, "")
            putExtra(EXTRA_IDAUTORIZACION, "45445454511")
        }
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    companion object {
        const val EXTRA_AMOUNT = "amount"
        const val EXTRA_CODIGO = "codigo"
        const val EXTRA_MENSAJE = "mensaje"
        const val EXTRA_IDAUTORIZACION = "idautorizacion"

        fun newIntent(context: Context?, amount: Double): Intent {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(EXTRA_AMOUNT, amount)
            return intent
        }
    }
}
