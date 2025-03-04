package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    label: String,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onValueSelected: (selectedCurrency: String) -> Unit
) {
    var currency by remember { mutableStateOf("Selecione uma moeda") }

    val currencyDropdownOptions = listOf(
        "Selecione uma moeda",
        "AED - Emirados Árabes Unidos",
        "ARS - Argentina",
        "AUD - Austrália",
        "BRL - Brasil",
        "CAD - Canadá",
        "CNY - China",
        "EGP - Egito",
        "GBP - Reino Unido",
        "JPY - Japão",
        "NZD - Nova Zelândia",
        "THB - Tailândia",
        "UAH - Ucrânia",
        "USD - Estados Unidos",
        "YER - Iêmen"
    )

    var showCurrencyDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(currency) {
        if(currency.isNotBlank()) {
            onValueSelected(currency)
        }
    }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = showCurrencyDropdown,
            onExpandedChange = { showCurrencyDropdown = !showCurrencyDropdown }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .width(280.dp),
                value = currency,
                onValueChange = {
                    showCurrencyDropdown = !showCurrencyDropdown
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                singleLine = true,
                isError = isError,
                supportingText = {
                    if(errorMessage.isNotBlank()) {
                        Text(
                            text = errorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Ícone de dinheiro",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector =
                        if (showCurrencyDropdown) Icons.Filled.KeyboardArrowUp
                        else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Ícone de seta para cima ou para baixo",
                        tint = White06Color
                    )
                },
                readOnly = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
            )

            ExposedDropdownMenu(
                expanded = showCurrencyDropdown,
                onDismissRequest = { showCurrencyDropdown = false }
            ) {
                currencyDropdownOptions.forEach { selectedCurrency ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = selectedCurrency,
                                fontFamily = PoppinsFontFamily
                            )
                        },
                        onClick = {
                            currency = selectedCurrency
                            showCurrencyDropdown = false
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CurrencyDropdownPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column {
            CurrencyDropdown(
                label = "Moeda (DE)",
                isError = false,
                errorMessage = ""
            ) {}

            CurrencyDropdown(
                label = "Moeda (PARA)",
                isError = false,
                errorMessage = ""
            ) {}
        }
    }
}