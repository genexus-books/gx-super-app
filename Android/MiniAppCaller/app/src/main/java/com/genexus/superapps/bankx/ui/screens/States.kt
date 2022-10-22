package com.genexus.superapps.bankx.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.genexus.superapps.bankx.ui.AnimatedShimmer

object States {
    @Composable
    fun ErrorState(text: String) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberVectorPainter(image = Icons.Filled.Warning),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(5.dp)
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun LoadingState() {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            repeat(7) {
                AnimatedShimmer()
            }
        }
    }
}