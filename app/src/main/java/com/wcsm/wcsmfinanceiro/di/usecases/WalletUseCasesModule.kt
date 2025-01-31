package com.wcsm.wcsmfinanceiro.di.usecases

import com.wcsm.wcsmfinanceiro.domain.repository.WalletCardRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.DeleteWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.GetWalletWithCardsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.SaveWalletUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletCardUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.wallet.UpdateWalletUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WalletUseCasesModule {

    @Provides
    fun provideGetWalletWithCardsUseCase(
        walletRepository: WalletRepository
    ) : GetWalletWithCardsUseCase {
        return GetWalletWithCardsUseCase(walletRepository)
    }

    @Provides
    fun provideSaveWalletUseCase(
        walletRepository: WalletRepository
    ) : SaveWalletUseCase {
        return SaveWalletUseCase(walletRepository)
    }

    @Provides
    fun provideUpdateWalletUseCase(
        walletRepository: WalletRepository
    ) : UpdateWalletUseCase {
        return UpdateWalletUseCase(walletRepository)
    }

    @Provides
    fun provideDeleteWalletUseCase(
        walletRepository: WalletRepository
    ) : DeleteWalletUseCase {
        return DeleteWalletUseCase(walletRepository)
    }

    @Provides
    fun provideSaveWalletCardUseCase(
        walletCardRepository: WalletCardRepository
    ) : SaveWalletCardUseCase {
        return SaveWalletCardUseCase(walletCardRepository)
    }

    @Provides
    fun provideUpdateWalletCardUseCase(
        walletCardRepository: WalletCardRepository
    ) : UpdateWalletCardUseCase {
        return UpdateWalletCardUseCase(walletCardRepository)
    }

    @Provides
    fun provideDeleteWalletCardUseCase(
        walletCardRepository: WalletCardRepository
    ) : DeleteWalletCardUseCase {
        return DeleteWalletCardUseCase(walletCardRepository)
    }

}