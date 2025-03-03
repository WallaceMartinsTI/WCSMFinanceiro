package com.wcsm.wcsmfinanceiro.presentation.ui.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CurrentDateTimeContainer
import com.wcsm.wcsmfinanceiro.presentation.ui.component.DateRangeFilter
import com.wcsm.wcsmfinanceiro.presentation.ui.component.StylizedText
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.MoneyGreenColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnBackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.RedColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.util.toBrazilianDateString

@Composable
fun HomeView(
    homeViewModel: HomeViewModel = viewModel()
) {
    val userName = "Wallace"

    val filterSelectedDateRange by homeViewModel.filterSelectedDateRange.collectAsStateWithLifecycle()

    var selectedFilterDate by remember { mutableStateOf("Selecione uma data") }

    LaunchedEffect(filterSelectedDateRange) {
        if (filterSelectedDateRange != null) {
            val startDateString = filterSelectedDateRange!!.first.toBrazilianDateString()
            val endDateString = filterSelectedDateRange!!.second.toBrazilianDateString()
            selectedFilterDate = "$startDateString - $endDateString"
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

        DateRangeFilter(
            filterSelectedDateRange = filterSelectedDateRange,
            onDateSelected = { startDate, endDate ->
                homeViewModel.updateFilterSelectedDateRange(
                    startDate = startDate,
                    endDate = endDate
                )
            },
            onClearFilter = {},
            onFilter = { _, _ -> } // FAZER FILTRO
        )

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
                fontFamily = PoppinsFontFamily,
                fontSize = 40.sp,
            )
        }
    }
}

@Preview
@Composable
private fun HomeViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        HomeView()
    }
}

@Composable
private fun UserValuesContainer(
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