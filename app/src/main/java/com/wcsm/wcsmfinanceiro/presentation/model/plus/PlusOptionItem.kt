package com.wcsm.wcsmfinanceiro.presentation.model.plus

import androidx.compose.ui.graphics.vector.ImageVector

data class PlusOptionItem(
    val icon: ImageVector,
    val label: String,
    val onItemClick: () -> Unit
)
