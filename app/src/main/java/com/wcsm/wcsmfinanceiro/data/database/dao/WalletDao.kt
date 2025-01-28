package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.relation.WalletWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {
    @Insert
    fun saveWallet(wallet: Wallet) : Long

    @Update
    fun updateWallet(wallet: Wallet) : Int

    @Delete
    fun deleteWallet(wallet: Wallet) : Int

    @Transaction
    @Query("SELECT * FROM wallets")
    fun selectAllWalletWithCards() : Flow<List<WalletWithCards>>
}