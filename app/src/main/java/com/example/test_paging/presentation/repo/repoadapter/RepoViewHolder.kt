/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test_paging.presentation.repo.repoadapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.codelabs.paging.model.Repo
import com.example.test_paging.R
import com.example.test_paging.databinding.RepoViewItemBinding

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(private val binding: RepoViewItemBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(repo: Repo) = with(binding) {
         repoName.text = repo.fullName
        if (repo.description !=null) repoDescription.text = repo.description
        repoStars.text = repo.stars.toString()
        repoForks.text = repo.forks.toString()
        repoLanguage.text = repo.language

        }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repo_view_item, parent, false)
            return RepoViewHolder(RepoViewItemBinding.bind(view))
        }
    }
}
