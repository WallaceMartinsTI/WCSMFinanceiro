package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.util.showToastMessage

class SettingsViewModel : ViewModel() {
    fun checkForNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun goToAppDetailsSettings(activity: Activity?) {
        if(activity != null) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.applicationContext.packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }
    }

    fun sendEmail(activity: Activity?) {
        val devEmail = "wallace159santos@hotmail.com"

        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$devEmail")
            putExtra(Intent.EXTRA_SUBJECT, "Contato via aplicativo WCSM Financeiro")
            putExtra(Intent.EXTRA_TEXT, "Olá, gostaria de entrar em contato...")
        }

        if(activity != null && emailIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(emailIntent)
        } else {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText("Email", devEmail)
            clipboard?.setPrimaryClip(clip)

            activity?.let { showToastMessage(it, "E-mail copiado para a área de transferência!") }
        }
    }
}