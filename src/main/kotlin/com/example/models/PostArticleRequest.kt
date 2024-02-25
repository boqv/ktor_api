package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class PostArticleRequest(
    val title: String,
    val body: String
)