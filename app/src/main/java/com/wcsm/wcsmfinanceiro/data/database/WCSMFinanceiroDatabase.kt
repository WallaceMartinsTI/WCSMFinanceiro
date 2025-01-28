package com.wcsm.wcsmfinanceiro.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wcsm.wcsmfinanceiro.data.database.dao.BillsDao
import com.wcsm.wcsmfinanceiro.data.database.dao.WalletCardDao
import com.wcsm.wcsmfinanceiro.data.database.dao.WalletDao
import com.wcsm.wcsmfinanceiro.data.entity.Bill
import com.wcsm.wcsmfinanceiro.data.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.entity.WalletCard
import com.wcsm.wcsmfinanceiro.data.entity.converter.BillConverter
import com.wcsm.wcsmfinanceiro.data.helper.Constants

@Database(
    entities = [Bill::class, Wallet::class, WalletCard::class],
    version = 1
)
@TypeConverters(BillConverter::class)
abstract class WCSMFinanceiroDatabase : RoomDatabase() {

    abstract val billsDao: BillsDao
    abstract val walletDao: WalletDao
    abstract val walletCardDao: WalletCardDao

    companion object {
        fun getInstance(context: Context) : WCSMFinanceiroDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = WCSMFinanceiroDatabase::class.java,
                name = Constants.DATABASE_NAME
            )
                .addTypeConverter(BillConverter())
                .build()
        }
    }
}