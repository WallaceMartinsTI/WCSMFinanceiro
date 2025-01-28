package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard

@Dao
interface WalletCardDao {
    @Insert
    fun saveWalletCard(walletCard: WalletCard) : Long

    @Update
    fun updateWalletCard(walletCard: WalletCard) : Int

    @Delete
    fun deleteWalletCard(walletCard: WalletCard) : Int
}