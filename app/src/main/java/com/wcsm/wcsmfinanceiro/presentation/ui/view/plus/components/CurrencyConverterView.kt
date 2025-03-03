package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterView() {
    val options = listOf(
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

    var baseCurrencyExpanded by remember { mutableStateOf(false) }
    var targetCurrencyExpanded by remember { mutableStateOf(false) }

    var baseCurrencySelected by remember { mutableStateOf(options[0]) }
    var targetCurrencySelected by remember { mutableStateOf(options[0]) }

    var valueToConvert by remember { mutableStateOf("") }
    var valueConverted by remember { mutableStateOf("") }

    LaunchedEffect(valueToConvert) {
        println("+++++++ valueToConvert: $valueToConvert")
    }

    Dialog(
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "CONVERSOR MONETÁRIO",
                color = SecondaryColor,
                fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = baseCurrencyExpanded,
                onExpandedChange = {
                    baseCurrencyExpanded = !baseCurrencyExpanded
                }
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = "Moeda (DE)"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = baseCurrencyExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = baseCurrencyExpanded,
                    onDismissRequest = {
                        baseCurrencyExpanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectionOption
                                )
                            },
                            onClick = {
                                baseCurrencySelected = selectionOption
                                baseCurrencyExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = targetCurrencyExpanded,
                onExpandedChange = {
                    targetCurrencyExpanded = !targetCurrencyExpanded
                }
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = "Moeda (PARA)"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = targetCurrencyExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = targetCurrencyExpanded,
                    onDismissRequest = {
                        targetCurrencyExpanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectionOption
                                )
                            },
                            onClick = {
                                targetCurrencySelected = selectionOption
                                targetCurrencyExpanded = false
                            }
                        )
                    }
                }
            }

            MonetaryInputField(
                label = "Valor",
                alreadyExistsDoubleValue = false,
                alreadyDoubleValue = 0.0,
                isError = false,
                errorMessage = "",
                onMonetaryValueChange = { doubleMonetaryValue ->
                    valueToConvert = doubleMonetaryValue.toString()
                },
                modifier = Modifier.width(280.dp)
            )

            Button(
                onClick = {}
            ) {
                Text(
                    text = "CONVERTER"
                )
            }

            OutlinedTextField(
                value = valueConverted,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = "Resultado da conversão"
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun CurrencyConverterViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        CurrencyConverterView()
    }
}