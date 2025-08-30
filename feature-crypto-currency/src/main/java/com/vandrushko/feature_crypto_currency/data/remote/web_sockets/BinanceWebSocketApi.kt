package com.vandrushko.feature_crypto_currency.data.remote.web_sockets

import com.vandrushko.core.di.remote.WebSocketClient
import com.vandrushko.feature_crypto_currency.data.local.entity.CurrencyEntity
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto.MiniTickerDTO
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto.WsRequest
import com.vandrushko.feature_crypto_currency.data.remote.web_sockets.dto.WsResponse
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
import kotlinx.serialization.json.Json

private const val BINANCE_WS_URL = "wss://stream.binance.com:9443/ws"

class WebSocketBinanceApi(
    @WebSocketClient private val client: HttpClient, private val scope: CoroutineScope
) : WebSocketCurrencyMarketApi {

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
            val symbol = ticker.s.lowercase()
            if (symbol !in set) {
                set.add(symbol)
            }
            _currencyFlow.emit(ticker.toCurrencyInfoEntity())
        } catch (_: Exception) {
            val resp = Json.decodeFromString(WsResponse.serializer(), text)
            println("Response: $resp")
        }
    }

    private val set = mutableSetOf<String>()


    override suspend fun subscribeSymbol(symbols: List<String>, id: Int) {
        if (set.containsAll(symbols)) return
        val filtered = symbols.filter { it !in set }
        val streams = filtered.map { "${it}@miniTicker" }
        val request = WsRequest(method = "SUBSCRIBE", params = streams, id = id)
        session?.sendSerialized(request)
        println("Subscribed to: $streams")
    }

    override suspend fun unsubscribeSymbol(symbols: List<String>, id: Int) {
        val streams = symbols.map { "${it}@miniTicker" }
        val request = WsRequest(method = "UNSUBSCRIBE", params = streams, id = id)
        session?.sendSerialized(request).let {
            set.removeAll(symbols)
        }
        println("Unsubscribed from: $streams")
    }

    override fun stopConnection() {
        scope.launch {
            session?.close()
        }
        client.close()
    }

}