package com.novandiramadhan.reflect.data.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStringFloatMap(map: Map<String, Float>?): String? =
        map?.let { gson.toJson(it) }

    @TypeConverter
    fun toStringFloatMap(json: String?): Map<String, Float>? =
        json?.let {
            val type = object : TypeToken<Map<String, Float>>() {}.type
            gson.fromJson<Map<String, Float>>(it, type)
        }
}
