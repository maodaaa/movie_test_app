package com.movie.trvn.core.data.source.remote.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MoviesResponse(
	@field:SerializedName("results")
	val results: List<MovieData>
) : Parcelable

@Parcelize
data class MovieData(
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("release_date")
	val releaseDate: String,
) : Parcelable
