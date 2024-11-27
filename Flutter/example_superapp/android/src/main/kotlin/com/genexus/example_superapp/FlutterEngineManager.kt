package com.genexus.example_superapp

import android.content.Context
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor

class FlutterEngineManager(private val context: Context) {

    private var groups = FlutterEngineGroup(context)

    private val cache get() = FlutterEngineCache.getInstance()

    fun createPaymentsRoute(): FlutterEngine {
        val engine = cache.get(ENGINE_PAYMENT_SCREEN)
        if (engine != null)
            return engine

        val dartEntryPoint = DartExecutor.DartEntrypoint(
            FlutterInjector.instance().flutterLoader().findAppBundlePath(),
            ENTRY_POINT_PAY_WITH_UY
        )

        return groups.createAndRunEngine(context, dartEntryPoint).let {
            cache.put(ENGINE_PAYMENT_SCREEN, it)
            it
        }

//        val newEngine = FlutterEngine(context).apply {
//            dartExecutor.executeDartEntrypoint(dartEntryPoint, mutableListOf())
//        }
//
//        cache.put(PAYMENTS_ROUTE, newEngine)
//        return newEngine
    }

    fun destroyPaymentsRoute() {
        cache.get(ENGINE_PAYMENT_SCREEN)?.destroy()
        cache.remove(ENGINE_PAYMENT_SCREEN)
    }

    companion object {
        const val ENTRY_POINT_PAY_WITH_UY = "showPayWithUI"
        const val ENGINE_PAYMENT_SCREEN = "FlutterPaymentActivity"
    }
}