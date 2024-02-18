package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.AuthenticationConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "access to login"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(AuthenticationConfig.SECRET))
                    .withAudience(AuthenticationConfig.AUDIENCE)
                    .withIssuer(AuthenticationConfig.ISSUER)
                    .build()
            )

            validate { credentials ->
                if (credentials.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credentials.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}