package com.genexus.example_superapp.api

import android.content.Context
import android.os.Bundle
import com.genexus.example_superapp.EngineBindingsDelegate
import com.genexus.example_superapp.FlutterEngineManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel
import com.genexus.example_superapp.SuperAppAPI
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class FlutterPaymentActivity : FlutterActivity(), EngineBindingsDelegate {

    private var channelPaymentsFlow: MethodChannel? = null
    private lateinit var amount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flutterEngine = provideFlutterEngine(this)
        if (flutterEngine == null) {
            finish()
            return
        }

        amount = intent.extras?.getString("amount") ?: "0"

        channelPaymentsFlow = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL_PAYMENT_FLOW).apply {
            setMethodCallHandler(methodCallHandler)
            invokeMethod(METHOD_INIT, mapOf("data" to amount))
        }
    }

    override fun getDartEntrypointFunctionName(): String {
        return FlutterEngineManager.ENTRY_POINT_PAY_WITH_UY
    }

    override fun getDartEntrypointArgs(): MutableList<String> {
        return mutableListOf(amount)
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get(FlutterEngineManager.ENGINE_PAYMENT_SCREEN)
    }

    private val methodCallHandler = MethodChannel.MethodCallHandler { call, result ->
        when (call.method) {
            METHOD_FLOW_CONFIRM -> {
                this@FlutterPaymentActivity.onConfirm(call.argument<String>("data"))
                result.success(null)
            }

            METHOD_FLOW_CANCEL -> {
                this@FlutterPaymentActivity.onCancel()
                result.success(null)
            }

            METHOD_FLOW_GETINFO -> {
                // Handle the call in a coroutine to wait for onGetInformation
                lifecycleScope.launch {
                    try {
                        val data = onGetInformation(call.argument<String>("data"))
                        if (data != null) {
                            result.success(data) // Send the result back to Flutter
                        } else {
                            result.error("UNAVAILABLE", "Data not available.", null)
                        }
                    } catch (e: Exception) {
                        result.error("ERROR", "Exception occurred: ${e.message}", null)
                    }
                }
            }

            else -> result.notImplemented()
        }
    }

    override fun onConfirm(args: String?) {
        finish()
        PaymentsApi.UI_RESULT_HANDLER?.success(args)
    }

    override fun onCancel() {
        finish()
        PaymentsApi.UI_RESULT_HANDLER?.error("1", "Payment canceled", null)
    }

    override suspend fun onGetInformation(args: String?): String? {
        return when (args) {
            GETINFORMATION_SESSION -> {
                // Wait for the result of the API call
                SuperAppAPI.getSessionInformation()
            }
            else -> null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        channelPaymentsFlow?.setMethodCallHandler(null)
    }

    companion object {
        private const val CHANNEL_PAYMENT_FLOW = "com.genexus.superapp/paymentConfirm"
        private const val METHOD_INIT = "init"
        private const val METHOD_FLOW_CONFIRM = "confirmFlutterActivity"
        private const val METHOD_FLOW_CANCEL = "closeFlutterActivity"
        private const val METHOD_FLOW_GETINFO = "getInformation"
        private const val GETINFORMATION_SESSION = "session"
    }
}