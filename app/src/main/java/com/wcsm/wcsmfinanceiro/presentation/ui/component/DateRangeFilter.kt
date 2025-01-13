package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color
import com.wcsm.wcsmfinanceiro.presentation.util.toBrazilianDateString

@Composable
fun DateRangeFilter(
    filterSelectedDateRange: Pair<Long, Long>?,
    onDateSelected: (startDate: Long, endDate: Long) -> Unit,
    onClearFilter: () -> Unit,
    onFilter: (startDate: Long, endDate: Long) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    var showRangeDatePickerDialog by remember { mutableStateOf(false) }
    var selectedFilterDate by remember { mutableStateOf("Selecione uma data") }
    var selectedFilterDateErrorMessage by remember { mutableStateOf("") }

    var alreadyFiltered by remember { mutableStateOf(false) }

    val startDate = filterSelectedDateRange?.first
    val endDate = filterSelectedDateRange?.second

    LaunchedEffect(filterSelectedDateRange) {
        if (filterSelectedDateRange != null) {
            val startDateString = startDate!!.toBrazilianDateString()
            val endDateString = endDate!!.toBrazilianDateString()
            selectedFilterDate = "$startDateString - $endDateString"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        OutlinedTextField(
            value = selectedFilterDate,
            onValueChange = {},
            modifier = Modifier
                .width(300.dp)
                .focusRequester(focusRequester)
                .onFocusEvent {
                    if(it.isFocused) {
                        showRangeDatePickerDialog = true
                        focusRequester.freeFocus()
                    }
                },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Ícone de pessoa",
                    tint = White06Color
                )
            },
            trailingIcon = {
                if(selectedFilterDate != "Selecione uma data") {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Ícone de x",
                        modifier = Modifier
                            .clickable {
                                onClearFilter()
                                alreadyFiltered = false
                                selectedFilterDate = "Selecione uma data"
                            },
                        tint = White06Color
                    )
                }
            },
            singleLine = true,
            readOnly = true,
            isError = selectedFilterDateErrorMessage.isNotEmpty(),
            supportingText = {
                if(selectedFilterDateErrorMessage.isNotEmpty()) {
                    Text(
                        text = selectedFilterDateErrorMessage
                    )
                }
            }
        )
        IconButton(
            onClick = {
                if(alreadyFiltered) {
                    onClearFilter()
                    alreadyFiltered = false
                } else {
                    selectedFilterDateErrorMessage = ""
                    if (selectedFilterDate == "Selecione uma data") {
                        selectedFilterDateErrorMessage = "Selecione uma data para filtrar."
                    } else {
                        if(startDate != null && endDate != null) {
                            onFilter(startDate, endDate)
                            alreadyFiltered = true
                        }
                    }
                }
            }
        ) {
            if(alreadyFiltered) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Ícone de x",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 4.dp),
                    tint = White06Color
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de lupa",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 4.dp),
                    tint = White06Color
                )
            }
        }
    }

    if(showRangeDatePickerDialog) {
        AppDateRangePicker(
            onDismiss = {
                showRangeDatePickerDialog = false
            },
            onFillDate = { startDateParam, endDateParam ->
                onDateSelected(startDateParam, endDateParam)
            }
        )
    }
}

@Preview
@Composable
private fun DateTimeContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        DateRangeFilter(
            filterSelectedDateRange = Pair(0L, 0L),
            onDateSelected = { _, _ -> },
            onClearFilter = {}
        ) { _, _ -> }
    }
}