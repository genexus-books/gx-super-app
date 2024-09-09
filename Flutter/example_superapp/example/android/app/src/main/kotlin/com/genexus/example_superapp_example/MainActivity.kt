package com.genexus.example_superapp_example

import android.os.Bundle
import com.genexus.example_superapp.FlutterEngineManager
import io.flutter.embedding.android.FlutterActivity

class MainActivity: FlutterActivity() {

    private var engineManager: FlutterEngineManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engineManager = FlutterEngineManager(context).apply { createPaymentsRoute() }
    }

    override fun onDestroy() {
        super.onDestroy()
        engineManager?.destroyPaymentsRoute()
    }
}
