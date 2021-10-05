package com.leagueapp1.data.collections

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class Match(
    val info: Info,
    val metadata: Metadata
)

@Serializable
data class Info(
    val gameCreation: Long,
    val gameDuration: Int,
    val gameId: Long,
    val gameMode: String,
    val gameName: String,
    val gameStartTimestamp: Long,
    val gameType: String,
    val gameVersion: String,
    val mapId: Int,
    val participants: List<Participant>,
    val platformId: String,
    val queueId: Int,
    val teams: List<Team>,
    val tournamentCode: String
)

@Serializable
data class Metadata(
    val dataVersion: String,
    @BsonId
    val matchId: String,
    val participants: List<String>
)

@Serializable
data class Participant(
    val assists: Int,
    val baronKills: Int,
    val bountyLevel: Int,
    val champExperience: Int,
    val champLevel: Int,
    val championId: Int,
    val championName: String,
    val championTransform: Int,
    val consumablesPurchased: Int,
    val damageDealtToBuildings: Int,
    val damageDealtToObjectives: Int,
    val damageDealtToTurrets: Int,
    val damageSelfMitigated: Int,
    val deaths: Int,
    val detectorWardsPlaced: Int,
    val doubleKills: Int,
    val dragonKills: Int,
    val firstBloodAssist: Boolean,
    val firstBloodKill: Boolean,
    val firstTowerAssist: Boolean,
    val firstTowerKill: Boolean,
    val gameEndedInEarlySurrender: Boolean,
    val gameEndedInSurrender: Boolean,
    val goldEarned: Int,
    val goldSpent: Int,
    val individualPosition: String,
    val inhibitorKills: Int,
    val inhibitorTakedowns: Int,
    val inhibitorsLost: Int,
    val item0: Int,
    val item1: Int,
    val item2: Int,
    val item3: Int,
    val item4: Int,
    val item5: Int,
    val item6: Int,
    val itemsPurchased: Int,
    val killingSprees: Int,
    val kills: Int,
    val lane: String,
    val largestCriticalStrike: Int,
    val largestKillingSpree: Int,
    val largestMultiKill: Int,
    val longestTimeSpentLiving: Int,
    val magicDamageDealt: Int,
    val magicDamageDealtToChampions: Int,
    val magicDamageTaken: Int,
    val neutralMinionsKilled: Int,
    val nexusKills: Int,
    val nexusLost: Int,
    val nexusTakedowns: Int,
    val objectivesStolen: Int,
    val objectivesStolenAssists: Int,
    val participantId: Int,
    val pentaKills: Int,
    val perks: Perks,
    val physicalDamageDealt: Int,
    val physicalDamageDealtToChampions: Int,
    val physicalDamageTaken: Int,
    val profileIcon: Int,
    val puuid: String,
    val quadraKills: Int,
    val riotIdName: String,
    val riotIdTagline: String,
    val role: String,
    val sightWardsBoughtInGame: Int,
    val spell1Casts: Int,
    val spell2Casts: Int,
    val spell3Casts: Int,
    val spell4Casts: Int,
    val summoner1Casts: Int,
    val summoner1Id: Int,
    val summoner2Casts: Int,
    val summoner2Id: Int,
    val summonerId: String,
    val summonerLevel: Int,
    val summonerName: String,
    val teamEarlySurrendered: Boolean,
    val teamId: Int,
    val teamPosition: String,
    val timeCCingOthers: Int,
    val timePlayed: Int,
    val totalDamageDealt: Int,
    val totalDamageDealtToChampions: Int,
    val totalDamageShieldedOnTeammates: Int,
    val totalDamageTaken: Int,
    val totalHeal: Int,
    val totalHealsOnTeammates: Int,
    val totalMinionsKilled: Int,
    val totalTimeCCDealt: Int,
    val totalTimeSpentDead: Int,
    val totalUnitsHealed: Int,
    val tripleKills: Int,
    val trueDamageDealt: Int,
    val trueDamageDealtToChampions: Int,
    val trueDamageTaken: Int,
    val turretKills: Int,
    val turretTakedowns: Int,
    val turretsLost: Int,
    val unrealKills: Int,
    val visionScore: Int,
    val visionWardsBoughtInGame: Int,
    val wardsKilled: Int,
    val wardsPlaced: Int,
    val win: Boolean
)

@Serializable
data class Team(
    val bans: List<Ban>,
    val objectives: Objectives,
    val teamId: Int,
    val win: Boolean
)

@Serializable
data class Perks(
    val statPerks: StatPerks,
    val styles: List<Style>
)

@Serializable
data class StatPerks(
    val defense: Int,
    val flex: Int,
    val offense: Int
)

@Serializable
data class Style(
    val description: String,
    val selections: List<Selection>,
    val style: Int
)

@Serializable
data class Selection(
    val perk: Int,
    val var1: Int,
    val var2: Int,
    val var3: Int
)

@Serializable
data class Ban(
    val championId: Int,
    val pickTurn: Int
)

@Serializable
data class Objectives(
    val baron: Baron,
    val champion: Champ,
    val dragon: Dragon,
    val inhibitor: Inhibitor,
    val riftHerald: RiftHerald,
    val tower: Tower
)

@Serializable
data class Baron(
    val first: Boolean,
    val kills: Int
)

@Serializable
data class Champ(
    val first: Boolean,
    val kills: Int
)

@Serializable
data class Dragon(
    val first: Boolean,
    val kills: Int
)

@Serializable
data class Inhibitor(
    val first: Boolean,
    val kills: Int
)

@Serializable
data class RiftHerald(
    val first: Boolean,
    val kills: Int
)

@Serializable
data class Tower(
    val first: Boolean,
    val kills: Int
)