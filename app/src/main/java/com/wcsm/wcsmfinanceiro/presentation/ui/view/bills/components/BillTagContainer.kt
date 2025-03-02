package com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun BillTagsContainer(
    tags: List<String>,
    onDeleteTag: (tagToDelete: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .heightIn(0.dp, 200.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(tags) { tag ->
            BillTag(tag) {
                onDeleteTag(tag)
            }
        }
    }
}

@Preview
@Composable
private fun BillTagsContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val tags = listOf("Entrada", "Trabalhozando", "Bonificaçãotogodata", "2025")

        Column(
            modifier = Modifier
                .size(350.dp, 150.dp)
                .background(BackgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BillTagsContainer(tags = tags) {}
        }
    }
}