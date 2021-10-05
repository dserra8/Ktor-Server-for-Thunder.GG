package com.leagueapp1

import com.leagueapp1.data.checkPasswordForEmail
import com.leagueapp1.routes.loginRoute
import com.leagueapp1.routes.registerRoute
import com.leagueapp1.routes.summonerRoutes
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.netty.*


fun main(args: Array<String>): Unit = EngineMain.main(args)


@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    install(Routing){
        registerRoute()
        summonerRoutes()
        loginRoute()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "League Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}
