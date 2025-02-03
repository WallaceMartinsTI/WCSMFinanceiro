package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
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
fun CustomCheckbox(
    checkboxText: String,
    alreadyChecked: Boolean,
    modifier: Modifier = Modifier,
    onCheckdChange: (isChecked: Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(alreadyChecked) }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .width(280.dp)
            .selectable(
                selected = isChecked,
                onClick = {
                    isChecked = !isChecked
                    onCheckdChange(isChecked)
                },
                role = Role.Checkbox
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = !isChecked
                onCheckdChange(isChecked)
            },
        )

        Text(
            text = checkboxText,
            fontFamily = PoppinsFontFamily,
            color = if (isChecked) PrimaryColor else White06Color,
            fontSize = 14.sp,
        )
    }
}

@Preview
@Composable
private fun CustomCheckboxPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        CustomCheckbox(
            checkboxText = "Continuar Logado?",
            alreadyChecked = false,
            onCheckdChange = {}
        )
    }
}