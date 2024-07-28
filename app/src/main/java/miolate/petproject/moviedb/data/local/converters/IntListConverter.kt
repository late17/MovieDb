package miolate.petproject.moviedb.data.local.converters

import androidx.room.TypeConverter

// Genres should be stored in the other table
// Short term solution to save those id's
class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(data: String?): List<Int>? {
        return data?.split(",")?.map { it.toInt() }
    }
}