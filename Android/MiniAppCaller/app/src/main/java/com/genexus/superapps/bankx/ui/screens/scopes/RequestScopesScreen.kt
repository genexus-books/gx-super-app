package com.genexus.superapps.bankx.ui.screens.scopes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RequestScopesScreen(
    miniAppId: String,
    scopes: String,
    onRejected: () -> Unit,
    onAccepted: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = miniAppId,
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = scopes,
            style = MaterialTheme.typography.h2,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAccepted,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Accept")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRejected,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reject")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestScopesScreen() {
    RequestScopesScreen(
        miniAppId = "SSO",
        scopes = "user_email",
        onRejected = { },
        onAccepted = { }
    )
}
