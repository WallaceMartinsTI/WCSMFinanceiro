package com.wcsm.wcsmfinanceiro.data.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.converter.BillConverter
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class WalletDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var walletDao: WalletDao

    @Before
    fun setUp() {
        wcsmFinanceiroDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WCSMFinanceiroDatabase::class.java,
        )
            .addTypeConverter(BillConverter())
            .allowMainThreadQueries()
            .build()

        walletDao = wcsmFinanceiroDatabase.walletDao
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }

    @Test
    fun saveWallet_saveWalletInRoomDB_returnWalletId() {
        // GIVEN: A wallet is created
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: The wallet is saved in the Room database
        val walletId = walletDao.saveWallet(wallet)

        // THEN: The returned wallet ID should match the original wallet ID
        assertThat(walletId).isEqualTo(wallet.walletId)
    }

    @Test
    fun saveWallet_saveWalletInRoomDB_retursValidId() {
        // GIVEN: A wallet is created
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: The wallet is saved in the Room database
        val walletId = walletDao.saveWallet(wallet)

        // THEN: The returned wallet ID should be greater than -, indicating a successful save
        assertThat(walletId).isGreaterThan(0L)
    }

    @Test
    fun saveWallet_saveDuplicatedWallet_shouldThrowSQLiteConstraintException() {
        // GIVEN: Creating a bill with valid data
        val wallet1 = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        val wallet2 = Wallet(
            walletId = 1, // Using the same ID to test unique key violation
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: Trying to save two bills with the same ID
        val exception = try {
            walletDao.saveWallet(wallet1)

            // This should fail due to the unique key violation
            walletDao.saveWallet(wallet2)
            null
        } catch (e: Exception) {
            e
        }

        // THEN: Check that an exception was thrown
        assertThat(exception).isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun updateWallet_updateWalletInRoomDB_returnsAffectedRowCount() = runTest {
        // GIVEN: Creating a bill with valid data and saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the bill in the database
        walletDao.saveWallet(wallet)

        // WHEN: The wallet title is updated
        val rowsAffected = walletDao.updateWallet(wallet.copy(title = "Nubank Atualizado"))

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The bill should be correctly updated in the database
        walletDao.selectAllWalletWithCards().test {
            val updatedWallet = awaitItem().find { it.wallet.walletId == wallet.walletId }
            assertThat(updatedWallet?.wallet?.title).isEqualTo("Nubank Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

}