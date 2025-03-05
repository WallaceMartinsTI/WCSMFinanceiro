package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings

import android.app.Activity
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wcsm.wcsmfinanceiro.R
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomCheckbox
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomSwitch
import com.wcsm.wcsmfinanceiro.presentation.ui.component.ExitAppDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.BackgroundColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.ErrorColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.GrayColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PoppinsFontFamily
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.PrimaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.SecondaryColor
import com.wcsm.wcsmfinanceiro.presentation.ui.theme.WCSMFinanceiroTheme
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components.AppInfoView
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components.ProfileContainer
import com.wcsm.wcsmfinanceiro.presentation.ui.view.settings.components.RowButton

@Composable
fun SettingsView() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val activity = LocalContext.current as? Activity

    var showAppInfoView by remember { mutableStateOf(false) }
    var showExitAppDialog by remember { mutableStateOf(false) }

    var notificationsEnabled by remember { mutableStateOf(false) } // TEMP

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
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

            ProfileContainer()

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            CustomSwitch(
                switchText = "Permitir Notificações e Lembretes",
                alreadyChecked = false,
            ) { isChecked -> notificationsEnabled = isChecked }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            RowButton(
                rowText = "Informações do Aplicativo",
                leadingIcon = Icons.Default.Info,
                trailingIcon = Icons.Default.OpenInFull,
                rowBackgroundColor = SecondaryColor
            ) { showAppInfoView = true }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = GrayColor)

            RowButton(
                rowText = "Encerrar Sessão",
                leadingIcon = Icons.Default.Person,
                trailingIcon = Icons.AutoMirrored.Filled.Logout,
                rowBackgroundColor = ErrorColor
            ) { showExitAppDialog = true }

            Spacer(Modifier.height(8.dp))
        }

        if(showAppInfoView) {
            AppInfoView(
                onEmailClick = { settingsViewModel.sendEmail(activity) },
                onDismiss = { showAppInfoView = false }
            )
        }

        if(showExitAppDialog) {
            ExitAppDialog(
                onExitApp = { activity?.finish() },
                onLogout = {}, // FAZER LOGOUT
                onDismiss = { showExitAppDialog = false }
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

