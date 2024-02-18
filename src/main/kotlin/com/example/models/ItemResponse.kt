package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    val items: List<String>
)