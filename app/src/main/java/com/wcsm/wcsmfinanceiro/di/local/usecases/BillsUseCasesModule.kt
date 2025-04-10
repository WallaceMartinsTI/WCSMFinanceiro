package com.wcsm.wcsmfinanceiro.di.local.usecases

import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.bills.UpdateBillUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BillsUseCasesModule {

    @Provides
    fun provideGetBillsUseCase(
        billsRepository: BillsRepository
    ): GetBillsUseCase {
        return GetBillsUseCase(billsRepository)
    }

    @Provides
    fun provideSaveBillUseCase(
        billsRepository: BillsRepository,
        walletRepository: WalletRepository
    ): SaveBillUseCase {
        return SaveBillUseCase(billsRepository, walletRepository)
    }

    @Provides
    fun provideUpdateBillUseCase(
        billsRepository: BillsRepository,
        walletRepository: WalletRepository
    ): UpdateBillUseCase {
        return UpdateBillUseCase(billsRepository, walletRepository)
    }

    @Provides
    fun provideDeleteBillUseCase(
        billsRepository: BillsRepository,
        walletRepository: WalletRepository
    ): DeleteBillUseCase {
        return DeleteBillUseCase(billsRepository, walletRepository)
    }

    @Provides
    fun provideGetBillsByDateUseCase(
        billsRepository: BillsRepository
    ): GetBillsByDateUseCase {
        return GetBillsByDateUseCase(billsRepository)
    }

    @Provides
    fun provideGetBillsByTextUseCase(
        billsRepository: BillsRepository
    ): GetBillsByTextUseCase {
        return GetBillsByTextUseCase(billsRepository)
    }

}