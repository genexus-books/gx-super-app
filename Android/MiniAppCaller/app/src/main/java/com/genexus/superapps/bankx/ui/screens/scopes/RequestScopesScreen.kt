package com.genexus.superapps.bankx.ui.screens.scopes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
            text = "Mini App '$miniAppId' needs your permission to access the following scopes",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = scopes,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Button(
                onClick = onAccepted,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text("Accept")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onRejected,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reject")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRequestScopesScreen() {
    RequestScopesScreen(
        miniAppId = "com.genexus.example.miniapp",
        scopes = "user_email;user_name;user_id",
        onRejected = { },
        onAccepted = { }
    )
}
