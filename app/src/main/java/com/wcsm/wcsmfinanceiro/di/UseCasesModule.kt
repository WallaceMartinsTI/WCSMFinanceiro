package com.wcsm.wcsmfinanceiro.di

import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.usecase.DeleteBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsByDateUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsByTextUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.GetBillsUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.SaveBillUseCase
import com.wcsm.wcsmfinanceiro.domain.usecase.UpdateBillUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetBillsUseCase(
        billsRepository: BillsRepository
    ) : GetBillsUseCase {
        return GetBillsUseCase(billsRepository)
    }

    @Provides
    fun provideSaveBillUseCase(
        billsRepository: BillsRepository
    ) : SaveBillUseCase {
        return SaveBillUseCase(billsRepository)
    }

    @Provides
    fun provideUpdateBillUseCase(
        billsRepository: BillsRepository
    ) : UpdateBillUseCase {
        return UpdateBillUseCase(billsRepository)
    }

    @Provides
    fun provideDeleteBillUseCase(
        billsRepository: BillsRepository
    ) : DeleteBillUseCase {
        return DeleteBillUseCase(billsRepository)
    }

    @Provides
    fun provideGetBillsByDateUseCase(
        billsRepository: BillsRepository
    ) : GetBillsByDateUseCase {
        return GetBillsByDateUseCase(billsRepository)
    }

    @Provides
    fun provideGetBillsByTextUseCase(
        billsRepository: BillsRepository
    ) : GetBillsByTextUseCase {
        return GetBillsByTextUseCase(billsRepository)
    }

}