package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components.AppInfoView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components.ProfileContainer

@Composable
fun SettingsView() {

    var showAppInfoView by remember { mutableStateOf(false) }
    var showConfirmLogoutDialog by remember { mutableStateOf(false) }

    var notificationsEnabled by remember { mutableStateOf(false) } // TEMP

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CONFIGURAÇÕES",
                color = PrimaryColor,
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                fontFamily = PoppinsFontFamily
            )

            // Profile
            ProfileContainer()

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            CustomCheckbox(
                checkboxText = "Notificações habilitadas?",
                alreadyChecked = false,
            ) { isChecked -> notificationsEnabled = isChecked }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            // App Info
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .clickable {  }
                    .fillMaxWidth()
                    .background(SecondaryColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Ícone de informação",
                    tint = Color.White
                )

                Text(
                    text = "Informações do Aplicativo",
                    color = Color.White,
                    fontFamily = PoppinsFontFamily,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.OpenInFull,
                    contentDescription = "Ícone de abrir em tela cheia",
                    tint = Color.White
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            // Logout
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .clickable {  }
                    .fillMaxWidth()
                    .background(ErrorColor)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Ícone de pessoa",
                    tint = Color.White
                )

                Text(
                    text = "Encerrar Sessão",
                    color = Color.White,
                    fontFamily = PoppinsFontFamily,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Ícone de sair",
                    tint = Color.White
                )
            }
        }

        if(showAppInfoView) {
            AppInfoView(
                onEmailClick = {},
                onDismiss = { showAppInfoView = false }
            )
        }
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    WCSMFinanceiroTheme(dynamicColor = false) {
        SettingsView()
    }
}

