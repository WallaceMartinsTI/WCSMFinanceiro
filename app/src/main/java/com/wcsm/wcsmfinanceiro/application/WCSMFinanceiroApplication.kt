package com.wcsm.wcsmfinanceiro.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.wcsm.wcsmfinanceiro.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WCSMFinanceiroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channelId = Constants.NOTIFICATION_CHANNEL_ID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lembrete",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Envia notificações de lembretes"
            }

            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
    }
}