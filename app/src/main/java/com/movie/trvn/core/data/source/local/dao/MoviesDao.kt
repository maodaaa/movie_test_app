package com.movie.trvn.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movie.trvn.core.data.source.local.entity.MoviesEntity


@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MoviesEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movieList: List<MoviesEntity>)
    @Query("DELETE FROM movies")
    fun deleteAll()
}