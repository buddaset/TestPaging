package com.example.test_paging.presentation.movie.adapter.movie

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test_paging.R
import com.example.test_paging.data.movie.Movie
import com.example.test_paging.databinding.ViewHolderMovieBinding


class MovieViewHolder(private val binding: ViewHolderMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    private val context = itemView.context

//    @SerialName("adult")  val adult: Boolean,
//    @SerialName("genre_ids") val genresId: List<Int>,
//    @PrimaryKey
//    @SerialName("id") val id: Long,
//    @SerialName("overview")  val storyLine: String,
//    @SerialName("poster_path")  val imagePath: String,
//    @SerialName("release_date")  val releaseDate: String,
//    @SerialName("title") val title: String,
//    @SerialName("vote_average") val rating: Double,
//    @SerialName("vote_count") val reviewCount: Int,

    fun bind(movie: Movie) = with(binding) {
            title.text = movie.title
            pgAge.text = 13.toString()
            val likeRes = R.drawable.ic_like_off
            likedMovie.setImageResource(likeRes)

            genre.text = movie.genresId.joinToString { it.toString() }
            ratingBar.rating = 5f
            countReview.text = context.getString(R.string.reviews, movie.reviewCount)

            runningTime.text = context.getString(R.string.movie_minutes, 100)
        val imageUrl =  baseImageUrl + movie.imagePath
            Glide
                .with(context)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.moviePoster)



        }
    companion object {

        const val  baseImageUrl = "https://image.tmdb.org/t/p/w300"
    }
    }


