package com.wcsm.wcsmfinanceiro.domain.entity

import androidx.compose.ui.graphics.vector.ImageVector

data class Category(
    val id: Long,
    val title: String,
    val icon: ImageVector? = null
)
