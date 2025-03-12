package com.wcsm.wcsmfinanceiro.data.local.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.wcsm.wcsmfinanceiro.data.local.database.WCSMFinanceiroDatabase
import com.wcsm.wcsmfinanceiro.data.local.entity.Subscription
import com.wcsm.wcsmfinanceiro.data.local.entity.converter.BillConverter
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class SubscriptionDaoTest {
    private lateinit var wcsmFinanceiroDatabase: WCSMFinanceiroDatabase
    private lateinit var subscriptionDao: SubscriptionDao

    @Before
    fun setUp() {
        wcsmFinanceiroDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WCSMFinanceiroDatabase::class.java,
        )
            .addTypeConverter(BillConverter())
            .allowMainThreadQueries()
            .build()

        subscriptionDao = wcsmFinanceiroDatabase.subscriptionDao
    }

    @After
    fun tearDown() {
        if(::wcsmFinanceiroDatabase.isInitialized) {
            wcsmFinanceiroDatabase.close()
        }
    }

    @Test
    fun saveSubscription_saveSubscriptionInRoomDB_returnSubscriptionId() {
        // GIVEN: A subscription is created
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: The subscription is saved in the Room database
        val subscriptionId = subscriptionDao.saveSubscription(subscription)

        // THEN: The returned subscription ID should match the original subscription ID
        assertThat(subscriptionId).isEqualTo(subscription.subscriptionId)
    }

    @Test
    fun saveSubscription_saveSubscriptionInRoomDB_returnsValidId() {
        // GIVEN: A subscription is created
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: The subscription is saved in the Room database
        val subscriptionId = subscriptionDao.saveSubscription(subscription)

        // THEN: The returned subscription ID should be greater than 0, indicating a successful save
        assertThat(subscriptionId).isGreaterThan(0L)
    }

    @Test
    fun saveSubscription_saveSubscriptionInRoomDB_shouldThrowSQLiteConstraintException() {
        // GIVEN: Creating a subscription with valid data
        val subscription1 = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscription2 = Subscription(
            subscriptionId = 1, // Using the same ID to test unique key violation
            title = "Netflix Identica",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: Trying to save two subscriptions with the same ID
        val exception = try {
            subscriptionDao.saveSubscription(subscription1)

            // This should fail due to the unique key violation
            subscriptionDao.saveSubscription(subscription2)
            null
        } catch (e: Exception) {
            e
        }

        // THEN: Check that an exception was thrown
        assertThat(exception).isInstanceOf(SQLiteConstraintException::class.java)
    }

    @Test
    fun updateSubscription_updateSubscriptionInRoomDB_returnsAffectedRowCount() = runTest {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: The subscription title is updated
        val rowsAffected = subscriptionDao.updateSubscription(subscription.copy(title = "Netflix Atualizada"))

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The subscription should be correctly updated in the database
        subscriptionDao.selectAllSubscriptions().test {
            val updatedSubscription = awaitItem().find { it.subscriptionId == subscription.subscriptionId }

            assertThat(updatedSubscription?.title).isEqualTo("Netflix Atualizada")

            // Important: Cancels the flow to prevent coroutine leaks in the test
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscription_updateNonExistentSubscription_shouldReturnZero() = runTest {
        // GIVEN: A non-existent subscription (ID 999 doesn't exist)
        val subscription = Subscription(
            subscriptionId = 999,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: Attempting to update the non-existent subscription
        val rowsAffected = subscriptionDao.updateSubscription(subscription)

        // THEN: No rows should be affected
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun updateSubscription_updateMultipleFields_shouldUpdateCorrectly() = runTest {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: The subscription title is updated
        val updatedSubscription = subscription.copy(
            title = "Netflix Atualizada",
            price = 250.50,
            expired = true,
            automaticRenewal = false
        )
        val rowsAffected = subscriptionDao.updateSubscription(updatedSubscription)

        // THEN: The update should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)

        // AND: The subscription should be correctly updated in the database
        subscriptionDao.selectAllSubscriptions().test {
            val subscriptionInDb = awaitItem().find { it.subscriptionId == updatedSubscription.subscriptionId }
            assertThat(subscriptionInDb?.title).isEqualTo("Netflix Atualizada")
            assertThat(subscriptionInDb?.price).isEqualTo(250.50)
            assertThat(subscriptionInDb?.expired).isTrue()
            assertThat(subscriptionInDb?.automaticRenewal).isFalse()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateSubscription_updateAndRetrieveSubscription_shouldMatchUpdatedData() = runTest {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: The subscription title is updated
        val updatedSubscription = subscription.copy(title = "Netflix Atualizada")
        subscriptionDao.updateSubscription(updatedSubscription)

        // THEN: The updated subscription should match the data in the database
        subscriptionDao.selectAllSubscriptions().test {
            val subscriptionInDb = awaitItem().find { it.subscriptionId == updatedSubscription.subscriptionId }
            assertThat(subscriptionInDb?.title).isEqualTo("Netflix Atualizada")

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteSubscriptionInRoomDB_returnsAffectedRowCount() {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: The subscription is deleted from the databaase
        val rowsAffected = subscriptionDao.deleteSubscription(subscription)

        // THEN: The delete operation should affect at least one row
        assertThat(rowsAffected).isGreaterThan(0)
    }

    @Test
    fun deleteSubscription_deleteSubscriptionInRoomDB_returnEmptyList() = runTest {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN & THEN: The subscription list should contain the subscription before deletion, then be empty after deletion
        subscriptionDao.selectAllSubscriptions().test {
            // The subscription should be present in the database
            assertThat(awaitItem()).containsExactly(subscription)

            // Delete the subscription
            subscriptionDao.deleteSubscription(subscription)

            // The database should be empty after deletion
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteNonExistentSubscription_shouldReturnZero() {
        // GIVEN: A subscription that was never saved in the database
        val nonExistentSubscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // WHEN: Attempting to delete a subscription that doesn't exist
        val rowsAffected = subscriptionDao.deleteSubscription(nonExistentSubscription)

        // THEN: No rows should be affected
        assertThat(rowsAffected).isEqualTo(0)
    }

    @Test
    fun saveSubscription_deleteMultipleSubscriptions_shouldReturnEmptyList() = runTest {
        // GIVEN: Multiple subscriptions saved in the database
        val subscription1 = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscription2 = Subscription(
            subscriptionId = 2,
            title = "Amazon Music",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscriptions in the database
        subscriptionDao.saveSubscription(subscription1)
        subscriptionDao.saveSubscription(subscription2)

        subscriptionDao.selectAllSubscriptions().test {
            // The subscription list size in db should match with 2 (2 subscriptions added)
            val subscriptionsInDb = awaitItem()
            assertThat(subscriptionsInDb.size).isEqualTo(2)

            // WHEN: Deleting both subscriptions
            subscriptionDao.deleteSubscription(subscription1)
            subscriptionDao.deleteSubscription(subscription2)

            // THEN: The database should be empty
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveSubscription_deleteOneSubscription_shouldNotAffectOthers() = runTest {
        // GIVEN: Multiple subscriptions saved in the database
        val subscription1 = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscription2 = Subscription(
            subscriptionId = 2,
            title = "Amazon Music",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscriptions in the database
        subscriptionDao.saveSubscription(subscription1)
        subscriptionDao.saveSubscription(subscription2)

        // WHEN: Deleting only subscription1
        subscriptionDao.deleteSubscription(subscription1)

        // THEN: Subscription2 should still be in the database
        subscriptionDao.selectAllSubscriptions().test {
            assertThat(awaitItem()).containsExactly(subscription2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteSubscription_deleteSameSubscriptionTwice_shouldReturnZeroOnSecondDelete() {
        // GIVEN: A subscription is created and saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: Deleting the subscription twice
        val firstDelete = subscriptionDao.deleteSubscription(subscription)
        val secondDelete = subscriptionDao.deleteSubscription(subscription)

        // THEN: The first delete should affect at least one row
        assertThat(firstDelete).isGreaterThan(0)

        // AND: The second delete should return 0 since the subscription is already deleted
        assertThat(secondDelete).isEqualTo(0)
    }

    @Test
    fun selectAllSubscriptions_selectAllSubscriptions_shouldReturnAListOfSubscriptions() = runTest {
        // GIVEN: Multiple subscriptions saved in the database
        val subscription1 = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscription2 = Subscription(
            subscriptionId = 2,
            title = "Amazon Music",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscriptions in the database
        subscriptionDao.saveSubscription(subscription1)
        subscriptionDao.saveSubscription(subscription2)

        // WHEN: Selecting all subscriptions from the database
        subscriptionDao.selectAllSubscriptions().test {
            // THEN: Should not return a empty list
            val subscriptionsList = awaitItem()
            assertThat(subscriptionsList).isNotEmpty()

            // AND: Should match with size of 2 (2 subscriptions saved in database)
            assertThat(subscriptionsList.size).isEqualTo(2)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllSubscriptions_emptyDatabase_shouldReturnEmptyList() = runTest {
        // GIVEN: No subscriptions saved in the database

        // WHEN: Selecting all subscriptions
        subscriptionDao.selectAllSubscriptions().test {
            // THEN: The list should be empty
            assertThat(awaitItem()).isEmpty()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllSubscriptions_oneSubscriptionSaved_shouldReturnSingleSubscription() = runTest {
        // GIVEN: A single subscription saved in the database
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        // WHEN: Selecting all subscriptions
        subscriptionDao.selectAllSubscriptions().test {
            // THEN: The list should contain exactly one subscription
            val subscriptionsList = awaitItem()
            assertThat(subscriptionsList).containsExactly(subscription)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllSubscriptions_deleteSubscription_shouldNotReturnDeletedSubscription() = runTest {
        // GIVEN: A subscription is saved and then deleted
        val subscription = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving and deleting the subscription in the database
        subscriptionDao.saveSubscription(subscription)

        subscriptionDao.selectAllSubscriptions().test {
            assertThat(awaitItem()).contains(subscription)

            // WHEN: Deleting the subscription twice
            subscriptionDao.deleteSubscription(subscription)

            // THEN: The deleted subscription should not be in the database
            assertThat(awaitItem()).doesNotContain(subscription)

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun selectAllSubscription_multipleSubscriptions_shouldMaintainInsertionOrder() = runTest {
        // GIVEN: Multiple subscriptions inserted in a specific order
        val subscription1 = Subscription(
            subscriptionId = 1,
            title = "Netflix",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        val subscription2 = Subscription(
            subscriptionId = 2,
            title = "Amazon Music",
            startDate = 1737504000000,
            dueDate = 1737804000000,
            price = 79.99,
            durationInMonths = 3,
            expired = false,
            automaticRenewal = true
        )

        // Saving the subscriptions in the database
        subscriptionDao.saveSubscription(subscription1)
        subscriptionDao.saveSubscription(subscription2)

        // WHEN: Selecting all subscriptions
        subscriptionDao.selectAllSubscriptions().test {
            // THEN: The order should be the same as insertion order
            val subscriptionsList = awaitItem()
            assertThat(subscriptionsList).containsExactly(subscription1, subscription2).inOrder()

            // Important: Cancels the flow to prevent coroutine leaks
            cancelAndIgnoreRemainingEvents()
        }
    }
}