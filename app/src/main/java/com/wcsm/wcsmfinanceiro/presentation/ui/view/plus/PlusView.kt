package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun PlusView() {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "PLUS",
            color = PrimaryColor,
            modifier = Modifier.padding(vertical = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            fontFamily = PoppinsFontFamily
        )
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        PlusView()
    }
}