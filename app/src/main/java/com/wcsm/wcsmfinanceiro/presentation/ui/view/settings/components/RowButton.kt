package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun RowButton(
    rowText: String,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector,
    rowBackgroundColor: Color,
    modifier: Modifier = Modifier,
    onRowClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable { onRowClick() }
            .fillMaxWidth()
            .background(rowBackgroundColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leadingIcon, //Icons.Default.Info,
            contentDescription = "Ícone antes do texto",
            tint = Color.White
        )

        Text(
            text = rowText, //"Informações do Aplicativo",
            color = Color.White,
            fontFamily = PoppinsFontFamily,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = trailingIcon, //Icons.Default.OpenInFull,
            contentDescription = "Ícone após o texto",
            tint = Color.White
        )
    }
}

@Preview
@Composable
private fun RowButtonPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        RowButton(
            rowText = "Informações do Aplicativo",
            leadingIcon = Icons.Default.Info,
            trailingIcon = Icons.Default.OpenInFull,
            rowBackgroundColor = SecondaryColor
        ) { }
    }
}