package com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto

import kotlinx.serialization.Serializable

@Serializable
data class WsResponse(
    val result: kotlinx.serialization.json.JsonElement? = null, val id: Int? = null
)