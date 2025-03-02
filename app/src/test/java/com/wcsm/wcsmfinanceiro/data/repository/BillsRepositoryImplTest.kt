package com.wcsm.wcsmfinanceiro.data.repository

import android.database.sqlite.SQLiteConstraintException
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import com.wcsm.wcsmfinanceiro.domain.model.DatabaseResponse
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BillsRepositoryImplTest {

    @Mock
    private lateinit var billsDao: BillsDao

    private lateinit var billsRepository: BillsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        billsRepository = BillsRepositoryImpl(billsDao)
    }

    @Test
    fun saveBill_saveBillWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 5,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.saveBill(bill)).thenReturn(bill.billId)


        // WHEN: Trying to save the bill
        billsRepository.saveBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(bill.billId)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveBill_saveBillNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.saveBill(bill)).thenReturn(0)

        // WHEN: Trying to save the bill
        billsRepository.saveBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro ao salvar conta.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveBill_saveBillTwice_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be saved
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.saveBill(bill)).thenThrow(SQLiteConstraintException())

        // WHEN: Trying to save the bill
        billsRepository.saveBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro desconhecido ao salvar conta, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBill_updateBillWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be updated
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.updateBill(bill)).thenReturn(1)


        // WHEN: Trying to update the bill
        billsRepository.updateBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response with rows affected
            val rowsAffected = (awaitItem() as DatabaseResponse.Success).data
            assertThat(rowsAffected).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBill_updateBillNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be updated
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.updateBill(bill)).thenReturn(0)

        // WHEN: Trying to update the bill
        billsRepository.updateBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro ao atualizar conta.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteBillWithSuccess_shouldEmitSuccessResponse() = runTest {
        // GIVEN: A bill to be deleted
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.deleteBill(bill)).thenReturn(1)

        // WHEN: Trying to update the bill
        billsRepository.deleteBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(1)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteBillNoRowsAffected_shouldEmitErrorResponse() = runTest {
        // GIVEN: A bill to be deleted
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        Mockito.`when`(billsDao.deleteBill(bill)).thenReturn(0)

        // WHEN: Trying to update the bill
        billsRepository.deleteBill(bill).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro ao deletar conta.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBills_getBillsWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedBills = listOf(
            Bill(
                billId = 1,
                billType = BillType.INCOME,
                origin = "Origem Teste",
                title = "Conta Teste",
                value = 2578.92,
                description = "Descrição Teste",
                date = 1737504000000,
                paymentType = PaymentType.MONEY,
                paid = true,
                dueDate = 1737804000000,
                expired = false,
                category = "Lazer",
                tags = listOf("lazer", "casa", "construção")
            ),
            Bill(
                billId = 2,
                billType = BillType.EXPENSE,
                origin = "Origem Teste2",
                title = "Conta Teste2",
                value = 1512.74,
                description = "Descrição Teste2",
                date = 1737504000000,
                paymentType = PaymentType.CARD,
                paid = false,
                dueDate = 1737804000000,
                expired = false,
                category = "Lazer",
                tags = listOf("lazer", "casa", "construção")
            ),
        )
        Mockito.`when`(billsDao.selectAllBills()).thenReturn(
            flow {
                emit(expectedBills)
            }
        )

        // GIVEN & WHEN: A request to select all bills
        billsRepository.getBills().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response with the bills list
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(expectedBills)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBills_getBillsWithFailure_shouldEmitErrorResponse() = runTest {
        Mockito.`when`(billsDao.selectAllBills()).thenReturn(
            flow {
                throw RuntimeException("Erro ao acessar banco de dados.")
            }
        )

        // GIVEN & WHEN: A request to select all bills
        billsRepository.getBills().test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro desconhecido ao buscar contas, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsByDate_getBillsByDateWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedBills = listOf(
            Bill(
                billId = 2,
                billType = BillType.EXPENSE,
                origin = "Origem Teste2",
                title = "Conta Teste2",
                value = 1512.74,
                description = "Descrição Teste2",
                date = 1737504000000,
                paymentType = PaymentType.CARD,
                paid = false,
                dueDate = 1737804000000,
                expired = false,
                category = "Lazer",
                tags = listOf("lazer", "casa", "construção")
            )
        )
        Mockito.`when`(billsDao.selectBillsByDate(anyLong(), anyLong())).thenReturn(
            flow {
                emit(expectedBills)
            }
        )

        // GIVEN & WHEN: A request to select all bills by date
        billsRepository.getBillsByDate(1737504000000, 1737804000000).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response with the filtered bills list
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(expectedBills)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsByDate_getBillsWithFailure_shouldEmitErrorResponse() = runTest {
        Mockito.`when`(billsDao.selectBillsByDate(anyLong(), anyLong())).thenReturn(
            flow {
                throw RuntimeException("Erro ao acessar banco de dados.")
            }
        )

        // GIVEN & WHEN: A request to select all bills
        billsRepository.getBillsByDate(1737504000000, 1737804000000).test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro desconhecido ao buscar contas pela data, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsByText_getBillsByTextWithSuccess_shouldEmitSuccessResponse() = runTest {
        val expectedBills = listOf(
            Bill(
                billId = 2,
                billType = BillType.EXPENSE,
                origin = "Origem Teste2",
                title = "Conta Teste2",
                value = 1512.74,
                description = "Descrição Teste2",
                date = 1737504000000,
                paymentType = PaymentType.CARD,
                paid = false,
                dueDate = 1737804000000,
                expired = false,
                category = "Lazer",
                tags = listOf("lazer", "casa", "construção")
            )
        )
        Mockito.`when`(billsDao.selectBillsByText(anyString())).thenReturn(
            flow {
                emit(expectedBills)
            }
        )

        // GIVEN & WHEN: A request to select all bills by date
        billsRepository.getBillsByText("Teste2").test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit a success response with the filtered bills list
            assertThat((awaitItem() as DatabaseResponse.Success).data).isEqualTo(expectedBills)

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getBillsByText_getBillsByTextWithFailure_shouldEmitErrorResponse() = runTest {
        Mockito.`when`(billsDao.selectBillsByText(anyString())).thenReturn(
            flow {
                throw RuntimeException("Erro ao acessar banco de dados.")
            }
        )

        // GIVEN & WHEN: A request to select all bills
        billsRepository.getBillsByText("Teste2").test {
            // THEN: Repository should emit Loading at first
            assertThat(awaitItem()).isInstanceOf(DatabaseResponse.Loading::class.java)

            // AND THEN: It should emit an error response
            assertThat((awaitItem() as DatabaseResponse.Error).message).isEqualTo("Erro desconhecido ao buscar contas pelo texto, informe o administrador.")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }
}