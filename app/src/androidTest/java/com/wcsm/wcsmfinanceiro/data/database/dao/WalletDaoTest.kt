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
    fun saveWallet_saveWalletInRoomDB_returnsValidId() {
        // GIVEN: A wallet is created
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: The wallet is saved in the Room database
        val walletId = walletDao.saveWallet(wallet)

        // THEN: The returned wallet ID should be greater than 0, indicating a successful save
        assertThat(walletId).isGreaterThan(0L)
    }

    @Test
    fun saveWallet_saveDuplicatedWallet_shouldThrowSQLiteConstraintException() {
        // GIVEN: Multiple wallet saved in the database
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

        // WHEN: Trying to save two wallets with the same ID
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
        // GIVEN: Creating a wallet with valid data and saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: The wallet title is updated
        val rowsAffected = walletDao.updateWallet(wallet.copy(title = "Nubank Atualizado"))

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The wallet should be correctly updated in the database
        walletDao.selectAllWalletWithCards().test {
            val updatedWallet = awaitItem().find { it.wallet.walletId == wallet.walletId }
            assertThat(updatedWallet?.wallet?.title).isEqualTo("Nubank Atualizado")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWallet_updateNonExistentWallet_shouldReturnZero() = runTest {
        // GIVEN: A non-existent wallet (ID 999 doesn't exist)
        val wallet = Wallet(
            walletId = 999,
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: The wallet title is updated
        val rowsAffected = walletDao.updateWallet(wallet)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun updateWallet_updateMultipleFields_shouldUpdateCorrectly() = runTest {
        // GIVEN: A wallet is created and saved in the database
        val wallet = Wallet(
            walletId = 999,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: Multiple fields are updated
        val updatedWallet = wallet.copy(title = "Updated Wallet", balance = 25.31)
        val rowsAffected = walletDao.updateWallet(updatedWallet)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The wallet should be correctly updated in the database
        walletDao.selectAllWalletWithCards().test {
            val walletInDb = awaitItem().find { it.wallet.walletId == updatedWallet.walletId }
            assertThat(walletInDb?.wallet?.title).isEqualTo("Updated Wallet")
            assertThat(walletInDb?.wallet?.balance).isEqualTo(25.31)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWallet_updateAndRetrieveWallet_shouldMatchUpdatedData() = runTest {
        // GIVEN: A wallet is created and saved in the database
        val wallet = Wallet(
            walletId = 999,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: Multiple fields are updated
        val updatedWallet = wallet.copy(title = "Updated Wallet", balance = 25.31)
        val rowsAffected = walletDao.updateWallet(updatedWallet)

        walletDao.selectAllWalletWithCards().test {
            val walletInDb = awaitItem().find { it.wallet.walletId == updatedWallet.walletId }
            assertThat(walletInDb?.wallet?.title).isEqualTo("Updated Wallet")

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteWalletInRoomDB_returnsAffectedRowCount() {
        // GIVEN: A wallet is created and saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: The wallet is deleted from the database
        val rowsAffected = walletDao.deleteWallet(wallet)

        // THEN: The delete operation should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)
    }

    @Test
    fun deleteWallet_deleteWalletInRoomDB_returnEmptyList() = runTest {
        // GIVEN: A wallet is created and saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN & THEN: The wallet list should contain the wallet before deletion, then be empty after deletion
        walletDao.selectAllWalletWithCards().test {
            // The wallet should be present in the datanase
            assertThat(awaitItem()[0].wallet).isEqualTo(wallet)

            // Delete the wallet
            walletDao.deleteWallet(wallet)

            // The datanase should be empty after deletion
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteNonExistentWallet_returnEmptyList() = runTest {
        // GIVEN: A wallet that was never saved in the database
        val nonExistentWallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // WHEN: Attempting to delete a wallet that doesn't exist
        val rowsAffected = walletDao.deleteWallet(nonExistentWallet)

        // THEN: No rows should be affected
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun deleteWallet_deleteMultipleWallets_shouldReturnEmptyList() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val wallet1 = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        val wallet2 = Wallet(
            walletId = 2,
            title = "Inter",
            balance = 412.89
        )

        // Saving the wallets in the database
        walletDao.saveWallet(wallet1)
        walletDao.saveWallet(wallet2)

        // WHEN: Deleting both wallets
        walletDao.deleteWallet(wallet1)
        walletDao.deleteWallet(wallet2)

        // THEN: The database should be empty
        walletDao.selectAllWalletWithCards().test {
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteOneWallet_shouldNotAffectOthers() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val wallet1 = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        val wallet2 = Wallet(
            walletId = 2,
            title = "Inter",
            balance = 412.89
        )

        // Saving the wallets in the database
        walletDao.saveWallet(wallet1)
        walletDao.saveWallet(wallet2)

        // WHEN: Deleting only wallet1
        walletDao.deleteWallet(wallet1)

        // THEN: The database should be empty
        walletDao.selectAllWalletWithCards().test {
            assertThat(awaitItem()[0].wallet).isEqualTo(wallet2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWallet_deleteSameWalletTwice_shouldReturnZeroOnSecondDelete() = runTest {
        // GIVEN: A wallet is created and saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: Deleting the wallet twice
        val firstDelete = walletDao.deleteWallet(wallet)
        val secondDelete = walletDao.deleteWallet(wallet)

        // THEN: The first delete should affect at least one row
        assertThat(firstDelete).isGreaterThan(0)

        // AND: The second delete should return - since the wallet is already deleted
        assertThat(secondDelete).isEqualTo(0)
    }

    @Test
    fun selectAllWalletWithCards_selectingAllWalletWithCards_shouldReturnAListOfWalletWithCards() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val wallet1 = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        val wallet2 = Wallet(
            walletId = 2,
            title = "Inter",
            balance = 412.89
        )

        // Saving the wallets in the database
        walletDao.saveWallet(wallet1)
        walletDao.saveWallet(wallet2)

        // WHEN: Selecting all walletWithCards from the database
        walletDao.selectAllWalletWithCards().test {
            // THEN: Should not return a empty list
            val walletWithCardsList = awaitItem()
            assertThat(walletWithCardsList).isNotEmpty()

            // AND: Should match with size of 2 (2 wallets saved in database)
            assertThat(walletWithCardsList.size).isEqualTo(2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllWalletWithCards_emptyDatabase_shouldReturnEmptyList() = runTest {
        // GIVEN: No wallets saved in the database

        // WHEN: Selecting all wallets
        walletDao.selectAllWalletWithCards().test {
            // THEN: The list should be empty
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllWalletWithCards_oneWalletSaved_shouldReturnSingleWallet() = runTest {
        // GIVEN: A single wallet saved in the database
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)

        // WHEN: Selecting all wallets
        walletDao.selectAllWalletWithCards().test {
            // THEN: The list should contain exactly one wallet
            val walletWithCardsList = awaitItem()
            assertThat(walletWithCardsList[0].wallet).isEqualTo(wallet)// containsExactly(wallet)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllWalletWithCards_deleteWallet_shouldNotReturnDeletedWallet() = runTest {
        // GIVEN: A wallet is saved and then deleted
        val wallet = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        // Saving the wallet in the database
        walletDao.saveWallet(wallet)
        walletDao.deleteWallet(wallet)

        // WHEN: Selecting all wallets
        walletDao.selectAllWalletWithCards().test {
            // THEN: The deleted wallet should not be in the list
            assertThat(awaitItem()).doesNotContain(wallet)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllWalletWithCards_multipleWallets_shouldMaintainInsertionOrder() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val wallet1 = Wallet(
            walletId = 1,
            title = "Nubank",
            balance = 125.32
        )

        val wallet2 = Wallet(
            walletId = 2,
            title = "Inter",
            balance = 412.89
        )

        // Saving the wallets in the database
        walletDao.saveWallet(wallet1)
        walletDao.saveWallet(wallet2)

        // WHEN: Selecting all wallets
        walletDao.selectAllWalletWithCards().test {
            // THEN: The order should be the same as insertion otder
            val walletWithCardsList = awaitItem()
            //assertThat(walletWithCardsList).containsExactly(wallet1, wallet2).inOrder()
            assertThat(walletWithCardsList[0].wallet).isEqualTo(wallet1)
            assertThat(walletWithCardsList[1].wallet).isEqualTo(wallet2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }
}