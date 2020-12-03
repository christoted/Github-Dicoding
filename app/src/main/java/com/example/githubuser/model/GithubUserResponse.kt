package com.example.githubuser.model

data class GithubUserResponse(
    val incomplete_results: Boolean,
    val items: List<GithubUserItem>,
    val total_count: Int
)