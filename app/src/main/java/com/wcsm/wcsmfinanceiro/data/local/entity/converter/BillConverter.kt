package com.wcsm.wcsmfinanceiro.data.local.entity.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.data.local.model.PaymentType
import com.wcsm.wcsmfinanceiro.util.getBillTypeFromString
import com.wcsm.wcsmfinanceiro.util.getPaymentTypeFromString

@ProvidedTypeConverter
class BillConverter {

    @TypeConverter
    fun billTypeToString(billType: BillType) : String {
        return billType.displayName
    }

    @TypeConverter
    fun stringToBillType(billTypeString: String) : BillType {
        return getBillTypeFromString(billTypeString)
    }

    @TypeConverter
    fun paymntTypeToString(paymentType: PaymentType) : String {
        return paymentType.displayName
    }

    @TypeConverter
    fun stringToPaymentType(paymentTypeString: String) : PaymentType {
        return getPaymentTypeFromString(paymentTypeString)
    }

    @TypeConverter
    fun stringListToString(stringList: List<String>) : String {
        return Gson().toJson(stringList)
    }

    @TypeConverter
    fun stringToStringList(string: String) : List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, listType)
    }

}