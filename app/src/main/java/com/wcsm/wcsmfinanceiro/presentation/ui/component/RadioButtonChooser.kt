package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun RadioButtonChooser(
    inputedOption: String?,
    optionsList: List<String>,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    val radioOptions by remember { mutableStateOf(optionsList) }
    val (selectedOption, setOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    LaunchedEffect(inputedOption) {
        inputedOption?.let {
            setOptionSelected(it)
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .border(1.dp, White06Color, RoundedCornerShape(15.dp))
            .selectableGroup()
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            setOptionSelected(text)
                            onOptionSelected(text)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    color = if(text == selectedOption) PrimaryColor else White06Color,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun RadioButtonChooserPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        val options = listOf(PaymentType.MONEY.displayName, PaymentType.CARD.displayName)
        RadioButtonChooser(inputedOption = null, optionsList = options) {}
    }
}