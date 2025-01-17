package com.wcsm.wcsmfinanceiro.data.entity

import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: Long,
    val title: String,
    val icon: ImageVector? = null
)
