package com.wcsm.wcsmfinanceiro.presentation.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CurrentDateTimeContainer
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomDateRangePicker
import com.wcsm.wcsmfinanceiro.presentation.ui.component.StylizedText
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
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

        CurrentDateTimeContainer()

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            color = OnBackgroundColor
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ícone de lupa",
                    modifier = Modifier
                        .clickable {
                            if(selectedFilterDate == "Selecione uma data") {
                                selectedFilterDateErrorMessage = "Selecione uma data para filtrar."
                            }
                        }
                        .size(40.dp)
                        .padding(top = 4.dp),
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

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            color = OnBackgroundColor
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            UserValuesContainer(
                iconResourceId = R.drawable.money_up,
                iconContentDescription = "Ícone de dinheiro pra cima",
                text = "Receitas",
                value = 2538.92,
                contentColor = MoneyGreenColor
            )

            Spacer(Modifier.height(16.dp))

            UserValuesContainer(
                iconResourceId = R.drawable.money_down,
                iconContentDescription = "Ícone de dinheiro pra baixo",
                text = "Despezas",
                value = 1327.32,
                contentColor = RedColor
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp).border(1.dp, SecondaryColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "GRÁFICO",
                color = OnSecondaryColor,
                fontSize = 40.sp,
            )
        }
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        HomeView()
    }
}

@Composable
fun UserValuesContainer(
    iconResourceId: Int,
    iconContentDescription: String,
    text: String,
    value: Double,
    contentColor: Color
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.8f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconResourceId),
                contentDescription = iconContentDescription,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )

            Text(
                text = text,
                color = contentColor,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Text(
            text = "R$ $value",
            color = contentColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
private fun UserValuesContainerPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        Column {
            UserValuesContainer(
                iconResourceId = R.drawable.money_up,
                iconContentDescription = "Ícone de dinheiro pra cima",
                text = "Receitas",
                value = 2538.92,
                contentColor = MoneyGreenColor
            )

            Spacer(Modifier.height(16.dp))

            UserValuesContainer(
                iconResourceId = R.drawable.money_down,
                iconContentDescription = "Ícone de dinheiro pra baixo",
                text = "Despezas",
                value = 1327.32,
                contentColor = RedColor
            )
        }
    }
}