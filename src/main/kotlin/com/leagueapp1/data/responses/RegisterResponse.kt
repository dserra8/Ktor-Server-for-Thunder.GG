package com.leagueapp1.data.responses

import com.leagueapp1.data.collections.Summoner

data class RegisterResponse(
    val summoner: Summoner?,
    val successful: Boolean,
    val message: String
)
