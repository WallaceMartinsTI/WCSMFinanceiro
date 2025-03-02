package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun BottomNavigationBarItem(
    label: String,
    icon: ImageVector,
    iconDescription: String,
    selected: Boolean,
    onNavigate: () -> Unit
) {
    val offsetY: Dp by animateDpAsState(
        targetValue = if (selected) 8.dp else 0.dp,
        label = "offsetY"
    )

    Column(
        modifier = Modifier
            .offset { IntOffset(x = 0, y = -offsetY.roundToPx()) }
            .clip(RoundedCornerShape(5.dp))
            .clickable { onNavigate() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconDescription,
            tint = if(selected) PrimaryColor else OnBackgroundColor
        )

        Text(
            text = label,
            color = if(selected) PrimaryColor else OnBackgroundColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun BottomNavigationBarItemPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        BottomNavigationBarItem(
            label = "Contas",
            icon = Icons.Default.Payments,
            iconDescription = "Ícone de pagamento",
            selected = true
        ) {}
    }
}