package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListStringConverters {

    private val gson: Gson = Gson()

    @TypeConverter
    fun stringToStringList(data: String?): List<String?>? {
        if (data == null) {
            return Collections.emptyList()
        }

        return gson.fromJson(data, object : TypeToken<List<String?>?>() {}.type)
    }

    @TypeConverter
    fun stringListToString(someObjects: List<String?>?): String? {
        return gson.toJson(someObjects)
    }

}
