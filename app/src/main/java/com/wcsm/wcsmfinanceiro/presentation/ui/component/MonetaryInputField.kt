package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.util.formatMonetaryValue
import com.wcsm.wcsmfinanceiro.util.getDoubleForStringPrice

@Composable
fun MonetaryInputField(
    label: String,
    alreadyExistsDoubleValue: Boolean,
    alreadyDoubleValue: Double,
    isError: Boolean,
    errorMessage: String,
    onMonetaryValueChange: (doubleMonetaryValue: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val monetaryFocusRequester = remember { FocusRequester() }
    var monetaryValue by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if(alreadyExistsDoubleValue) {
            monetaryValue = alreadyDoubleValue.toString()
            monetaryValue = formatMonetaryValue(monetaryValue).replace(".", "")
        }
    }

    LaunchedEffect(monetaryValue) {
        if(monetaryValue == "0") {
            monetaryValue = ""
        }

        if(monetaryValue.isNotBlank()) {
            onMonetaryValueChange(getDoubleForStringPrice(monetaryValue))
        } else {
            onMonetaryValueChange(0.0)
        }
    }

    OutlinedTextField(
        value = monetaryValue,
        onValueChange = { newValue ->
            if(newValue.length <= 12 && newValue.all { it.isDigit() }) {
                monetaryValue = newValue
            }
        },
        modifier = modifier.focusRequester(monetaryFocusRequester),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = "Ícone de dinheiro",
                tint = White06Color
            )
        },
        trailingIcon = {
            if(monetaryValue.isNotBlank()) {
                XIcon {
                    monetaryValue = ""
                    monetaryFocusRequester.requestFocus()
                }
            }
        },
        singleLine = true,
        isError = isError,
        supportingText = {
            if(isError) {
                Text(
                    text = errorMessage,
                    fontFamily = PoppinsFontFamily
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        visualTransformation = CurrencyVisualTransformation()
    )
}

@Preview
@Composable
private fun MonetaryInputFieldPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.fillMaxSize().background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MonetaryInputField(
                label = "Saldo*",
                alreadyExistsDoubleValue = true,
                alreadyDoubleValue = 0.0,
                isError = false,
                errorMessage = "",
                onMonetaryValueChange = {},
                modifier = Modifier.width(280.dp)
            )
        }
    }
}