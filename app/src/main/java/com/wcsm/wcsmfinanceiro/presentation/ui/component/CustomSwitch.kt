package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun CustomSwitch(
    switchText: String,
    alreadyChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (isChecked: Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(alreadyChecked) }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .selectable(
                selected = isChecked,
                onClick = {
                    isChecked = !isChecked
                    onCheckedChange(isChecked)
                },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = {
                isChecked = !isChecked
                onCheckedChange(isChecked)
            },
            modifier = Modifier.padding(start = 8.dp)
        )

        Text(
            text = switchText,
            fontFamily = PoppinsFontFamily,
            color = if (isChecked) PrimaryColor else White06Color,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
private fun CustomSwitchPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column {
            CustomSwitch(
                switchText = "Permitir Notificações e Lembretes",
                alreadyChecked = true,
                onCheckedChange = {}
            )

            CustomSwitch(
                switchText = "Permitir Notificações e Lembretes",
                alreadyChecked = false,
                onCheckedChange = {}
            )
        }
    }
}