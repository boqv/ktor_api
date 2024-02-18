package com.example

final class AuthenticationConfig {
    companion object {
        const val ISSUER: String = "http://0.0.0.0:8080/"
        const val AUDIENCE: String = "http://0.0.0.0:8080/"
        const val SECRET: String = "secret123"
    }
}