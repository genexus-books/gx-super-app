package com.genexus.superapps.bankx.ui.screens.scopes

import android.app.Activity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.genexus.superapps.bankx.ui.theme.BankingSuperAppTheme

class RequestScopesActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val miniAppId = intent.extras?.getString(KEY_MINIAPP_ID)
        val scopes = intent.extras?.getString(KEY_SCOPES_STRING)
        if (miniAppId.isNullOrEmpty() || scopes.isNullOrEmpty())
            throw IllegalArgumentException("Invalid Mini App Id or Scopes")

        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        
        setContent {
            BankingSuperAppTheme {
                RequestScopesScreen(miniAppId, scopes, onRejected, onAccepted)
            }
        }
    }
    
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            onRejected.invoke()
        }
    }
    
    private val onRejected = object : () -> Unit {
        override fun invoke() {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private val onAccepted = object : () -> Unit {
        override fun invoke() {
            // TODO: Please keep the confirmation of the permissions so that the next time the GXSSOURLCheckMiniAppScope service runs, it returns is_allowed = true.
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
    
    companion object {
        internal const val KEY_MINIAPP_ID = "scopes-miniapp-id"
        internal const val KEY_SCOPES_STRING = "scopes-string"
    }
}