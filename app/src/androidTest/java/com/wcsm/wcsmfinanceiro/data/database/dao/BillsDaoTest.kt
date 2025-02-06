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
            origin = "Test Origin",
            title = "Test Bill",
            value = 2578.92,
            description = "Test Description",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Leisure",
            tags = listOf("leisure", "home", "construction")
        )

        val bill2 = Bill(
            billId = 1,  // Using the same ID to test unique key violation
            billType = BillType.EXPENSE,
            origin = "Test Origin",
            title = "Test Bill",
            value = 2578.92,
            description = "Test Description",
            date = 1737504000000,
            paymentType = PaymentType.MONEY,
            paid = true,
            dueDate = 1737804000000,
            expired = false,
            category = "Leisure",
            tags = listOf("leisure", "home", "construction")
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

        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Deleting both bills
        billsDao.deleteBill(bill1)
        billsDao.deleteBill(bill2)

        // THEN: The database should be empty
        billsDao.selectAllBills().test {
            assertThat(awaitItem()).isEmpty()
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

        billsDao.saveBill(bill1)
        billsDao.saveBill(bill2)

        // WHEN: Deleting only bill1
        billsDao.deleteBill(bill1)

        // THEN: Bill2 should still be in the database
        billsDao.selectAllBills().test {
            assertThat(awaitItem()).containsExactly(bill2)
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

        billsDao.saveBill(bill)

        // WHEN: Deleting the bill twice
        val firstDelete = billsDao.deleteBill(bill)
        val secondDelete = billsDao.deleteBill(bill)

        // THEN: The first delete should affect at least one row
        assertThat(firstDelete).isGreaterThan(0)

        // AND: The second delete should return 0 since the bill is already deleted
        assertThat(secondDelete).isEqualTo(0)
    }
}