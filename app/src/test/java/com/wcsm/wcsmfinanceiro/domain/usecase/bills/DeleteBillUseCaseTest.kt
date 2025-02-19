package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteBillUseCaseTest {

    @Mock
    private lateinit var billsRepository: BillsRepository

    private lateinit var deleteBillUseCase: DeleteBillUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        deleteBillUseCase = DeleteBillUseCase(billsRepository)
    }
}