package com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto

import kotlinx.serialization.Serializable

@Serializable
data class WsRequest(
    val method: String, val params: List<String>? = null, val id: Int? = null
)