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
        val miniAppId = intent.extras?.getString(EXTRA_MINIAPP_ID) ?: ""
        setContent {
            BankingSuperAppTheme {
                PaymentSheet({ canceled() }) {
                    BottomSheet(amount, miniAppId, { succeeded(amount, miniAppId) }, { canceled() })
                }
            }
        }
    }

    private fun canceled() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun succeeded(amount: Double, miniAppId: String) {
        val paymentId = PaymentsService.pay(amount, miniAppId)
        val result = Intent().apply { putExtra(EXTRA_PAYMENT_ID, paymentId) }
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    companion object {
        private const val EXTRA_AMOUNT = "Amount"
        private const val EXTRA_MINIAPP_ID = "MiniAppId"
        const val EXTRA_PAYMENT_ID = "PaymentId"

        fun newIntent(context: Context?, amount: Double, miniAppId: String?): Intent {
            return Intent(context, PaymentActivity::class.java).apply {
                putExtra(EXTRA_AMOUNT, amount)
                if (miniAppId != null)
                    putExtra(EXTRA_MINIAPP_ID, miniAppId)
            }
        }
    }
}
