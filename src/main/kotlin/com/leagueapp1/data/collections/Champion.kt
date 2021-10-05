package com.leagueapp1.data.collections

import kotlinx.serialization.Serializable

@Serializable
data class Champion(
    val championId: Int,
    val championLevel: Int,
    val championPoints: Int,
    val lastPlayTime: Long,
    val championPointsSinceLastLevel: Long,
    val championPointsUntilNextLevel: Long,
    val chestGranted: Boolean,
    val tokensEarned: Int,
    val summonerId: String,
    var rankInfo: ChampRankInfo = ChampRankInfo(),
    var roles: TrueRoles? = null
)

@Serializable
data class ChampRankInfo(
    val lp: Int = 0,
    val rank: String = "IRON"
)

@Serializable
data class TrueRoles(
    val TOP: Boolean = false,
    val JUNGLE: Boolean = false,
    val MIDDLE: Boolean = false,
    val BOTTOM: Boolean = false,
    val UTILITY: Boolean = false,
    val ALL: Boolean = true
)