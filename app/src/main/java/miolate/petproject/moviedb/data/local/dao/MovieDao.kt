package miolate.petproject.moviedb.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import miolate.petproject.moviedb.data.local.entities.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie")
    fun getAllFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE id = :movieId")
    fun getById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movie: List<MovieEntity>)

    @Delete
    fun delete(movie: MovieEntity)

    @Delete
    fun deleteAll(movie: List<MovieEntity>)

    @Update
    fun update(movie: MovieEntity): Int
}