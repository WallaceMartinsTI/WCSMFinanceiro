package com.wcsm.wcsmfinanceiro.presentation.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.CurrencyVisualTransformation
import com.wcsm.wcsmfinanceiro.presentation.util.formatMonetaryValue
import com.wcsm.wcsmfinanceiro.presentation.util.getDoubleForStringPrice

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

    var error by remember { mutableStateOf(false) }
    var monetaryValueErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if(alreadyExistsDoubleValue) {
            monetaryValue = alreadyDoubleValue.toString()
            monetaryValue = formatMonetaryValue(monetaryValue).replace(".", "")
        }

        error = isError
        monetaryValueErrorMessage = errorMessage
        Log.i("#-# TESTE #-#", "monetaryValue: $monetaryValue")
        Log.i("#-# TESTE #-#", "error: $error")
        Log.i("#-# TESTE #-#", "monetaryValueErrorMessage: $monetaryValueErrorMessage")
    }

    LaunchedEffect(monetaryValue) {
        Log.i("#-# TESTE #-#", "monetaryValue $monetaryValue")
        Log.i("#-# TESTE #-#", "monetaryValue.length: ${monetaryValue.length}")

        if(monetaryValue == "0") {
            monetaryValue = ""
        }

        if(!error && monetaryValue.length > 9) {
            Log.i("#-# TESTE #-#", "ENTROU IF")
            error = true
            monetaryValueErrorMessage = "Valor muito grande."
        } else {
            error = isError
            monetaryValueErrorMessage = errorMessage
        }

        Log.i("#-# TESTE #-#", "DEPOIS IF")
        Log.i("#-# TESTE #-#", "monetaryValue: $monetaryValue")
        Log.i("#-# TESTE #-#", "error: $error")
        Log.i("#-# TESTE #-#", "monetaryValueErrorMessage: $monetaryValueErrorMessage")


        if(monetaryValue.isNotBlank()) {
            onMonetaryValueChange(getDoubleForStringPrice(monetaryValue))
        } else {
            onMonetaryValueChange(0.0)
        }
    }

    OutlinedTextField(
        value = monetaryValue,
        onValueChange = { newValue ->
            if(newValue.all { it.isDigit() }) {
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
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Ícone de x",
                    modifier = Modifier
                        .clickable {
                            monetaryValue = ""
                            monetaryFocusRequester.requestFocus()
                        },
                    tint = White06Color
                )
            }
        },
        singleLine = true,
        isError = error,
        supportingText = {
            if(error) {
                Text(
                    text = monetaryValueErrorMessage,
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