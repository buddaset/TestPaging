package com.example.test_paging.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toSting(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromString(value: String) : List<Int> {
        val mapType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, mapType)
    }


}