package com.leagueapp1.data

import com.leagueapp1.data.Constants.LP_SEPARATION
import com.leagueapp1.data.Constants.REQUIRED_NUM_GAMES
import com.leagueapp1.data.collections.ChampRankInfo
import com.leagueapp1.data.collections.Champion
import com.leagueapp1.data.collections.Match
import com.leagueapp1.data.requests.getMatchDetails
import com.leagueapp1.data.requests.getMatchList
import com.leagueapp1.util.Resource
import io.ktor.client.*

data class ChampWinRate(
    var wins: Int,
    var total: Int
)

suspend fun initializeChampRanks(client: HttpClient, puuid: String) {
    when (val result = getMatchList(client, puuid)) {
        is Resource.Success -> {
            parseMatchList(result.data!!, client, puuid)
        }
        is Resource.Error -> {

        }
    }
}

suspend fun parseMatchList(list: List<String>, client: HttpClient, puuid: String) {
    val winRatesList = hashMapOf<Int, ChampWinRate>()
    list.forEach { matchID ->
        when (val matchResponse = getMatchDetails(client, matchID)) {
            is Resource.Success -> {
                val match = matchResponse.data
                addMatch(match!!)
                inspectMatch(match, winRatesList, puuid)
            }
            is Resource.Error -> {

            }
        }
    }
    calculateBoost(winRatesList, puuid)
}

fun inspectMatch(match: Match, winRateList: HashMap<Int, ChampWinRate>, puuid: String) {
    val participantIndex = findIndex(match.metadata.participants, puuid)
    participantIndex?.let { index ->
        val gameInfo = match.info.participants[index]
        val outcome = gameInfo.win
        val champId = gameInfo.championId
        val win = if (outcome) 1 else 0
        val entry = winRateList.getOrPut(
            champId
        ) {
            ChampWinRate(win, 1)
        }
        entry.wins = entry.wins + win
        entry.total = entry.total + 1
    }
}

fun findIndex(list: List<String>, puuid: String): Int? {
    for ((index, _puuid) in list.withIndex()) {
        if (_puuid == puuid) {
            return index
        }
    }
    return null
}

suspend fun calculateBoost(list: HashMap<Int, ChampWinRate>, puuid: String) {
    val summoner = getSummonerWithPuuid(puuid)
    summoner?.let { sum ->
        val champList : MutableList<Champion> = sum.championList as MutableList<Champion>
        list.forEach { champ ->
            var recentBoost = when ((champ.value.wins.toFloat() / champ.value.total.toFloat()).times(100).toInt()) {
                50 -> 10
                51 -> 20
                52 -> 30
                53 -> 40
                54 -> 50
                55 -> 60
                56 -> 70
                57 -> 80
                58 -> 90
                in 59..100 -> 100
                else -> 0
            }
            if(champ.value.total < REQUIRED_NUM_GAMES) recentBoost = 0

            val champObj = champList.find { it.championId == champ.key }
            val rankBoost = if ((champObj?.championPoints ?: 0) > 2000) {
                when(sum.rank?.tier){
                    "IRON" -> 0
                    "BRONZE" -> 0
                    "SILVER" -> LP_SEPARATION
                    "GOLD" -> LP_SEPARATION*2
                    "PLATINUM" -> LP_SEPARATION*3
                    "DIAMOND" -> LP_SEPARATION*4
                    "MASTER" -> LP_SEPARATION*5
                    "GRANDMASTER" -> LP_SEPARATION*6
                    "CHALLENGER" -> LP_SEPARATION*7
                    else -> 0
                }
            } else 0

            val totalBoost = recentBoost + rankBoost
            val initialRank = calculateRank(totalBoost)

            champList[champList.indexOf(champObj)].rankInfo = ChampRankInfo(totalBoost, initialRank)
        }
        saveSummoner(sum.copy(championList = champList))
    }
}

fun calculateRank(lp: Int): String {
    return when (lp) {
        in 0 until LP_SEPARATION -> "IRON"
        in LP_SEPARATION until LP_SEPARATION*2 -> "BRONZE"
        in LP_SEPARATION*2 until LP_SEPARATION*3 -> "SILVER"
        in LP_SEPARATION*3 until LP_SEPARATION*4 -> "GOLD"
        in LP_SEPARATION*4 until LP_SEPARATION*5 -> "PLATINUM"
        in LP_SEPARATION*5 until LP_SEPARATION*6 -> "DIAMOND"
        in LP_SEPARATION*6 until LP_SEPARATION*7 -> "MASTER"
        in LP_SEPARATION*7 until LP_SEPARATION*8 -> "GRANDMASTER"
        else -> "CHALLENGER"
    }
}

