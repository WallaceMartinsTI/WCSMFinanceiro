package com.wcsm.wcsmfinanceiro.data.local.entity.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class WalletConverter {

    @TypeConverter
    fun longDoublePairListToString(longDoublePairList: List<Pair<String, Double>>): String {
        return Gson().toJson(longDoublePairList)
    }

    @TypeConverter
    fun stringToLongDoublePairList(string: String): List<Pair<String, Double>> {
        val listType = object : TypeToken<List<Pair<String, Double>>>() {}.type
        return Gson().fromJson(string, listType)
    }
}