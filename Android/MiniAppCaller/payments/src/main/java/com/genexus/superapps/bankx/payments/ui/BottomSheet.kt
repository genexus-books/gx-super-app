package com.genexus.superapps.bankx.payments.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheet(@PreviewParameter(AmountProvider::class) amount: Double, succeeded: () -> Unit, canceled: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Confirm $$amount payment?",
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomButton(
                text = "Confirm",
                color = Color.Green
            ) { succeeded() }
            Spacer(modifier = Modifier.height(8.dp))
            CustomButton(
                text = "Cancel",
                color = Color.Red
            ) { canceled() }
        }
    }
}

@Composable
fun CustomButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )
    }
}

private class AmountProvider : PreviewParameterProvider<Int> {
    override val values = listOf(10, 20).asSequence()
}
