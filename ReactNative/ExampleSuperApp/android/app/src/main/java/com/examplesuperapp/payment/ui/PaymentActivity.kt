package com.examplesuperapp.payment.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.examplesuperapp.payment.services.PaymentsService
import com.examplesuperapp.payment.ui.theme.BankingSuperAppTheme

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
        val paymentId = PaymentsService.pay(amount)
        val result = Intent().apply { putExtra(EXTRA_PAYMENT_ID, paymentId) }
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    companion object {
        private const val EXTRA_AMOUNT = "Amount"
        const val EXTRA_PAYMENT_ID = "PaymentId"

        fun newIntent(context: Context?, amount: Double): Intent {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(EXTRA_AMOUNT, amount)
            return intent
        }
    }
}
