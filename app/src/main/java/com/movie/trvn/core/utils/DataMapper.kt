package com.movie.trvn.core.utils

import com.movie.trvn.core.data.source.local.entity.MoviesEntity
import com.movie.trvn.core.data.source.remote.model.MovieData
import com.movie.trvn.core.domain.model.Movie

object DataMapper {

    fun MovieData.toDomain(): Movie {
        return Movie(id, title, releaseDate)
    }

    fun MoviesEntity.toDomain(): Movie {
        return Movie(id, title, releaseDate)
    }

    fun Movie.toEntity(): MoviesEntity {
        return MoviesEntity(id, title, releaseDate)
    }
}