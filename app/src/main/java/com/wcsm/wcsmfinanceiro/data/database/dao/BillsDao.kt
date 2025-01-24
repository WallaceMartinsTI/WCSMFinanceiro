package com.wcsm.wcsmfinanceiro.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillsDao {
    @Insert
    fun saveBill(bill: Bill): Long // Long -> Item ID inserted

    @Update
    fun updateBill(bill: Bill) : Int

    @Delete
    fun deleteBill(bill: Bill): Int // Quantity of items deleted

    @Query("SELECT * FROM bills")
    fun selectAllBills() : Flow<List<Bill>>

    @Query("SELECT * FROM bills WHERE date BETWEEN :startDate AND :endDate")
    fun selectBillsByDate(startDate: Long, endDate: Long) : Flow<List<Bill>>

    @Query("""
        SELECT * FROM bills WHERE 
        origin LIKE '%' || :text || '%' OR
        title LIKE '%' || :text || '%' OR
        description LIKE '%' || :text || '%'
    """)
    fun selectBillsByText(text: String) : Flow<List<Bill>>
}