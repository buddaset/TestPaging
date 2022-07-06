package com.example.test_paging.data.movie


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val idDB: Long = 0,
    @SerialName("adult")  val adult: Boolean,
    @SerialName("genre_ids") val genresId: List<Int>,
    @SerialName("id") val remoteId: Long,
    @SerialName("overview")  val storyLine: String,
    @SerialName("poster_path")  val imagePath: String,
    @SerialName("release_date")  val releaseDate: String,
    @SerialName("title") val title: String,
    @SerialName("vote_average") val rating: Double,
    @SerialName("vote_count") val reviewCount: Int,
)

