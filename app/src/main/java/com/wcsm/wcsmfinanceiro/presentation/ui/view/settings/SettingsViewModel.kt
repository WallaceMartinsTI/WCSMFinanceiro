package com.wcsm.wcsmfinanceiro.presentation.ui.view.settings

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.wcsm.wcsmfinanceiro.util.showToastMessage

class SettingsViewModel : ViewModel() {

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