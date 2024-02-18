package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.AuthenticationConfig
import com.example.models.ItemResponse
import com.example.models.LoginRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoutes() {
    authenticate("auth-jwt") {
        get("/") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal?.payload?.getClaim("username")?.asString()
            val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }

        get("/items") {
            val response = ItemResponse(items = listOf("one", "two", "three"))
            call.respond(response)
        }
    }

    post("/login") {
        val loginRequest = call.receive<LoginRequest>()

        if (loginRequest.username == "johan" && loginRequest.password == "pass") {
            val token = generateToken(loginRequest)

            call.respond(hashMapOf("token" to token))
        } else {
            call.respondText("Stay right there!", status = HttpStatusCode.Unauthorized)
        }
    }
}

private fun generateToken(loginRequest: LoginRequest): String {
    return JWT.create()
        .withAudience(AuthenticationConfig.AUDIENCE)
        .withIssuer(AuthenticationConfig.ISSUER)
        .withClaim("username", loginRequest.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256(AuthenticationConfig.SECRET))
}