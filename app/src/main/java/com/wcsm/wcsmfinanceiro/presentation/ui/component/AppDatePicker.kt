package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.TertiaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    onDismiss: () -> Unit,
    onFillDate: (selectedDate: Long) -> Unit
) {
    val datePickerState = rememberDatePickerState(yearRange = IntRange(2000, 2100))

    val dateFormatter = remember {
        SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    var selectedDateText = datePickerState.selectedDateMillis?.let {
        dateFormatter.format(Date(it))
    } ?: "Selecione uma data"

    val datePickerColors = DatePickerDefaults.colors(
        headlineContentColor = PrimaryColor,
        navigationContentColor = SecondaryColor,
        weekdayContentColor = TertiaryColor
    )

    val selectedDate = datePickerState.selectedDateMillis

    LaunchedEffect(Unit) {
        selectedDateText = "Selecione uma data"
    }

    DatePickerDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            Button(
                onClick = {
                    if(selectedDateText != "Selecione uma data") {
                        if(selectedDate != null) {
                            onFillDate(selectedDate)
                        }
                    }
                    onDismiss()
                }
            ) {
                Text(
                    text = "CONFIRMAR"
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = datePickerColors,
            title = {
                Text(
                    text = "Selecione uma data",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = OnSurfaceColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            headline = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = selectedDateText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun AppDatePickerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        AppDatePicker(
            onDismiss = {},
            onFillDate = { _ -> }
        )
    }
}