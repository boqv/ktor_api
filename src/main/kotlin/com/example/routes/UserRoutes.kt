package com.example.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.AuthenticationConfig
import com.example.data.dao.dao
import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.userRoutes() {
    //authenticate("auth-jwt") {
        get("/") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal?.payload?.getClaim("username")?.asString()
            val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }

        get("/items") {
            val response = ItemResponse(items = listOf("one", "two", "GO!"))
            call.respond(response)
        }

        get("/articles") {
            try {
                val articles = dao.allArticles()
                call.respond(
                    mapOf("articles" to articles)
                )
            } catch (exception: Exception) {
                println(exception)
            }
        }

        post("/articles") {
            try {
                val request = call.receive<PostArticleRequest>()

                val id = dao.addNewArticle(request.title, request.body)

                val article = dao.article(id)

                call.respond(hashMapOf("article" to article))
            } catch (exception: Exception) {
                println(exception)
            }
        }
    //}

    post("/login") {
        val loginRequest = call.receive<LoginRequest>()
        val user = User(loginRequest.username, loginRequest.password)
        if (users.contains(user)) {
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
        .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60))
        .sign(Algorithm.HMAC256(AuthenticationConfig.SECRET))
}