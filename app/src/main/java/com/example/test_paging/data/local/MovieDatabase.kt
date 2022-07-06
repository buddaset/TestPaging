package com.example.test_paging.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.test_paging.data.repo.db.RepoRemoteKeys
import com.example.test_paging.data.repo.db.RepoDao
import com.example.android.codelabs.paging.model.Repo

import com.example.test_paging.data.movie.Movie
import com.example.test_paging.data.movie.db.MovieDao
import com.example.test_paging.data.movie.db.MovieRemoteKeys
import com.example.test_paging.data.movie.db.MovieRemoteKeysDao
import com.example.test_paging.data.repo.db.RepoRemoteKeysDao


@Database(
    entities = [
        Movie::class,
        Repo::class,
        RepoRemoteKeys::class,
        MovieRemoteKeys::class
    ],

    version = 1,
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun repoDao(): RepoDao
    abstract fun repoRemoteKeysDao(): RepoRemoteKeysDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(appContext: Context): MovieDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(appContext, MovieDatabase::class.java, "movie_db")
                        .build()
                INSTANCE = instance
                return@synchronized instance
            }
    }


}

//    companion object {
//        private val CALLBACK = object : RoomDatabase.Callback() {
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                db.execSQL(
//                    """
//                        CREATE TRIGGER update_movie AFTER INSERT ON movies
//                        BEGIN
//                        UPDATE movies
//                        END;
//                    """
//                )
//    }
//}

