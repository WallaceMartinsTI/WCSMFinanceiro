package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.entity.Bill2
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.bills.BillsViewModel

@Composable
fun PlusView(
    billsViwModel: BillsViewModel = viewModel()
) {
    val context = LocalContext.current

    val bill = Bill2(
        0,
        "Origem 1",
        "Titulo 1",
        1.0,
        "Descrição 1",
        1L,
        false,
        1L,
        false
    )

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

        Button(onClick = { billsViwModel.saveBill(context, bill) }) {
            Text("SALVAR")
        }
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        PlusView()
    }
}