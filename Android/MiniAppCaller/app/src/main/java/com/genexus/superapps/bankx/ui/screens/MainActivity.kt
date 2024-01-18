package com.genexus.superapps.bankx.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.genexus.android.core.base.services.Services
import com.genexus.superapps.bankx.application.MiniAppLifecycleListener
import com.genexus.superapps.bankx.ui.BottomNavController
import com.genexus.superapps.bankx.ui.theme.BankingSuperAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listener = MiniAppLifecycleListener(this)
        Services.Application.lifecycle.registerMiniApplicationLifecycleListener(listener)
        setContent {
            BankingSuperAppTheme {
                BottomNavController()
            }
        }
    }
}
