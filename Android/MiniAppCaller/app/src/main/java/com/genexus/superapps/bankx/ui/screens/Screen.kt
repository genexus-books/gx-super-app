package com.genexus.superapps.bankx.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.genexus.superapps.bankx.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Main : Screen("Home", R.string.id_main, Icons.Filled.Home)
    object Cache : Screen("Cache", R.string.id_cache, Icons.Filled.Favorite)
}
