package com.leagueapp1.routes

import com.leagueapp1.data.collections.Summoner
import com.leagueapp1.data.getSummonerFromDatabase
import com.leagueapp1.data.getUser
import com.leagueapp1.data.requests.updateSummoner
import com.leagueapp1.data.saveSummoner
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.summonerRoutes() {
    route("/getSummoner") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val summonerID = getUser(email)?.summonerID ?: ""
                val summoner = getSummonerFromDatabase(summonerID)
                val client = HttpClient()
                summoner?.let {
                    call.respond(OK, summoner)
                } ?: call.respond(Conflict)
                client.close()
            }
        }
    }

    route("/addSummoner") {
        authenticate {
            post {
                val summoner = try {
                    call.receive<Summoner>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                val client = HttpClient(CIO) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                }
                if (saveSummoner(updateSummoner(client, summoner))) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}