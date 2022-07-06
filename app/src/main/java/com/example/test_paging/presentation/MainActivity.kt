package com.example.test_paging.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commitNow
import com.example.test_paging.R
import com.example.test_paging.presentation.movie.MoviesFragment
import com.example.test_paging.presentation.repo.RepoFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            supportFragmentManager.commitNow {
                replace(R.id.container_fragment,  MoviesFragment()) // RepoFragment()  MoviesFragment()
            }
    }
}