package com.wcsm.wcsmfinanceiro.presentation.ui.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateRangePicker(
    onDismiss: () -> Unit,
    onFillDate: (startDate: String, endDate: String) -> Unit
) {
    val context = LocalContext.current

    val datePickerState = rememberDateRangePickerState(yearRange = IntRange(2000, 2100))

    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    var startDateText = datePickerState.selectedStartDateMillis?.let {
        dateFormatter.format(Date(it))
    } ?: "Data Inicial"

    var endDateText = datePickerState.selectedEndDateMillis?.let {
        dateFormatter.format(Date(it))
    } ?: "Data Final"

    var toastShowedCounter = 0

    val dateRangePickerColors = DatePickerDefaults.colors(
        headlineContentColor = PrimaryColor,
        subheadContentColor = SecondaryColor
    )

    LaunchedEffect(Unit) {
        startDateText = "Data Inicial"
        endDateText = "Data Final"
    }

    LaunchedEffect(startDateText, endDateText) {
        if(
            isDateValid(
                displayModeValidation = DisplayMode.Picker,
                startDateText = startDateText,
                endDateText = endDateText,
                datePickerDisplayMode = datePickerState.displayMode
            )
        ) {
            onFillDate(startDateText, endDateText)
            onDismiss()
        }
    }

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    if(
                        isDateValid(
                            displayModeValidation = DisplayMode.Input,
                            startDateText = startDateText,
                            endDateText = endDateText,
                            datePickerDisplayMode = datePickerState.displayMode
                        )
                    ) {
                        onFillDate(startDateText, endDateText)
                        onDismiss()
                    } else {
                        if(toastShowedCounter < 2) {
                            Toast.makeText(
                                context,
                                "Data Inválida, tente novamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        toastShowedCounter++
                    }
                }
            ) {
                Text(
                    text = "CONFIRMAR"
                )
            }
        }
    ) {
        DateRangePicker(
            state = datePickerState,
            colors = dateRangePickerColors,
            title = {
                Text(
                    text = "Selecione um intervalo de data para filtrar",
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = startDateText,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = endDateText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun AppDateRangePickerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        AppDateRangePicker(
            onDismiss = {},
            onFillDate = { _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun isDateValid(
    displayModeValidation: DisplayMode,
    startDateText: String,
    endDateText: String,
    datePickerDisplayMode: DisplayMode
) : Boolean {
    val validateDisplayMode = datePickerDisplayMode == displayModeValidation
    val validateDateTexts = startDateText != "Data Inicial" && endDateText != "Data Final"

    return validateDisplayMode && validateDateTexts
}