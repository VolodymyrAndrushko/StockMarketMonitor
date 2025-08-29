package com.vandrushko.feature_crypto_currency.data.remote.web_sockets

import com.vandrushko.core.di.remote.WebSocketClient
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto.MiniTickerDTO
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto.toCurrencyInfoEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val BINANCE_WS_URL = "wss://stream.binance.com:9443/ws"

@Serializable
data class WsRequest(
    val method: String, val params: List<String>? = null, val id: Int? = null
)

@Serializable
data class WsResponse(
    val result: kotlinx.serialization.json.JsonElement? = null, val id: Int? = null
)

class WebSocketBinanceApi(
    @WebSocketClient private val client: HttpClient, private val scope: CoroutineScope
) : WebSocketCurrencyMarketApi {

    private var webSocketJob: Job? = null
    private val mutex = Mutex()
    private val subscriptions = mutableSetOf<String>()

    private val _currencyFlow = MutableSharedFlow<CurrencyEntity>(replay = 1)
    override val currencyFlow: Flow<CurrencyEntity> get() = _currencyFlow

    private var session: DefaultClientWebSocketSession? = null

    override fun startConnection() {
        scope.launch {
            session = client.webSocketSession(urlString = BINANCE_WS_URL)

            scope.launch {
                session?.incoming?.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            println("MiniTicker: Frame")
                            handleFrame(frame.readText())
                        }

                        is Frame.Ping -> {
                            println("MiniTicker: Pong")
                            session?.send(Frame.Pong(frame.buffer))
                        }

                        is Frame.Close -> {
                            println("MiniTicker: Close")
                            session?.close(
                                frame.readReason() ?: CloseReason(CloseReason.Codes.NORMAL, "Bye")
                            )
                        }

                        else -> {
                            println("MiniTicker: Error")
                        }
                    }
                }
            }
        }
    }

    private suspend fun handleFrame(text: String) {
        try {
            val ticker = Json.decodeFromString(MiniTickerDTO.serializer(), text)
            println("MiniTicker: ${ticker.s} | Close: ${ticker.c} | High: ${ticker.h} | Low: ${ticker.l}")
            set.add(ticker.s)
            _currencyFlow.emit(ticker.toCurrencyInfoEntity())
        } catch (_: Exception) {
            // Might be subscribe/unsubscribe response
            val resp = Json.decodeFromString(WsResponse.serializer(), text)
            println("Response: $resp")
        }
    }

    private val set = mutableSetOf<String>()

    /**
     * Subscribe to a symbol (will send SUBSCRIBE message to Binance WS)
     */
    override suspend fun subscribeSymbol(symbols: List<String>, id: Int) {
        if (set.containsAll(symbols)) return

        val filtered = symbols.filter { it !in set }

        val streams = filtered.map { "${it}@miniTicker" }
        val request = WsRequest(method = "SUBSCRIBE", params = streams, id = id)
        session?.sendSerialized(request)
        println("Subscribed to: $streams")
    }

    /**
     * Unsubscribe from a symbol
     */
    override suspend fun unsubscribeSymbol(symbols: List<String>, id: Int) {
        set.removeAll(symbols)
        val streams = symbols.map { "${it}@miniTicker" }
        val request = WsRequest(method = "UNSUBSCRIBE", params = streams, id = id)
        session?.sendSerialized(request)
        println("Unsubscribed from: $streams")
    }

    override fun stopConnection() {
        scope.launch {
            session?.close()
        }
        client.close()
    }
//    override fun startConnection() {
//        scope.launch {
//            client.webSocket(urlString = BINANCE_WS_URL) {
//                // List of symbols to subscribe
//                val symbols = listOf("btcusdt", "ethusdt", "bnbusdt", "xrpbidr")
//                val streams = symbols.map { "${it}@miniTicker" }
//
//                // Subscribe
//                val subscribeRequest = WsRequest(method = "SUBSCRIBE", params = streams, id = 1)
//                sendSerialized(subscribeRequest)
//
//                println("Subscribed to: $streams")
//
//                val job = launch {
//                    incoming.consumeEach { frame ->
//                        when (frame) {
//                            is Frame.Text -> {
//                                val text = frame.readText()
//                                try {
//                                    val ticker =
//                                        Json.decodeFromString(MiniTickerDTO.serializer(), text)
//                                    println("MiniTicker: ${ticker.s} | Close: ${ticker.c} | High: ${ticker.h} | Low: ${ticker.l}")
//                                    _currencyFlow.emit(ticker.toCurrencyInfoEntity())
//                                } catch (_: Exception) {
//                                    // Might be a subscribe/unsubscribe response
//                                    val resp = Json.decodeFromString(WsResponse.serializer(), text)
//                                    println("Response: $resp")
//                                }
//                            }
//
//                            is Frame.Ping -> send(Frame.Pong(frame.buffer))
//                            is Frame.Close -> close(
//                                frame.readReason() ?: CloseReason(CloseReason.Codes.NORMAL, "Bye")
//                            )
//
//                            else -> {}
//                        }
//                    }
//                }
//
//                // Keep the connection alive for demo (e.g., 30s)
//                delay(30_000)
//
//                // Unsubscribe
//                val unsubscribeRequest = WsRequest(method = "UNSUBSCRIBE", params = streams, id = 2)
//                sendSerialized(unsubscribeRequest)
//                println("Unsubscribed from: $streams")
//
//                delay(2000)
////                job.cancelAndJoin()
//            }
//
//            client.close()
//        }
//    }
}