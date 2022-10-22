package com.genexus.superapps.bankx.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.genexus.superapps.bankx.ui.BottomNavController
import com.genexus.superapps.bankx.ui.theme.BankingSuperAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BankingSuperAppTheme {
                BottomNavController()
            }
        }
    }
}
