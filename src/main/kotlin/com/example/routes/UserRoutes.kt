package com.example.routes

import com.example.models.LoginRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    route("/") {
        get {
            call.respondText("Hello", status = HttpStatusCode.OK)
        }
    }

    post("/login") {
        val loginRequest = call.receive<LoginRequest>()

        if (loginRequest.username == "Johan") {
            call.respondText("You are logged in", status = HttpStatusCode.OK)
        } else {
            call.respondText("Stay right there!", status = HttpStatusCode.Unauthorized)
        }
    }
}