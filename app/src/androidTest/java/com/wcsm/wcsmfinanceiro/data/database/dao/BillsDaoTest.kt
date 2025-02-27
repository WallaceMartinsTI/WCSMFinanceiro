package com.wcsm.wcsmfinanceiro.data.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.entity.converter.BillConverter
import com.wcsm.wcsmfinanceiro.data.model.BillType
import com.wcsm.wcsmfinanceiro.data.model.PaymentType
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class BillsDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var billsDao: BillsDao

    @Before
    fun setUp() {
        wcsmFinanceiroDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WCSMFinanceiroDatabase::class.java,
        )
            .addTypeConverter(BillConverter())
            .allowMainThreadQueries()
            .build()

        billsDao = wcsmFinanceiroDatabase.billsDao
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }

    @Test
    fun saveBill_saveBillInRoomDB_returnBillId() {
        // GIVEN: A bill is created
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

        // WHEN: The bill is saved in the Room database
        val billId = billsDao.saveBill(bill)

        // THEN: The returned bill ID should match the original bill ID
        assertThat(billId).isEqualTo(bill.billId)
    }

    @Test
    fun saveBill_saveBillInRoomDB_returnsValidId() {
        // GIVEN: A bill is created
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

        // WHEN: The bill is saved in the Room database
        val billId = billsDao.saveBill(bill)

        // THEN: The returned bill ID should be greater than 0, indicating a successful save
        assertThat(billId).isGreaterThan(0L)
    }

    @Test
    fun saveBill_saveDuplicatedBill_shouldThrowSQLiteConstraintException() {
        // GIVEN: Creating a bill with valid data
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 1,  // Using the same ID to test unique key violation
            billType = BillType.EXPENSE,
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

        // WHEN: Trying to save two bills with the same ID
        val exception = try {
            billsDao.saveBill(bill1)

            // This should fail due to the unique key violation
            billsDao.saveBill(bill2)
            null
        } catch (e: Exception) {
            e
        }

        // THEN: Check that an exception was thrown
        assertThat(exception).isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun updateBill_updateBillInRoomDB_returnsAffectedRowCount() = runTest {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)


        // WHEN: The bill title is updated
        val rowsAffected = billsDao.updateBill(bill.copy(title = "Conta Atualizada"))

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The bill should be correctly updated in the database
        billsDao.selectAllBills().test {
            val updatedBill = awaitItem().find { it.billId == bill.billId }
            assertThat(updatedBill?.title).isEqualTo("Conta Atualizada")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBill_updateNonExistentBill_shouldReturnZero() = runTest {
        // GIVEN: A non-existent bill (ID 999 doesn't exist)
        val bill = Bill(
            billId = 999,
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

        // WHEN: Attempting to update the non-existent bill
        val rowsAffected = billsDao.updateBill(bill)

        // THEN: No rows should be affected
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun updateBill_updateMultipleFields_shouldUpdateCorrectly() = runTest {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: Multiple fields are updated
        val updatedBill = bill.copy(title = "Updated Bill", value = 3000.00, paid = false)
        val rowsAffected = billsDao.updateBill(updatedBill)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The bill should be correctly updated in the database
        billsDao.selectAllBills().test {
            val billInDb = awaitItem().find { it.billId == updatedBill.billId }
            assertThat(billInDb?.title).isEqualTo("Updated Bill")
            assertThat(billInDb?.value).isEqualTo(3000.00)
            assertThat(billInDb?.paid).isFalse()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateBill_updateAndRetrieveBill_shouldMatchUpdatedData() = runTest {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: The bill is updated
        val updatedBill = bill.copy(title = "Updated Bill")
        billsDao.updateBill(updatedBill)

        // THEN: The updated bill should match the data in the database
        billsDao.selectAllBills().test {
            val billInDb = awaitItem().find { it.billId == updatedBill.billId }
            assertThat(billInDb?.title).isEqualTo("Updated Bill")

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteBillInRoomDB_returnsAffectedRowCount() {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: The bill is deleted from the database
        val rowsAffected = billsDao.deleteBill(bill)

        // THEN: The delete operation should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)
    }

    @Test
    fun deleteBill_deleteBillInRoomDB_returnEmptyList() = runTest {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN & THEN: The bill list should contain the bill before deletion, then be empty after deletion
        billsDao.selectAllBills().test {
            // The bill should be present in the database
            assertThat(awaitItem()).containsExactly(bill)

            // Delete the bill
            billsDao.deleteBill(bill)

            // The database should be empty after deletion
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteNonExistentBill_shouldReturnZero() = runTest {
        // GIVEN: A bill that was never saved in the database
        val nonExistentBill = Bill(
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

        // WHEN: Attempting to delete a bill that doesn't exist
        val rowsAffected = billsDao.deleteBill(nonExistentBill)

        // THEN: No rows should be affected
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun deleteBill_deleteMultipleBills_shouldReturnEmptyList() = runTest {
        // GIVEN: Multiple bills saved in the database
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Deleting both bills
        billsDao.deleteBill(bill1)
        billsDao.deleteBill(bill2)

        // THEN: The database should be empty
        billsDao.selectAllBills().test {
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteOneBill_shouldNotAffectOthers() = runTest {
        // GIVEN: Multiple bills saved in the database
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Deleting only bill1
        billsDao.deleteBill(bill1)

        // THEN: Bill2 should still be in the database
        billsDao.selectAllBills().test {
            assertThat(awaitItem()).containsExactly(bill2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteBill_deleteSameBillTwice_shouldReturnZeroOnSecondDelete() = runTest {
        // GIVEN: A bill is created and saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: Deleting the bill twice
        val firstDelete = billsDao.deleteBill(bill)
        val secondDelete = billsDao.deleteBill(bill)

        // THEN: The first delete should affect at least one row
        assertThat(firstDelete).isGreaterThan(0)

        // AND: The second delete should return 0 since the bill is already deleted
        assertThat(secondDelete).isEqualTo(0)
    }

    @Test
    fun selectAllBills_selectingAllBills_shouldReturnAListOfBills() = runTest {
        // GIVEN: Multiple bills saved in the database
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Selecting all bills from the database
        billsDao.selectAllBills().test {
            // THEN: Should not return a empty list
            val billsList = awaitItem()
            assertThat(billsList).isNotEmpty()

            // AND: Should match with size of 2 (2 bills saved in database)
            assertThat(billsList.size).isEqualTo(2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllBills_emptyDatabase_shouldReturnEmptyList() = runTest {
        // GIVEN: No bills saved in the database

        // WHEN: Selecting all bills
        billsDao.selectAllBills().test {
            // THEN: The list should be empty
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllBills_oneBillSaved_shouldReturnSingleBill() = runTest {
        // GIVEN: A single bill saved in the database
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

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: Selecting all bills
        billsDao.selectAllBills().test {
            // THEN: The list should contain exactly one bill
            val billsList = awaitItem()
            assertThat(billsList).containsExactly(bill)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllBills_deleteBill_shouldNotReturnDeletedBill() = runTest {
        // GIVEN: A bill is saved and then deleted
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

        // Saving the bills in the database
        billsDao.saveBill(bill)
        billsDao.deleteBill(bill)

        // WHEN: Selecting all bills
        billsDao.selectAllBills().test {
            // THEN: The deleted bill should not be in the list
            assertThat(awaitItem()).doesNotContain(bill)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllBills_multipleBills_shouldMaintainInsertionOrder() = runTest {
        // GIVEN: Multiple bills inserted in a specific order
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Selecting all bills
        billsDao.selectAllBills().test {
            // THEN: The order should be the same as insertion order
            val billsList = awaitItem()
            assertThat(billsList).containsExactly(bill1, bill2).inOrder()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectBillsByDate_validRange_shouldReturnMatchingBills() = runTest {
        // GIVEN: Multiple bills with different dates
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737604000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        val bill3 = Bill(
            billId = 3,
            billType = BillType.INCOME,
            origin = "Origem Teste 3",
            title = "Conta Teste 3",
            value = 2578.92,
            description = "Descrição Teste 3",
            date = 1737704000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)
        billsDao.saveBill(bill3)

        // WHEN: Selecting bills in a specific date range
        val startDate = 1737500000000
        val endDate = 1737605000000

        billsDao.selectBillsByDate(startDate, endDate).test {
            // THEN: Should return only bills inside the date range
            assertThat(awaitItem()).containsExactly(bill1, bill2).inOrder()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectBillsByDate_noMatchingBills_shouldReturnEmptyList() = runTest {
        // GIVEN: A bill outside the search range
        val bill = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta Teste",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1738000000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1738100000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bill in the database
        billsDao.saveBill(bill)

        // WHEN: Searching for bills within a date range that excludes the bill
        val startDate = 1737500000000
        val endDate = 1737600000000

        billsDao.selectBillsByDate(startDate, endDate).test {
            // THEN: Should return an empty list
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectBillsByDate_largeRange_shouldReturnAllBills() = runTest {
        // GIVEN: Multiple bills with different dates
        val bill1 = Bill(
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

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Origem Teste 2",
            title = "Conta Teste 2",
            value = 2578.92,
            description = "Descrição Teste 2",
            date = 1737604000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Selecting bills with a very large date range
        val startDate = 1737000000000
        val endDate = 1739000000000

        billsDao.selectBillsByDate(startDate, endDate).test {
            // THEN: Should return all bills
            assertThat(awaitItem()).containsExactly(bill1, bill2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectBillsByText_matchingBills_shouldReturnCorrectResults() = runTest {
        // GIVEN: Multiple bills with different fields containing the searched text
        val bill1 = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta 1",
            value = 2578.92,
            description = "Descrição 1",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Padaria",
            title = "Café da Manhã",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737604000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Searching for bills that contain "food"
        billsDao.selectBillsByText("Padaria").test {
            // THEN: Should return only the bill containing "food" in the description
            assertThat(awaitItem()).containsExactly(bill2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectBillsByText_noMatchingBills_shouldReturnEmptyList() = runTest {
        // GIVEN: A bill that does not match the search query
        val bill1 = Bill(
            billId = 1,
            billType = BillType.INCOME,
            origin = "Origem Teste",
            title = "Conta 1",
            value = 2578.92,
            description = "Descrição 1",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        val bill2 = Bill(
            billId = 2,
            billType = BillType.INCOME,
            origin = "Padaria",
            title = "Café da Manhã",
            value = 2578.92,
            description = "Descrição Teste",
            date = 1737604000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Lazer",
            tags = listOf("lazer", "casa", "construção")
        )

        // Saving the bills in the database
        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Searching for a text that does not exist in any field
        billsDao.selectBillsByText("Viagem").test {
            // THEN: Should return an empty list
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }
}