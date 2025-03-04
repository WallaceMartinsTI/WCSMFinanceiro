package com.wcsm.wcsmfinanceiro.data.local.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.local.entity.converter.BillConverter
import com.wcsm.wcsmfinanceiro.data.local.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.local.database.dao.WalletDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class WalletCardDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var walletDao: WalletDao
    private lateinit var walletCardDao: WalletCardDao

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
        walletCardDao = wcsmFinanceiroDatabase.walletCardDao

        // Inserting a Wallet because a WalletCard needs to have a Wallet
        walletDao.saveWallet(Wallet(1, "Nubank", 125.32))
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }

    @Test
    fun saveWalletCard_saveWalletCardInRoomDB_returnWalletCardId() {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: The wallet card is saved in the Room database
        val walletCardId = walletCardDao.saveWalletCard(walletCard)

        // THEN: The returned wallet ID should be grater than 0, indicating a successful save
        assertThat(walletCardId).isGreaterThan(0L)
    }

    @Test
    fun saveWalletCard_saveWalletInRoomDB_returnsValidId() {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: The wallet card is saved in the Room database
        val walletCardId = walletCardDao.saveWalletCard(walletCard)

        // THEN: The returned wallet ID should be greater than 0, indicating a successful save
        assertThat(walletCardId).isGreaterThan(0L)
    }

    @Test
    fun saveWalletCard_saveDuplicatedWalletCard_shouldThrowSQLiteConstraintException() {
        // GIVEN: Multiple wallet card saved in the database
        val walletCard1 = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        val walletCard2 = WalletCard(
            walletId = 1, // Using the same ID to test unique key violation
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: Trying to save two wallets with the same ID
        val exception = try {
            walletCardDao.saveWalletCard(walletCard1)

            // This should fail due to the unique key violation
            walletCardDao.saveWalletCard(walletCard2)
            null
        } catch (e: Exception) {
            e
        }

        // THEN: Check that an exception was thrown
        assertThat(exception).isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun updateWalletCard_updateWalletCardInRoomDB_returnsAffectedRowCount() = runTest {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)

        // WHEN: The wallet card title is updated
        val rowsAffected = walletCardDao.updateWalletCard(walletCard.copy(title = "Debit Card"))

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The wallet card should be correctly updated in the database
        walletDao.selectAllWalletWithCards().test {
            val updatedWallet = awaitItem().find { it.walletCards[0].walletCardId == walletCard.walletCardId }
            assertThat(updatedWallet?.walletCards?.get(0)?.title).isEqualTo("Debit Card")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWalletCard_updateNonExistentWalletCard_shouldReturnZero() = runTest {
        // GIVEN: A non-existent wallet card (ID 999 doesn't exist)
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 999,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: The wallet title is updated
        val rowsAffected = walletCardDao.updateWalletCard(walletCard)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun updateWalletCard_updateMultipleFields_shouldUpdateCorrectly() = runTest {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)

        // WHEN: Multiple fields are updated
        val updatedWalletCard = walletCard.copy(title = "Debit Card", spent = 1500.50, blocked =  true)
        val rowsAffected = walletCardDao.updateWalletCard(updatedWalletCard)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The wallet should be correctly updated in the database
        walletDao.selectAllWalletWithCards().test {
            val walletInDb = awaitItem().find { it.walletCards[0].walletCardId == updatedWalletCard.walletCardId }
            assertThat(walletInDb?.walletCards?.get(0)?.title).isEqualTo("Debit Card")
            assertThat(walletInDb?.walletCards?.get(0)?.spent).isEqualTo(1500.50)
            assertThat(walletInDb?.walletCards?.get(0)?.blocked).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateWalletCard_updateAndRetrieveWalletCard_shouldMatchUpdatedData() = runTest {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)

        // WHEN: Multiple fields are updated
        val updatedWallet = walletCard.copy(title = "Debit Card", spent = 125.90, blocked = true)
        walletCardDao.updateWalletCard(updatedWallet)

        walletDao.selectAllWalletWithCards().test {
            val walletInDb = awaitItem().find { it.walletCards[0].walletCardId == updatedWallet.walletCardId }
            assertThat(walletInDb?.walletCards?.get(0)?.title).isEqualTo("Debit Card")
            assertThat(walletInDb?.walletCards?.get(0)?.spent).isEqualTo(125.90)
            assertThat(walletInDb?.walletCards?.get(0)?.blocked).isTrue()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteWalletCardInRoomDB_returnsAffectedRowCount() {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)

        // WHEN: The wallet is deleted from the database
        val rowsAffected = walletCardDao.deleteWalletCard(walletCard)

        // THEN: The delete operation should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)
    }

    @Test
    fun deleteWalletCard_deleteWalletCardInRoomDB_returnEmptyList() = runTest {
        // GIVEN: A wallet card is created
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)



        // WHEN & THEN: The wallet card list should contain the wallet before deletion, then be empty after deletion
        walletDao.selectAllWalletWithCards().test {
            // The wallet card should be present in the database
            assertThat(awaitItem()[0].walletCards[0]).isEqualTo(walletCard)

            // Delete the wallet card
            walletCardDao.deleteWalletCard(walletCard)

            // The database should be empty after deletion
            assertThat(awaitItem()[0].walletCards).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteNonExistentWalletCard_returnEmptyList() {
        // GIVEN: A wallet card that was never saved in the database
        val nonExistentWalletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // WHEN: Attempting to delete a wallet card that doesn't exist
        val rowsAffected = walletCardDao.deleteWalletCard(nonExistentWalletCard)

        // THEN: The delete operation should affect at least one row
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun deleteWalletCard_deleteMultipleWalletCards_shouldReturnEmptyList() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val walletCard1 = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        val walletCard2 = WalletCard(
            walletId = 1,
            walletCardId = 2,
            title = "Debit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet cards in the database
        walletCardDao.saveWalletCard(walletCard1)
        walletCardDao.saveWalletCard(walletCard2)

        // WHEN: Deleting both wallet cards
        walletCardDao.deleteWalletCard(walletCard1)
        walletCardDao.deleteWalletCard(walletCard2)

        // THEN: The database should be empty
        walletDao.selectAllWalletWithCards().test {
            assertThat(awaitItem()[0].walletCards).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteOneWalletCard_shouldNotAffectOthers() = runTest {
        // GIVEN: Multiple wallet saved in the database
        val walletCard1 = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        val walletCard2 = WalletCard(
            walletId = 1,
            walletCardId = 2,
            title = "Debit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet cards in the database
        walletCardDao.saveWalletCard(walletCard1)
        walletCardDao.saveWalletCard(walletCard2)

        // WHEN: Deleting only walletCard1
        walletCardDao.deleteWalletCard(walletCard1)

        // THEN: The database should be empty
        walletDao.selectAllWalletWithCards().test {
            assertThat(awaitItem()[0].walletCards[0]).isEqualTo(walletCard2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteWalletCard_deleteSameWalletCardTwice_shouldReturnZeroOnSecondDelete() {
        // GIVEN: Multiple wallet saved in the database
        val walletCard = WalletCard(
            walletId = 1,
            walletCardId = 1,
            title = "Credit Card",
            limit = 2000.0,
            spent = 1000.0,
            available = 1000.0,
            blocked = false
        )

        // Saving the wallet card in the database
        walletCardDao.saveWalletCard(walletCard)

        // WHEN: Deleting the wallet twice
        val firstDelete = walletCardDao.deleteWalletCard(walletCard)
        val secondDelete = walletCardDao.deleteWalletCard(walletCard)

        // THEN: The first delete should affect at least one row
        assertThat(firstDelete).isGreaterThan(0)

        // AND: The second delete should return 0 since the wallet is already deleted
        assertThat(secondDelete).isEqualTo(0)
    }
}