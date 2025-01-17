package com.wcsm.wcsmfinanceiro.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill2
import com.wcsm.wcsmfinanceiro.data.helper.Constants

@Database(
    entities = [Bill2::class],
    version = 1
)
abstract class WCSMFinanceiroDatabase : RoomDatabase() {

    abstract val billsDao: BillsDao
    //abstract val walletDao: WalletDao

    companion object {
        fun getInstance(context: Context) : WCSMFinanceiroDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = WCSMFinanceiroDatabase::class.java,
                name = Constants.DATABASE_NAME
            ).build()
        }
    }
}