package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Button
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wcsm.wcsmfinanceiro.presentation.ui.component.MonetaryInputField
import com.wcsm.wcsmfinanceiro.presentation.ui.component.XIcon
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.viewmodel.InstallmentCalculatorViewModel

@Composable
fun InstallmentCalculatorView(
    onDismiss: () -> Unit
) {
    val installmentCalculatorViewModel: InstallmentCalculatorViewModel = hiltViewModel()

    val installmentCalculatorState by installmentCalculatorViewModel.installmentCalculatorStateFlow.collectAsStateWithLifecycle()

    var installmentValue by remember { mutableStateOf("") }
    var feesValue by remember { mutableStateOf("") }

    val installmentFocusRequester = remember { FocusRequester() }
    val feesFocusRequester = remember { FocusRequester() }

    var showTotalWithFees by remember { mutableStateOf(false) }

    LaunchedEffect(installmentCalculatorState.installment) {
        installmentValue = if(installmentCalculatorState.installment == 0) {
            ""
        } else {
            installmentCalculatorState.installment.toString()
        }
    }

    LaunchedEffect(installmentValue) {
        if(installmentValue.isNotBlank()) {
            if(installmentValue.toIntOrNull() != null) {
                installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                    installmentCalculatorState.copy(
                        installment = installmentValue.toInt()
                    )
                )
            } else {
                installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                    installmentCalculatorState.copy(
                        installment = 0
                    )
                )
            }
        }
    }

    LaunchedEffect(feesValue, installmentCalculatorState.installmentCalculationResult) {
        showTotalWithFees = if(feesValue.isNotBlank() && installmentCalculatorState.installmentCalculationResult.isNotBlank()) {
            true
        } else {
            installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                installmentCalculatorState.copy(
                    installmentTotalWithFees = ""
                )
            )
            false
        }
    }

    Dialog(
        onDismissRequest = {
            installmentCalculatorViewModel.resetInstallmentCalculatorStateFlow()
            onDismiss()
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
                text = "CALCULADORA DE PARCELAS",
                color = SecondaryColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            MonetaryInputField(
                label = "Valor",
                alreadyExistsDoubleValue = false,
                alreadyDoubleValue = 0.0,
                isError = installmentCalculatorState.valueErrorMessage.isNotBlank(),
                errorMessage = installmentCalculatorState.valueErrorMessage,
                onMonetaryValueChange = { doubleMonetaryValue ->
                    installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                        installmentCalculatorState.copy(
                            value = doubleMonetaryValue
                        )
                    )
                },
                modifier = Modifier.width(280.dp)
            )

            OutlinedTextField(
                value = installmentValue,
                onValueChange = { newValue ->
                    if(newValue.all { it.isDigit() }) {
                        installmentValue = newValue
                    }
                },
                modifier = Modifier.focusRequester(installmentFocusRequester),
                label = {
                    Text(
                        text = "Parcelas",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Ícone de multiplicação",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    if(installmentValue.isNotBlank()) {
                        XIcon {
                            installmentValue = ""
                            installmentFocusRequester.requestFocus()
                        }
                    }
                },
                singleLine = true,
                isError = installmentCalculatorState.installmentErrorMessage.isNotBlank(),
                supportingText = {
                    if(installmentCalculatorState.installmentErrorMessage.isNotBlank()) {
                        Text(
                            text = installmentCalculatorState.installmentErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = feesValue,
                onValueChange = {
                    feesValue = it
                },
                modifier = Modifier.focusRequester(feesFocusRequester),
                label = {
                    Text(
                        text = "Juros (ao mês)",
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Percent,
                        contentDescription = "Ícone de porcentagem",
                        tint = White06Color
                    )
                },
                trailingIcon = {
                    if(feesValue.isNotBlank()) {
                        XIcon {
                            feesValue = ""
                            feesFocusRequester.requestFocus()
                        }
                    }
                },
                singleLine = true,
                isError = installmentCalculatorState.feesErrorMessage.isNotBlank(),
                supportingText = {
                    if(installmentCalculatorState.feesErrorMessage.isNotBlank()) {
                        Text(
                            text = installmentCalculatorState.feesErrorMessage,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = {
                    installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                        installmentCalculatorState.copy(
                            fees = feesValue.replace(",", ".").trim()
                        )
                    )

                    installmentCalculatorViewModel.calculateInstallments()
                },
            ) {
                Text(
                    text = "CALCULAR"
                )
            }

            OutlinedTextField(
                value = installmentCalculatorState.installmentCalculationResult,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = "Resultado",
                        color = TertiaryColor
                    )
                },
                trailingIcon = {
                    if(installmentCalculatorState.installmentCalculationResult.isNotBlank()) {
                        XIcon {
                            installmentCalculatorViewModel.updateInstallmentCalculatorStateFlow(
                                installmentCalculatorState.copy(
                                    installmentCalculationResult = ""
                                )
                            )
                        }
                    }
                },
            )

            if(showTotalWithFees) {
                OutlinedTextField(
                    value = installmentCalculatorState.installmentTotalWithFees,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text(
                            text = "Total com Juros",
                            color = TertiaryColor
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun InstallmentCalculatorViewPreview() {
    WCSMFinanceiroTheme (dynamicColor = false){
        InstallmentCalculatorView {}
    }
}