package com.genexus.superapps.bankx.ui.screens.scopes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class RequestScopesContract: ActivityResultContract<RequestScopesContract.Input, Boolean>() {
    override fun createIntent(context: Context, input: Input): Intent {
        return Intent(context, RequestScopesActivity::class.java).apply { 
            putExtra(RequestScopesActivity.KEY_MINIAPP_ID, input.miniAppId)
            putExtra(RequestScopesActivity.KEY_SCOPES_STRING, input.scopes)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }

    data class Input(val miniAppId: String, val scopes: String)
}