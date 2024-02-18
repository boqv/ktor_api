package com.example.models

data class User(
    val username: String,
    val password: String
)

val users: List<User> = listOf(
    User("johan", "pass"),
    User("other", "user")
)