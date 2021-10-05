package com.leagueapp1.routes


import com.example.security.getHashWithSalt
import com.leagueapp1.data.*
import com.leagueapp1.data.collections.User
import com.leagueapp1.data.requests.AccountRequest
import com.leagueapp1.data.requests.prepareSummonerFirstTime
import com.leagueapp1.data.responses.RegisterResponse
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.ContentTransformationException
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }
            val userExists = checkIfUserExists(request.email)
            if (!userExists) {
                val client = HttpClient(CIO) {
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                            prettyPrint = true
                            isLenient = true
                        })
                    }
                }
                val summoner = prepareSummonerFirstTime(client, request.summonerName)
                summoner?.let {
                    saveSummoner(it)
                    initializeChampRanks(client, it.puuid)
                    if (registerUser(User(email = request.email, password = getHashWithSalt(request.password), summonerID = it.id, puuid = it.puuid))) {
                        val updatedSummoner = getSummonerFromDatabase(it.id)
                        call.respond(OK, RegisterResponse(updatedSummoner, true, "Successfully created an Account!"))
                    } else {
                        call.respond(OK, RegisterResponse(null,false, "An unknown error occured"))
                    }
                } ?: call.respond(OK, RegisterResponse(null, false, "Error registering. Summoner Not Found!"))
                client.close()

            } else {
                call.respond(OK, RegisterResponse(null,false, "A user with that E-Mail already exists"))
            }
        }
    }
}
