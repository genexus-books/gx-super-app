package com.genexus.superapps.bankx.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.genexus.android.core.superapps.MiniApp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MiniAppListItem(miniApp: MiniApp, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        onClick = onClick
    ) {
        Row {
            MiniAppImage(miniApp)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)

            ) {
                Text(text = miniApp.normalizedName, style = typography.h6, color = Color.Black)
                Text(text = "${miniApp.id}:${miniApp.version}", style = typography.caption, color = Color.Black)
            }
        }
    }
}

@Composable
private fun MiniAppImage(miniApp: MiniApp) {
    val painter = if (miniApp.iconUrl.isNullOrEmpty())
        rememberVectorPainter(Icons.Filled.Info)
    else
        rememberAsyncImagePainter(miniApp.iconUrl)

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
    )
}
