package miolate.petproject.moviedb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import miolate.petproject.moviedb.data.local.converters.IntListConverter
import miolate.petproject.moviedb.data.local.converters.LocalDateConverter
import miolate.petproject.moviedb.data.local.dao.MovieDao
import miolate.petproject.moviedb.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateConverter::class, IntListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private const val APP_DATABASE_NAME = "app_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}