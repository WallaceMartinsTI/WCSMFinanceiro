package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wcsm.wcsmfinanceiro.data.entity.Bill

@Dao
interface BillsDao {
    @Insert
    fun saveBill(bill: Bill): Long // Long -> Item ID inserted

    @Update
    fun updateBill(bill: Bill) : Int

    @Delete
    fun deleteBill(bill: Bill): Int // Quantity of items deleted

    @Query("SELECT * FROM bills")
    fun selectAllBills() : List<Bill>

    @Query("SELECT * FROM bills WHERE date BETWEEN :startDate AND :endDate")
    fun selectBillsByDate(startDate: Long, endDate: Long) : List<Bill>
}