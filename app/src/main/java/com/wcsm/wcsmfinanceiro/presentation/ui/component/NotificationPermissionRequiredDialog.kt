package com.wcsm.wcsmfinanceiro.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.OnSurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SurfaceColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme

@Composable
fun NotificationPermissionRequiredDialog(
    askForEnable: Boolean,
    onGoToSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    val text = if(askForEnable) "Você não permitiu o envio de notificações do aplicativo. Para um melhor proveito do aplicativo é necessário permitir as notificações. Vá nas configurações do aplicativo e permita receber notificações."
    else "Para desativar o envio de notificações vá nas configurações do aplicativo e desative a permissão de notificações."

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
                .background(SurfaceColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                color = OnSurfaceColor,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(GrayColor)
                    .clickable { onGoToSettings() }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Ícone de configuraçõesa",
                    tint = Color.White,
                )
                Text(
                    text = "CONFIGURAÇÕES",
                    color = Color.White
                )

                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Ícone de seta para direita",
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
                    .background(ErrorColor)
                    .clickable { onDismiss() }
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "FECHAR",
                    color = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
private fun NotificationPermissionRequiredDialogPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        NotificationPermissionRequiredDialog(askForEnable = true, onGoToSettings = {}) {}
    }
}