package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wcsm.wcsmfinanceiro.presentation.ui.component.CustomSwitch
import com.wcsm.wcsmfinanceiro.presentation.ui.component.ExitAppDialog
import com.wcsm.wcsmfinanceiro.presentation.ui.component.NotificationPermissionRequiredDialog
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
import kotlinx.coroutines.delay

@Composable
fun SettingsView() {
    val settingsViewModel: SettingsViewModel = viewModel()

    val context = LocalContext.current

    var showAppInfoView by remember { mutableStateOf(false) }
    var showExitAppDialog by remember { mutableStateOf(false) }
    var showNotificationPermissionRequiredDialog by remember { mutableStateOf(false) }

    var isNotificationSwitchEnabled by remember { mutableStateOf(true) }
    var askForEnableNotificationPermission by remember { mutableStateOf(true) }

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if(!isGranted) {
                showNotificationPermissionRequiredDialog = true
            }
        }
    )

    LaunchedEffect(isNotificationSwitchEnabled) {
        delay(2000)
        isNotificationSwitchEnabled = true
    }

    LaunchedEffect(showNotificationPermissionRequiredDialog) {
        if(!showNotificationPermissionRequiredDialog) {
            hasNotificationPermission = settingsViewModel.checkForNotificationPermission(context)
        }
    }

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
                alreadyChecked = hasNotificationPermission,
                enabled = isNotificationSwitchEnabled
            ) { isChecked ->
                isNotificationSwitchEnabled = false

                if(isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    if(hasNotificationPermission) {
                        askForEnableNotificationPermission = false
                        showNotificationPermissionRequiredDialog = true
                    }
                }
            }

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
                onEmailClick = { settingsViewModel.sendEmail(context as? Activity) },
                onDismiss = { showAppInfoView = false }
            )
        }

        if(showExitAppDialog) {
            ExitAppDialog(
                onExitApp = { (context as? Activity)?.finish() },
                onLogout = {}, // FAZER LOGOUT
                onDismiss = { showExitAppDialog = false }
            )
        }

        if(showNotificationPermissionRequiredDialog) {
            NotificationPermissionRequiredDialog(
                askForEnable = askForEnableNotificationPermission,
                onGoToSettings = {
                    settingsViewModel.goToAppDetailsSettings(context as? Activity)
                },
                onDismiss = { showNotificationPermissionRequiredDialog = false }
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

