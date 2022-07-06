package com.example.test_paging.data.movie.remote

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {




    @GET("movie/popular")
    suspend fun loadMoviesPopular(
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("pageSize") @IntRange(from = 1, to = MAX_PAGE_SIZE.toLong()) pageSize: Int = DEFAULT_PAGE_SIZE
    ): MoviesResponse



    companion object {

        const val DEFAULT_PAGE_SIZE = 20
        const val MAX_PAGE_SIZE = 60



            private const val BASE_URL =  "https://api.themoviedb.org/3/"

            @OptIn(ExperimentalSerializationApi::class)
            fun create(): MovieApi {
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BASIC

                val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .addInterceptor(ApiKeyInterception())
                    .build()

                val json =  Json { ignoreUnknownKeys = true}
                val contentType = "application/json".toMediaType()
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(json.asConverterFactory(contentType))
                    .build()
                    .create(MovieApi::class.java)
            }
    }

}

class ApiKeyInterception : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val url = originalUrl.newBuilder()
            .addQueryParameter(API_QUERY, API_KEY )
            .build()

        val request = originalRequest.newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)



    }

    companion object {
        private const val API_QUERY = "api_key"
        private const val API_KEY= "fbdee813c00d13b3212ef90e8b9ba074"
    }
}