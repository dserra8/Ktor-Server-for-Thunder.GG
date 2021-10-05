package com.leagueapp1.data

import com.example.security.checkHashForPassword
import com.leagueapp1.data.collections.Match
import com.leagueapp1.data.collections.Summoner
import com.leagueapp1.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client =  KMongo.createClient().coroutine
private val database = client.getDatabase("LeagueDatabase")
private val users = database.getCollection<User>()
private val summoners = database.getCollection<Summoner>()
private val matches = database.getCollection<Match>()


/**
 *  User Database Operations
 */

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return checkHashForPassword(passwordToCheck, actualPassword)
}

suspend fun getUser(email: String): User? {
    return users.findOne(User::email eq email)
}

suspend fun deleteUser(email: String): Boolean {
    return users.deleteOne(User::email eq email).wasAcknowledged()
}

/**
 *  Summoner Database Operations
 */

suspend fun saveSummoner(summoner: Summoner): Boolean {
    val summonerExists = summoners.findOneById(summoner.id) != null
    return if (summonerExists) {
        summoners.updateOneById(summoner.id, summoner).wasAcknowledged()
    } else {
        summoners.insertOne(summoner).wasAcknowledged()
    }
}

suspend fun getSummonerFromDatabase(id: String): Summoner? {
    return summoners.findOneById(id)
}

suspend fun getSummonerWithPuuid(puuid: String): Summoner? {
    return summoners.findOne(Summoner::puuid eq puuid)
}

/**
 *  Match Database Operations
 */

suspend fun addMatch(match: Match): Boolean {
    val matchExists = matches.findOneById(match.metadata.matchId) != null
    return if (matchExists) {
        true
    } else {
        matches.insertOne(match).wasAcknowledged()
    }
}