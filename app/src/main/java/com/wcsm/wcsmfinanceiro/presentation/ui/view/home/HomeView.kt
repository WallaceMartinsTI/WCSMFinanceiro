package com.wcsm.wcsmfinanceiro.presentation.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CurrentDateTimeContainer
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomDateRangePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.StylizedText
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.White06Color

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current

    val userName = "Wallace"

    var showRangeDatePickerDialog by remember { mutableStateOf(false) }
    var selectedFilterDate by remember { mutableStateOf("Selecione uma data") }
    var selectedFilterDateErrorMessage by remember { mutableStateOf("") }

    val filterSelectedDateRange by homeViewModel.filterSelectedDateRange.collectAsState()

    LaunchedEffect(filterSelectedDateRange) {
        if(filterSelectedDateRange.isNotEmpty()) {
            selectedFilterDate = filterSelectedDateRange
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Olá, Fulano!                 Ícone de Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StylizedText(
                initialText = "Olá, ",
                textToStyle = userName,
                endText = "!",
                color = OnBackgroundColor,
                fontWeight = FontWeight.SemiBold,
                fontFamily = PoppinsFontFamily,
                style = SpanStyle(
                    color = PrimaryColor,
                    fontWeight = FontWeight.Bold
                )
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Ícone de logout",
                tint = ErrorColor
            )
        }

        // Data e hora em tempo real
        CurrentDateTimeContainer()

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            color = OnBackgroundColor
        )

        // Filtro para selecionar o mes
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                value = selectedFilterDate,
                onValueChange = {},
                modifier = Modifier
                    .width(300.dp)
                    .onFocusEvent {
                        if(it.isFocused) {
                            showRangeDatePickerDialog = true
                            focusManager.clearFocus()
                            //focusManager.clearFocus(force = true)
                        }
                    }
                ,
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
                                    selectedFilterDate = "Selecione uma data"
                                },
                            tint = White06Color
                        )
                    }
                },
                singleLine = true,
                readOnly = true
                //isError = nameErrorMessage.isNotEmpty(),
                /*supportingText = {
                    if(nameErrorMessage.isNotEmpty()) {
                        Text(
                            text = nameErrorMessage
                        )
                    }
                }*/
            )
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de lupa",
                    modifier = Modifier.size(40.dp),
                    tint = White06Color
                )
            }
        }

        if(showRangeDatePickerDialog) {
            CustomDateRangePicker(
                onDismiss = { showRangeDatePickerDialog = false },
                onFillDate = { startDate, endDate ->
                    homeViewModel.updateFilterSelectedDateRange(
                        dateRange = "$startDate - $endDate"
                    )
                }
            )
        }

        // Receitas
        // Despezas

        // Gráfico de Receitas e Despezas
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        HomeView()
    }
}