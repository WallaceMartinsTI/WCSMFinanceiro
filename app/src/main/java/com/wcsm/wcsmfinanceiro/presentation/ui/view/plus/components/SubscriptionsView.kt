package com.wcsm.wcsmfinanceiro.presentation.ui.view.plus.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun SubscriptionsView() {

    var showSubscriptionDetails by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {}
    ) {
        Box(
          modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ASSINATURAS"
                )

                LazyColumn {

                }
            }

        }

        if(showSubscriptionDetails) {}

    }
}

@Preview
@Composable
private fun SubscriptionsViewPreview() {
    WCSMFinanceiroTheme {
        SubscriptionsView()
    }
}

@Preview
@Composable
private fun SubscriptionItem() {
    Row(
       modifier = Modifier.fillMaxWidth()
    ) {

    }
}