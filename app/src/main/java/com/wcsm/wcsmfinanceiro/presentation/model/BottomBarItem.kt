package com.wcsm.wcsmfinanceiro.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val title: String,
    val icon: ImageVector,
    val iconContentDescription: String,
    val route: String
)