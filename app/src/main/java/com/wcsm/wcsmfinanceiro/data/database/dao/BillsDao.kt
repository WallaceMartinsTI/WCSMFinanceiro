package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wcsm.wcsmfinanceiro.data.entity.Bill

@Dao
interface BillsDao {
    @Insert
    fun saveBill(bill: Bill): Long // Long -> Item ID inserted

    @Delete
    fun deleteBill(bill: Bill): Int // Quantity of items deleted

    @Query("SELECT * FROM bills")
    fun selectAllBills() : List<Bill>
}