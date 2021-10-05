package com.leagueapp1.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AccountRequest(
    val email: String,
    val password: String,
    val summonerName: String
)