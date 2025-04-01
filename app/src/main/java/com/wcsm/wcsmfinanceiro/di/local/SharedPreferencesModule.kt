package com.wcsm.wcsmfinanceiro.di.local

import android.content.Context
import com.wcsm.wcsmfinanceiro.data.local.sharedPrefs.SharedPreferencesHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    fun provideSharedPreferencesHandler(
        @ApplicationContext context: Context
    ): SharedPreferencesHandler {
        return SharedPreferencesHandler(context)
    }
}