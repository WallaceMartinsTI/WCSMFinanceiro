package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel.CurrencyConversionViewModel
import com.wcsm.wcsmfinanceiro.util.showToastMessage
import java.util.Locale

@Composable
fun CurrencyConverterView(
    onDismiss: () -> Unit
) {
    val currencyConversionViewModel: CurrencyConversionViewModel = hiltViewModel()

    val context = LocalContext.current

    val currencyConversionState by currencyConversionViewModel.currencyConversionStateFlow.collectAsStateWithLifecycle()
    val uiState by currencyConversionViewModel.uiState.collectAsStateWithLifecycle()

    var isConvertButtonEnable by remember { mutableStateOf(true) }
    var convertButtonText by remember { mutableStateOf("CONVERTER") }

    var conversionResult by remember { mutableStateOf("") }

    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(currencyConversionState.convertedValue) {
        val convertedValue = currencyConversionState.convertedValue
        if(convertedValue == 0.0) {
            conversionResult = ""
        } else {
            val formattedTargetCode = currencyConversionState.targetCode.split("-")[0].trim()
            val formattedConvertedValue = String.format(Locale("pt", "BR"), "%.2f", convertedValue)
            conversionResult = "$formattedTargetCode $$formattedConvertedValue"
        }
    }

    LaunchedEffect(uiState) {
        if(uiState.isLoading) {
            isConvertButtonEnable = false
            convertButtonText = "CONVERTENDO..."
            return@LaunchedEffect
        }

        if(uiState.error?.isNotBlank() == true) {
            val errorMessage = uiState.error
            errorMessage?.let { showToastMessage(context, it) }

            isConvertButtonEnable = true
            convertButtonText = "CONVERTER"

            return@LaunchedEffect
        }

        if(uiState.success) {
            isConvertButtonEnable = true
            convertButtonText = "CONVERTER"

            currencyConversionViewModel.resetUiState()
        }
    }

    Dialog(
        onDismissRequest = {
            if(!isDropdownExpanded) {
                onDismiss()
            }
        }
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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            CurrencyDropdown(
                label = "Moeda (DE)",
                isError = currencyConversionState.baseCodeErrorMessage.isNotBlank(),
                errorMessage = currencyConversionState.baseCodeErrorMessage,
                onValueSelected = { selectedCurrency ->
                    currencyConversionViewModel.updateCurrencyConversionStateFlow(
                        currencyConversionState.copy(
                            baseCode = selectedCurrency
                        )
                    )
                },
                onDropdownStateChange = { expanded -> isDropdownExpanded = expanded }
            )

            CurrencyDropdown(
                label = "Moeda (PARA)",
                isError = currencyConversionState.targetCodeErrorMessage.isNotBlank(),
                errorMessage = currencyConversionState.targetCodeErrorMessage,
                onValueSelected = { selectedCurrency ->
                    currencyConversionViewModel.updateCurrencyConversionStateFlow(
                        currencyConversionState.copy(
                            targetCode = selectedCurrency
                        )
                    )
                },
                onDropdownStateChange = { expanded -> isDropdownExpanded = expanded }
            )

            MonetaryInputField(
                label = "Valor",
                alreadyExistsDoubleValue = false,
                alreadyDoubleValue = 0.0,
                isError = currencyConversionState.valueToConvertErrorMessage.isNotBlank(),
                errorMessage = currencyConversionState.valueToConvertErrorMessage,
                onMonetaryValueChange = { doubleMonetaryValue ->
                    currencyConversionViewModel.updateCurrencyConversionStateFlow(
                        currencyConversionState.copy(
                            valueToConvert = doubleMonetaryValue
                        )
                    )
                },
                modifier = Modifier.width(280.dp),
                imeAction = ImeAction.Done
            )

            Button(
                onClick = {
                    currencyConversionViewModel.getConvertedCurrency(currencyConversionState)
                },
                enabled = isConvertButtonEnable
            ) {
                Text(
                    text = convertButtonText
                )
            }

            OutlinedTextField(
                value = conversionResult,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = "Resultado da conversão",
                        color = TertiaryColor
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
        CurrencyConverterView {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDropdown(
    label: String,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onValueSelected: (selectedCurrency: String) -> Unit,
    onDropdownStateChange: (Boolean) -> Unit
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
            onExpandedChange = { expanded ->
                showCurrencyDropdown = expanded
                onDropdownStateChange(expanded)
            }
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
                onDismissRequest = {
                    showCurrencyDropdown = false
                    onDropdownStateChange(false)
                }
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
                            onDropdownStateChange(false)
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
                errorMessage = "",
                onValueSelected = {},
                onDropdownStateChange = {}
            )

            CurrencyDropdown(
                label = "Moeda (PARA)",
                isError = false,
                errorMessage = "",
                onValueSelected = {},
                onDropdownStateChange = {}
            )
        }
    }
}