package com.leagueapp1.data.requests

import com.leagueapp1.data.Constants.ALL_CHAMPION_MASTERIES
import com.leagueapp1.data.Constants.API_KEY
import com.leagueapp1.data.Constants.API_PART
import com.leagueapp1.data.Constants.MATCH_DETAIL_URL
import com.leagueapp1.data.Constants.MATCH_LIST
import com.leagueapp1.data.Constants.NUM_MATCHES
import com.leagueapp1.data.Constants.RANK_TYPE_STRING
import com.leagueapp1.data.Constants.SUMMONER_INFO
import com.leagueapp1.data.Constants.SUMMONER_RANK_URL
import com.leagueapp1.data.Constants.URL
import com.leagueapp1.data.collections.*
import com.leagueapp1.data.getSummonerFromDatabase
import com.leagueapp1.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*

suspend fun updateSummoner(client: HttpClient, summonerFromApp: Summoner): Summoner {
    val summonerFromRiot = getSummoner(client, summonerFromApp.name)
    val championListFromRiot = getChampions(client, summonerFromApp.id)
    val rankFromRiot = getSummonerRank(client, summonerFromApp.id)?.find { it.queueType == "RANKED_SOLO_5x5" }
    val summonerFromMongo = getSummonerFromDatabase(summonerFromApp.id)
    val championListFromApp = summonerFromApp.championList

    //First sync summoner from mongo and app



    championListFromRiot?.let { newList ->
        championListFromApp?.forEachIndexed { index, oldChamp ->
            if (oldChamp.rankInfo != newList[index].rankInfo || oldChamp.roles != null) {
                newList[index].apply {
                    rankInfo = oldChamp.rankInfo
                    roles = oldChamp.roles
                }
            }
        }
    }

    if (championListFromRiot != null && rankFromRiot != null && summonerFromRiot != null) {
        return summonerFromRiot.copy(rank = rankFromRiot, championList = championListFromRiot)
    }
    return summonerFromApp.apply { rank = summonerFromMongo?.rank ?: rank }
}


suspend fun prepareSummonerFirstTime(client: HttpClient, summonerName: String): Summoner? {
    val summoner = getSummoner(client, summonerName)
    return summoner?.let { response ->
        summoner.copy(
            championList = getChampions(client, response.id),
            rank = getSummonerRank(client, summoner.id)?.find { it.queueType == "RANKED_SOLO_5x5"
            })
    }
}

suspend fun getSummoner(client: HttpClient, summonerName: String): Summoner? {
    return try {
        client.get("$URL$SUMMONER_INFO$summonerName$API_PART")
    } catch (e: Exception) {
        null
    }
}

suspend fun getChampions(client: HttpClient, id: String): List<Champion>? {
    return try {
        client.get("$URL$ALL_CHAMPION_MASTERIES$id$API_PART")
    } catch (e: Exception) {
        null
    }
}

suspend fun getSummonerRank(client: HttpClient, id: String): List<Rank>? {
    return try {
        client.get("$URL$SUMMONER_RANK_URL$id$API_PART")
    } catch (e: Exception) {
        null
    }
}

/**
 * MATCH api calls to riot server
 */

suspend fun getMatchList(client: HttpClient, puuid: String): Resource<List<String>> {
    return try {
        val list: List<String> = client.get("$MATCH_LIST$puuid/ids?type=$RANK_TYPE_STRING&start=0&count=$NUM_MATCHES&api_key=$API_KEY")
        Resource.Success(list)
    } catch (e: Throwable) {
        Resource.Error(e)
    }
}

suspend fun getMatchDetails(client: HttpClient, matchID: String): Resource<Match> {
    return try {
       val match: Match = client.get("$MATCH_DETAIL_URL$matchID$API_PART")
        Resource.Success(match)
    } catch (e: Throwable) {
        Resource.Error(e)
    }
}


