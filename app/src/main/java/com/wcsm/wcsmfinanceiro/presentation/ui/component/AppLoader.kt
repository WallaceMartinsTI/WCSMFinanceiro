package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun AppLoader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Preview
@Composable
private fun AppLoaderPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        AppLoader()
    }
}