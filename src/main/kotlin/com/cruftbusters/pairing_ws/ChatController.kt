package com.cruftbusters.pairing_ws

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.lang.RuntimeException
import java.util.*

fun Application.chatController() {
  routing {
    val webSockets = Collections.synchronizedList<DefaultWebSocketSession>(mutableListOf())
    webSocket("/chat") {
      webSockets.add(this)
      for (frame in incoming) {
        when (frame) {
          is Frame.Text -> webSockets
            .filter { it != this }
            .forEach { it.send(frame) }
          else -> throw RuntimeException("unknown frame type")
        }
      }
    }
  }
}
